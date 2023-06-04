package com.soeguet.tracker.controller;

import com.soeguet.tracker.model.Assistant;
import com.soeguet.tracker.model.DTOs.*;
import com.soeguet.tracker.model.WorkTime;
import com.soeguet.tracker.service.WorkerService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
public class WorkerController {

    private final WorkerService workerService;

    public WorkerController(WorkerService workerService) {
        this.workerService = workerService;
    }

    @GetMapping("/")
    public String getHello(Model model, Authentication authentication) {

        model.addAttribute("user", authentication.getPrincipal());

        List<AssistantOverviewDTO> allWorker = workerService.getAllAssistants();
        model.addAttribute("assistantList", allWorker);

        return "assistants/index";
    }

    @GetMapping("/add-assistant")
    public String addAssistantForm(HttpServletRequest request, Model model) {

        previousPageReferer(model, request);

        Assistant assistant = new Assistant();
        model.addAttribute("assistant", assistant);
        return "assistants/add-assistant";
    }

    @PostMapping("/add-assistant")
    public String submitAssistantForm(@ModelAttribute Assistant assistant) {
        workerService.addAssistant(assistant);
        return "redirect:/";
    }

    @PostMapping("/update-assistant")
    public String updateAssistant(@ModelAttribute AssistantDTO assistantDTO) {
        workerService.updateAssistant(assistantDTO);
        return "redirect:/";
    }

    @PostMapping("/delete-assistant")
    public String deleteAssistant(@RequestParam("id") UUID id) {
        workerService.deleteAssistant(id);
        return "redirect:/";
    }

    @PostMapping("/delete-worktime")
    public String deleteWorkTime(@RequestParam("id") UUID id) {
        workerService.deleteWorkTime(id);
        return "redirect:/";
    }

    @GetMapping("/edit-assistant/{id}")
    public String showEditAssistantForm(@PathVariable("id") UUID id, Model model, HttpServletRequest request) {

        previousPageReferer(model, request);

        AssistantDTO assistantDTO = workerService.getAssistantById(id);
        model.addAttribute("assistants", assistantDTO);
        return "assistants/edit-assistant-form";
    }

    @GetMapping("/edit-work-time/{id}")
    public String showEditWorkTimeForm(@PathVariable("id") UUID id, Model model, HttpServletRequest request) {

        previousPageReferer(model, request);

        WorkTimeDTO workTime = workerService.getWorkTimeById(id);
        model.addAttribute("workTime", workTime);
        return "assistants/edit-work-time";
    }

    private void previousPageReferer(Model model, HttpServletRequest request) {
        String referer = request.getHeader("referer");
        if (referer == null) referer = "/";
        model.addAttribute("previousPage", referer);
    }

    @PostMapping("/edit-work-time")
    public String editWorkTimeForm(@ModelAttribute WorkTimeDTO workTimeDTO) {
        workerService.editWorkTime(workTimeDTO);
        return "redirect:/";
    }

    @GetMapping("/add-work-time")
    public String addWorkTimeForm(Model model, HttpServletRequest request) {

        previousPageReferer(model, request);

        model.addAttribute("workTime", new WorkTime());
        model.addAttribute("allAssistants", workerService.getAllAssistants());
        return "assistants/add-work-time";
    }

    @PostMapping("/add-work-time")
    public String submitWorkTimeForm(@ModelAttribute WorkTimeDTO workTimeDTO) {
        try {
            workerService.save(workTimeDTO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "redirect:/";
    }

    @GetMapping("/show-all-work-time")
    public String showAllWorkTime(Model model, HttpServletRequest request) {

        previousPageReferer(model, request);

        model.addAttribute("allWorkTime", workerService.getAllWorkTime());
        model.addAttribute("totalWorkTimePerAssistant", workerService.fetchAllAssistantsDataForDashboard());
        return "assistants/show-all-work-time";
    }

    @GetMapping("/work-time/{id}")
    public String showAssistantWorkTime(@PathVariable("id") UUID assistantId, Model model, HttpServletRequest request) {

        previousPageReferer(model, request);

        AssistantTotalOverviewDTO assistantTotalOverviewDTO = workerService.getAllWorkTimeOverviewById(assistantId);
        model.addAttribute("assistants", assistantTotalOverviewDTO);
        model.addAttribute("allWorkTime", workerService.getAllWorkTimeById(assistantId));
        return "assistants/assistant-work-time";
    }

    @PostMapping("/api/v1/log")
    public ResponseEntity<String> liveLogAssistants(@RequestParam String nfc) {
        return workerService.logPartTimeWorkTime(nfc);
    }


    @GetMapping("/add-user")
    public String addUserForm(HttpServletRequest request, Model model) {

        previousPageReferer(model, request);

        model.addAttribute("customUser", new AddUserDTO(null,null,null,null));
        return "assistants/add-user";
    }
    @PostMapping("/add-user")
    public String addUserForm(@ModelAttribute AddUserDTO addUserDTO) {
        workerService.addNewUserToDatabase(addUserDTO);
        return "redirect:/";
    }
}
