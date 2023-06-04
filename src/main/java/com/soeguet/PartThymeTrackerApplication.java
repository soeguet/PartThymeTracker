package com.soeguet;

import com.soeguet.tracker.model.Authority;
import com.soeguet.tracker.model.CustomUser;
import com.soeguet.tracker.service.WorkerService;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class PartThymeTrackerApplication implements CommandLineRunner {

  private final WorkerService workerService;

  public PartThymeTrackerApplication(WorkerService workerService) {
    this.workerService = workerService;
  }

  public static void main(String[] args) {
    SpringApplication.run(PartThymeTrackerApplication.class, args);
  }

  @Override
  public void run(String... args) {

    CustomUser customUser = new CustomUser();
    customUser.setUsername("user");
    customUser.setPassword(new BCryptPasswordEncoder().encode("password123"));
    customUser.setEnabled(true);
    customUser.setAccountNonLocked(true);
    customUser.setAccountNonExpired(true);
    customUser.setCredentialsNonExpired(true);
    customUser.setAuthorities(List.of(new Authority("ROLE_USER")));
    workerService.addInitialUserToDatabase(customUser);
  }
}
