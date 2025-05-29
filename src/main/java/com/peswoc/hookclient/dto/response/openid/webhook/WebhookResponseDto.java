package com.peswoc.hookclient.dto.response.openid.webhook;

import com.peswoc.hookclient.dto.response.openid.event.EventDto;
import com.peswoc.hookclient.dto.response.openid.event.ScopeDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class WebhookResponseDto {
  private ScopeDto scope;
  private EventDto event;
  private String redirectUrl;

//  public WebhookResponseDto(Webhook webhook) {
//    event = new EventDto();
//
//    var eventScope = webhook.getEventScope();
//    event.setName(eventScope.getEvent().getName());
//
//    scope = new ScopeDto();
//    scope.setName(eventScope.getScope().getName());
//
//    redirectUrl = webhook.getRedirectUrl();
//  }
}

