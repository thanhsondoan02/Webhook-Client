package com.peswoc.hookclient.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum ConnectionAction {
  ACCEPT(1),
  REJECT(0);

  private final int code;

  public static ConnectionAction fromCode(int code) {
    for (ConnectionAction v : values()) {
      if (v.code == code) return v;
    }
    throw new IllegalArgumentException("Invalid code: " + code);
  }

  public static ConnectionAction fromString(String action) {
    for (ConnectionAction v : values()) {
      if (Objects.equals(v.toString(), action)) return v;
    }
    throw new IllegalArgumentException("Invalid action: " + action);
  }
}
