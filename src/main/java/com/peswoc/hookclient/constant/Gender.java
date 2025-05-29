package com.peswoc.hookclient.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

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

  public static Gender fromString(String gender) {
    for (Gender v : values()) {
      if (Objects.equals(v.toString(), gender)) return v;
    }
    throw new IllegalArgumentException("Invalid gender: " + gender);
  }
}
