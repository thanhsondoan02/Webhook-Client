package com.peswoc.hookclient.constant;

public final class MessageConst {

  // Common
  public static final String INTERNAL_SERVER_ERROR = "Internal server error";
  public static final String MISSING_REQUIRED_FIELD = "Missing required field";
  public static final String INVALID_INPUT_FORMAT = "Invalid input format";
  public static final String UNAUTHORIZED = "Unauthorized";
  public static final String ACCESS_DENIED = "Access denied";
  public static final String SUCCESS = "Success";
  public static final String BAD_REQUEST = "Bad request";

  // Auth
  public static final String REGISTER_SUCCESS = "User registered successfully";
  public static final String EMAIL_OR_USERNAME_EXISTS = "Email or username already exists";
  public static final String INVALID_EMAIL_OR_PASSWORD = "Invalid email or password";
  public static final String PASSWORD_CHANGED_SUCCESS = "Password changed successfully";
  public static final String INVALID_OLD_PASSWORD = "Invalid old password";
  public static final String NEW_PASSWORD_SAME_AS_OLD = "New password must be different";

  // Post
  public static final String POST_CREATED_SUCCESSFULLY = "Post created successfully";
  public static final String POST_UPDATED_SUCCESSFULLY = "Post updated successfully";
  public static final String POST_DELETED_SUCCESSFULLY = "Post deleted successfully";
  public static final String POST_NOT_FOUND = "Post not found";
  public static final String USER_NOT_FOUND = "User not found";

  // Group
  public static final String DUPLICATE_USER_ID = "Duplicate userId";
  public static final String INVALID_ROLE = "Invalid role";
  public static final String GROUP_NOT_FOUND = "Group not found";
  public static final String USER_ALREADY_IN_GROUP = "User already in group";

  // Webhook
  public static final String CONNECTION_NOT_FOUND = "Connection not found";

  private MessageConst() {
  }
}
