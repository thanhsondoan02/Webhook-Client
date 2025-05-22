package com.peswoc.hookclient.util.convert;

import com.peswoc.hookclient.constant.HookScope;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class HookScopeConverter implements AttributeConverter<HookScope, Integer> {

  @Override
  public Integer convertToDatabaseColumn(HookScope state) {
    return state != null ? state.getCode() : null;
  }

  @Override
  public HookScope convertToEntityAttribute(Integer integer) {
    return integer != null ? HookScope.fromCode(integer) : null;
  }
}