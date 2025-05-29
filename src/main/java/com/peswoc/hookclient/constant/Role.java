package com.peswoc.hookclient.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
  USER(0),
  ADMIN(1);

  private final int code;

  public static Role fromCode(int code) {
    for (Role v : values()) {
      if (v.code == code) return v;
    }
    throw new IllegalArgumentException("Invalid code: " + code);
  }

  public static Role fromString(String role) {
    for (Role v : values()) {
      if (v.toString().equalsIgnoreCase(role)) return v;
    }
    throw new IllegalArgumentException("Invalid role: " + role);
  }
}
