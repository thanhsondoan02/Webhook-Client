package com.peswoc.hookclient.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ServerOwner {
  OWN(0),
  EXTERNAL(1);

  private final int code;

  public static ServerOwner fromCode(int code) {
    for (ServerOwner state : values()) {
      if (state.code == code) return state;
    }
    throw new IllegalArgumentException("Invalid code: " + code);
  }
}
