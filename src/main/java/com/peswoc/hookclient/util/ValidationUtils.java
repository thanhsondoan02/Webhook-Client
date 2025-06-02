package com.peswoc.hookclient.util;

import com.peswoc.hookclient.constant.Gender;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.regex.Pattern;

public class ValidationUtils {

  private static final Pattern EMAIL_REGEX = Pattern.compile(
    "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
    Pattern.CASE_INSENSITIVE
  );

  private static final Pattern PASSWORD_REGEX = Pattern.compile(
    "^(?=.*[a-z])(?=.*[A-Z])(?=.*[\\W_]).{6,20}$"
  );

  private ValidationUtils() {
  }

  public static boolean isEmailValid(String email) {
    return email != null && EMAIL_REGEX.matcher(email).matches();
  }

  public static boolean isPasswordValid(String password) {
    return password != null && PASSWORD_REGEX.matcher(password).matches();
  }

  public static boolean isUsernameValid(String username) {
    return username != null && username.length() >= 6 && username.length() <= 20;
  }

  public static boolean isGenderValid(Integer gender) {
    if (gender == null) return false;
    try {
      Gender.fromCode(gender);
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }

  public static boolean isDateOfBirthValid(Long timestamp) {
    if (timestamp == null) return false;

    try {
      Instant instant = Instant.ofEpochMilli(timestamp);
      LocalDate dob = instant.atZone(ZoneId.systemDefault()).toLocalDate();

      LocalDate today = LocalDate.now();
      LocalDate minDate = today.minusYears(80); // oldest allowed
      LocalDate maxDate = today.minusYears(18); // youngest allowed

      return dob.isAfter(minDate) && dob.isBefore(maxDate);

    } catch (Exception e) {
      return false; // e.g., timestamp too large/small
    }
  }

  public static boolean isPostTitleValid(String title) {
    return title != null && title.length() >= 5 && title.length() <= 20;
  }

  public static boolean isPostContentValid(String content) {
    return content != null && content.length() >= 10 && content.length() <= 100;
  }

  public static boolean isGroupNameValid(String name) {
    return name != null && !name.isEmpty() && name.length() <= 50;
  }

  public static boolean isValidDomain(String domain) {
    if (domain == null) return false;
    try {
      URL url = new URL(domain);
      return url.getProtocol().equals("https") || url.getProtocol().equals("http");
    } catch (MalformedURLException e) {
      return false;
    }
  }

  public static boolean isValidServerName(String name) {
    return name != null && name.length() >= 5 && name.length() <= 20;
  }

  public static boolean isValidCallbackUrl(String domain, String callbackUrl) {
    if (domain == null || callbackUrl == null) return false;
    try {
      URL domainUrl = new URL(domain);
      URL cbUrl = new URL(callbackUrl);
      return domainUrl.getHost().equals(cbUrl.getHost());
    } catch (MalformedURLException e) {
      return false;
    }
  }
}

