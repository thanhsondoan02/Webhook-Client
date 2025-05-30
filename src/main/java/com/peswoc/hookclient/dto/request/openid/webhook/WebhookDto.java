package com.peswoc.hookclient.dto.request.openid.webhook;

import com.peswoc.hookclient.dto.response.openid.event.EventDto;
import com.peswoc.hookclient.dto.response.openid.event.ScopeDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class WebhookDto<T> {
  String connectionId;
  ScopeDto scope;
  EventDto event;
  T data;
}
