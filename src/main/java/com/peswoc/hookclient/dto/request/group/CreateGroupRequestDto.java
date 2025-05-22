package com.peswoc.hookclient.dto.request.group;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CreateGroupRequestDto {
  private String name;
  private String thumbnail;
  private List<GroupUserRequestDto> users;
}
