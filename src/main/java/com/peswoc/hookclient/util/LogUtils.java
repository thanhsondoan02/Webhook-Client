package com.peswoc.hookclient.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtils {

  private static final Logger defaultLogger = LoggerFactory.getLogger("Default");

  public static void log(String message) {
    defaultLogger.info(message);
  }

  public static void log(String loggerName, String message) {
    Logger logger = LoggerFactory.getLogger(loggerName);
    logger.info(message);
  }

  public static void log(Class<?> clazz, String message) {
    Logger logger = LoggerFactory.getLogger(clazz);
    logger.info(message);
  }
}
