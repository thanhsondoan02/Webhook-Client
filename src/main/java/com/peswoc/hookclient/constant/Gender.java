package com.peswoc.hookclient.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Gender {
  MALE(0),
  FEMALE(1);

  private final int code;

  public static Gender fromCode(int code) {
    for (Gender gender : values()) {
      if (gender.code == code) return gender;
    }
    throw new IllegalArgumentException("Invalid code: " + code);
  }
}
