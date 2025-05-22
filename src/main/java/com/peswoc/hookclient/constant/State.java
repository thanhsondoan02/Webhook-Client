package com.peswoc.hookclient.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum State {
  INACTIVE(0),
  ACTIVE(1);

  private final int code;

  public static State fromCode(int code) {
    for (State state : values()) {
      if (state.code == code) return state;
    }
    throw new IllegalArgumentException("Invalid code: " + code);
  }
}
