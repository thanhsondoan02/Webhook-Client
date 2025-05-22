package com.peswoc.hookclient.model.base;

import com.peswoc.hookclient.constant.State;
import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class StateReadConverter implements Converter<Integer, State> {
  @Override
  public State convert(@NonNull Integer source) {
    return State.fromCode(source);
  }
}

