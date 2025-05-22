package com.peswoc.hookclient.model.base;

import com.peswoc.hookclient.constant.State;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class StateConverter implements AttributeConverter<State, Integer> {

  @Override
  public Integer convertToDatabaseColumn(State state) {
    return state != null ? state.getCode() : null;
  }

  @Override
  public State convertToEntityAttribute(Integer integer) {
    return integer != null ? State.fromCode(integer) : null;
  }
}