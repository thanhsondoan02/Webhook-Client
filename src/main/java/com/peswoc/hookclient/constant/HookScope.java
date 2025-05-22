package com.peswoc.hookclient.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HookScope {
  READ_USER(0),
  READ_GROUP(1),
  READ_POST(2);

  private final int code;

  public static HookScope fromCode(int code) {
    for (HookScope v : values()) {
      if (v.code == code) return v;
    }
    throw new IllegalArgumentException("Invalid code: " + code);
  }
}
