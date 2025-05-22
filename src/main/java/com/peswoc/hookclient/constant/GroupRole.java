package com.peswoc.hookclient.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum GroupRole {
  MEMBER(0),
  ADMIN(1);

  private final int code;

  public static GroupRole fromCode(int code) {
    for (GroupRole v : values()) {
      if (v.code == code) return v;
    }
    throw new IllegalArgumentException("Invalid code: " + code);
  }

  public static GroupRole fromString(String role) {
    for (GroupRole v : values()) {
      if (Objects.equals(v.toString(), role)) return v;
    }
    throw new IllegalArgumentException("Invalid role: " + role);
  }
}
