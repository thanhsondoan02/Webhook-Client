package com.peswoc.hookclient.dto.response.openid.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EventDto {
  private Integer code;
  private String name;
  private String description;

//  public EventDto(Event event) {
//    this.code = event.getType().getCode();
//    this.name = event.getName();
//    this.description = event.getDescription();
//  }
//
//  public Event toEvent() {
//    return new Event(code, name, description);
//  }
//
//  @JsonIgnore
//  public HookEvent getType() {
//    return HookEvent.fromCode(code);
//  }
}

