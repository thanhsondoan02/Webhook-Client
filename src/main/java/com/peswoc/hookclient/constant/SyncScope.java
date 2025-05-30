package com.peswoc.hookclient.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SyncScope {
  USERS(0),
  POSTS(1),
  GROUPS(2);

  private final int code;

  public static SyncScope fromCode(int code) {
    for (SyncScope v : values()) {
      if (v.code == code) return v;
    }
    throw new IllegalArgumentException("Invalid code: " + code);
  }

  public static SyncScope fromString(String scope) {
    for (SyncScope v : values()) {
      if (v.toString().equalsIgnoreCase(scope)) return v;
    }
    throw new IllegalArgumentException("Invalid scope: " + scope);
  }
}
