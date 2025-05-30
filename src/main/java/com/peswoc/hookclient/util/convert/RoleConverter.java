package com.peswoc.hookclient.util.convert;

import com.peswoc.hookclient.constant.Role;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class RoleConverter implements AttributeConverter<Role, Integer> {

  @Override
  public Integer convertToDatabaseColumn(Role role) {
    return role != null ? role.getCode() : null;
  }

  @Override
  public Role convertToEntityAttribute(Integer integer) {
    return integer != null ? Role.fromCode(integer) : null;
  }
}