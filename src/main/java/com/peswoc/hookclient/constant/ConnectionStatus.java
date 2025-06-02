package com.peswoc.hookclient.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum ConnectionStatus {
  PENDING(0),
  ACCEPTED(1),
  REJECTED(-1);

  private final int code;

  public static ConnectionStatus fromCode(int code) {
    for (ConnectionStatus v : values()) {
      if (v.code == code) return v;
    }
    throw new IllegalArgumentException("Invalid code: " + code);
  }

  public static ConnectionStatus fromString(String status) {
    for (ConnectionStatus v : values()) {
      if (Objects.equals(v.toString(), status)) return v;
    }
    throw new IllegalArgumentException("Invalid status: " + status);
  }
}
