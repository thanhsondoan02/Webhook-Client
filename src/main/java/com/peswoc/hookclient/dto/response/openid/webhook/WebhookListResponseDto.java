package com.peswoc.hookclient.dto.response.openid.webhook;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class WebhookListResponseDto {
  private List<WebhookResponseDto> webhooks;

//  public WebhookListResponseDto(List<Webhook> webhooks) {
//    this.webhooks = webhooks.stream()
//      .map(WebhookResponseDto::new)
//      .toList();
//  }
}

