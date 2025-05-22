package com.peswoc.hookclient.util;

import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;

import java.util.Arrays;
import java.util.List;

public class PasswordUtils {
  private static final PasswordGenerator generator = new PasswordGenerator();

  /**
   * Rules: at least one upper case, one lower case, one digit, and one special character,
   * no whitespace and length 12.
   */
  public static String generateSecurePassword() {
    List<CharacterRule> rules = Arrays.asList(
      new CharacterRule(EnglishCharacterData.UpperCase, 1),
      new CharacterRule(EnglishCharacterData.LowerCase, 1),
      new CharacterRule(EnglishCharacterData.Digit, 1),
      new CharacterRule(EnglishCharacterData.Special, 1)
    );

    return generator.generatePassword(12, rules);
  }
}
