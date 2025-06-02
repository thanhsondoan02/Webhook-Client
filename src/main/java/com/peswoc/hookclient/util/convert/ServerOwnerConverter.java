package com.peswoc.hookclient.util.convert;

import com.peswoc.hookclient.constant.ServerOwner;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class ServerOwnerConverter implements AttributeConverter<ServerOwner, Integer> {

  @Override
  public Integer convertToDatabaseColumn(ServerOwner state) {
    return state != null ? state.getCode() : null;
  }

  @Override
  public ServerOwner convertToEntityAttribute(Integer integer) {
    return integer != null ? ServerOwner.fromCode(integer) : null;
  }
}