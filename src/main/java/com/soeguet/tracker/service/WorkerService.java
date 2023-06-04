package com.soeguet.tracker.service;

import com.soeguet.tracker.model.Assistant;
import com.soeguet.tracker.model.CustomUser;
import com.soeguet.tracker.model.DTOs.*;
import com.soeguet.tracker.model.WorkTime;
import com.soeguet.tracker.repository.AssistantRepository;
import com.soeguet.tracker.repository.CustomUserRepository;
import com.soeguet.tracker.repository.WorkTimeRepository;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class WorkerService {
  private final AssistantRepository assistantRepository;
  private final WorkTimeRepository workTimeRepository;
  private final CustomUserRepository customUserRepository;
  private final PasswordEncoder bCryptPasswordEncoder;

  public WorkerService(
      AssistantRepository assistantRepository,
      WorkTimeRepository workTimeRepository,
      CustomUserRepository customUserRepository,
      PasswordEncoder bCryptPasswordEncoder) {
    this.assistantRepository = assistantRepository;
    this.workTimeRepository = workTimeRepository;
    this.customUserRepository = customUserRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  public List<AssistantOverviewDTO> getAllAssistants() {

    List<Assistant> allAssistants = assistantRepository.findAll();
    return allAssistants.stream()
        .map(
            worker -> {
              List<WorkTime> assistantWorkTimeList =
                  workTimeRepository.findAllByAssistantId(worker.getId());

              Duration worktimeThisMonth =
                  assistantWorkTimeList.stream()
                      .filter(
                          time ->
                              time.getStartTime().getMonth().equals(LocalDateTime.now().getMonth()))
                      .map(WorkTime::getTotalTime)
                      .reduce(Duration.ZERO, Duration::plus);
              String timeInHHMMtM = convertDurationToReadableTimeString(worktimeThisMonth);

              Duration worktimeLastMonth =
                  assistantWorkTimeList.stream()
                      .filter(
                          time ->
                              time.getStartTime()
                                  .getMonth()
                                  .equals(LocalDateTime.now().getMonth().minus(1)))
                      .map(WorkTime::getTotalTime)
                      .reduce(Duration.ZERO, Duration::plus);
              String timeInHHMMlM = convertDurationToReadableTimeString(worktimeLastMonth);

              return new AssistantOverviewDTO(
                  worker.getId(), worker.getName(), timeInHHMMtM, timeInHHMMlM);
            })
        .toList();
  }

  public List<AssistantTotalOverviewDTO> fetchAllAssistantsDataForDashboard() {

    List<Assistant> allAssistants = assistantRepository.findAll();
    return allAssistants.stream()
        .map(
            worker -> {
              List<WorkTime> assistantWorkTimeList =
                  workTimeRepository.findAllByAssistantId(worker.getId());

              Duration worktimeThisMonth =
                  assistantWorkTimeList.stream()
                      .map(WorkTime::getTotalTime)
                      .reduce(Duration.ZERO, Duration::plus);
              String totalTime = convertDurationToReadableTimeString(worktimeThisMonth);

              return new AssistantTotalOverviewDTO(worker.getId(), worker.getName(), totalTime);
            })
        .toList();
  }

  private String convertDurationToReadableTimeString(Duration worktimeThisMonth) {
    long secondsThisMonth = worktimeThisMonth.getSeconds();
    long HH = secondsThisMonth / 3600;
    long MM = (secondsThisMonth % 3600) / 60;
    return String.format("%02d:%02d", HH, MM);
  }

  private String convertWorkingToUnderstandableString(Duration worktimeThisMonth) {
    long secondsThisMonth = worktimeThisMonth.getSeconds();
    long HH = secondsThisMonth / 3600;
    long MM = (secondsThisMonth % 3600) / 60;
    return String.format("%01d Stunden und %01d Minuten.", HH, MM);
  }

  public void addAssistant(Assistant assistant) {
    assistantRepository.save(assistant);
  }

  public AssistantDTO getAssistantById(UUID assistantId) {
    Assistant assistant = assistantRepository.getReferenceById(assistantId);
    return new AssistantDTO(assistant.getId(), assistant.getName(), assistant.getNfcTagId());
  }

  public void updateAssistant(AssistantDTO assistantDTO) {

    Assistant assistantUpdate = assistantRepository.getReferenceById(assistantDTO.id());
    assistantUpdate.setName(assistantDTO.name());
    assistantUpdate.setNfcTagId(assistantDTO.nfcTagId());
    assistantRepository.save(assistantUpdate);
  }

  public void deleteAssistant(UUID assistantId) {
    assistantRepository.deleteById(assistantId);
    workTimeRepository.deleteByAssistantId(assistantId);
  }

  public void save(WorkTimeDTO workTimeDTO) throws Exception {

    if (workTimeDTO.startTime().isAfter(workTimeDTO.endTime())) {
      throw new Exception("Start-Time after End-Time!");
    }

    WorkTime workTime =
        new WorkTime(
            workTimeDTO.assistantId(),
            workTimeDTO.startTime(),
            workTimeDTO.endTime(),
            workTimeDTO.workingStatus(),
            workTimeDTO.info());

    workTimeRepository.save(workTime);
  }

  public List<WorkTimeAssistantDTO> getAllWorkTime() {

    List<Assistant> assistantList = assistantRepository.findAll();
    List<WorkTime> workTimeList = workTimeRepository.findAll();

    return workTimeList.stream()
        .map(
            workTime -> {
              Optional<Assistant> assistant =
                  assistantList.stream()
                      .filter(co -> co.getId().equals(workTime.getAssistantId()))
                      .findAny();

              String assistantName = assistant.orElseThrow().getName();

              return new WorkTimeAssistantDTO(
                  workTime.getId(),
                  workTime.getAssistantId(),
                  assistantName,
                  workTime.getStartTime(),
                  workTime.getEndTime(),
                  convertDurationToReadableTimeString(
                      Duration.between(workTime.getStartTime(), workTime.getEndTime())),
                  workTime.getInfo());
            })
        .sorted(Comparator.comparing(WorkTimeAssistantDTO::startTime).reversed())
        .toList();
  }

  public WorkTimeDTO getWorkTimeById(UUID id) {

    WorkTime work = workTimeRepository.getReferenceById(id);

    return new WorkTimeDTO(
        work.getId(),
        work.getAssistantId(),
        work.getStartTime(),
        work.getEndTime(),
        work.isWorkingStatus(),
        work.getInfo());
  }

  public void editWorkTime(WorkTimeDTO workTimeDTO) {

    WorkTime workTime = workTimeRepository.getReferenceById(workTimeDTO.id());
    workTime.setStartTime(workTimeDTO.startTime());
    workTime.setEndTime(workTimeDTO.endTime());
    workTime.setInfo(workTimeDTO.info());
    workTimeRepository.save(workTime);
  }

  public List<WorkTimeAssistantDTO> getAllWorkTimeById(UUID assistantId) {

    Assistant assistant = assistantRepository.getReferenceById(assistantId);

    String assistantName = assistant.getName();
    List<WorkTime> workTimeList = workTimeRepository.findAllByAssistantId(assistantId);

    return workTimeList.stream()
        .map(
            time ->
                new WorkTimeAssistantDTO(
                    time.getId(),
                    time.getAssistantId(),
                    assistantName,
                    time.getStartTime(),
                    time.getEndTime(),
                    convertDurationToReadableTimeString(
                        Duration.between(time.getStartTime(), time.getEndTime())),
                    time.getInfo()))
        .sorted(Comparator.comparing(WorkTimeAssistantDTO::startTime).reversed())
        .toList();
  }

  public AssistantTotalOverviewDTO getAllWorkTimeOverviewById(UUID assistantId) {

    Assistant assistant = assistantRepository.getReferenceById(assistantId);
    String assistantName = assistant.getName();

    List<WorkTime> workTimes = workTimeRepository.findAllByAssistantId(assistantId);

    Duration workDuration =
        workTimes.stream()
            .map(time -> Duration.between(time.getStartTime(), time.getEndTime()))
            .reduce(Duration::plus)
            .orElse(Duration.ofMinutes(0));

    return new AssistantTotalOverviewDTO(
        assistantId, assistantName, convertDurationToReadableTimeString(workDuration));
  }

  public ResponseEntity<String> logPartTimeWorkTime(String nfcId) {

    Assistant assistant = getAssistantByNFCTagId(nfcId);
    List<WorkTime> workTimeList = workTimeRepository.findAllByAssistantId(assistant.getId());
    Optional<WorkTime> workTimeToday =
        workTimeList.stream().filter(WorkTime::isWorkingStatus).findAny();

    if (workTimeToday.isPresent()
        && workTimeToday.get().isWorkingStatus()
        && workTimeToday.get().getStartTime().getDayOfMonth()
            == LocalDateTime.now().getDayOfMonth()) {
      workTimeToday.get().setEndTime(LocalDateTime.now());
      workTimeRepository.save(workTimeToday.get());

      return new ResponseEntity<>(
          "ausgeloggt, "
              + assistant.getName()
              + ". Deine heutige Arbeitszeit beträgt "
              + convertWorkingToUnderstandableString(workTimeToday.get().getTotalTime()),
          HttpStatusCode.valueOf(201));
    } else {
      // autoclose open/forgotten worktime -> while using new object since a new day has started
      if (workTimeToday.isPresent() && workTimeToday.get().isWorkingStatus()) {
        workTimeToday.get().setEndTime(workTimeToday.get().getStartTime());
      }

      WorkTime workTime = new WorkTime();
      workTime.setAssistantId(assistant.getId());
      workTime.setStartTime(LocalDateTime.now());
      workTimeRepository.save(workTime);

      return new ResponseEntity<>(
          "eingeloggt, " + assistant.getName(), HttpStatusCode.valueOf(200));
    }
  }

  private Assistant getAssistantByNFCTagId(String nfcId) {
    return assistantRepository.findByNfcTagId(nfcId).orElse(new Assistant());
  }

  public void deleteWorkTime(UUID id) {
    workTimeRepository.deleteById(id);
  }

  public void addNewUserToDatabase(AddUserDTO addUserDTO) {

    if (!addUserDTO.password().equals(addUserDTO.password2())) {
      throw new InputMismatchException("password and password2 don't match");
    }
    customUserRepository.save(
        new CustomUser(
            addUserDTO.username(),
            bCryptPasswordEncoder.encode(addUserDTO.password()),
            addUserDTO.role()));
  }

  public void addInitialUserToDatabase(CustomUser customUser) {

    Optional<CustomUser> byUsername = customUserRepository.findByUsername(customUser.getUsername());

    if (byUsername.isPresent()) {
      return;
    }

    customUserRepository.save(customUser);
  }
}
