package com.peswoc.hookclient.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HookEvent {
  USER_CREATED(0),
  USER_UPDATED(1),
  USER_DELETED(2),

  GROUP_CREATED(3),
  GROUP_UPDATED(4),
  GROUP_DELETED(5),

  GROUP_USER_ADDED(6),
  GROUP_USER_REMOVED(7),

  POST_CREATED(8),
  POST_UPDATED(9),
  POST_DELETED(10);

  private final int code;

  public static HookEvent fromCode(int code) {
    for (HookEvent v : values()) {
      if (v.code == code) return v;
    }
    throw new IllegalArgumentException("Invalid code: " + code);
  }
}
