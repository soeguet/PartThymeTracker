package com.soeguet.tracker.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Authority implements GrantedAuthority {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String authority;

  public Authority(String authority) {
    this.authority = authority;
  }

  @Override
  public String getAuthority() {
    return authority;
  }

  @Override
  public String toString() {
    return "Authority{" + "id=" + id + ", authority='" + authority + '\'' + '}';
  }
}
