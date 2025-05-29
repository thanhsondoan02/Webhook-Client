package com.peswoc.hookclient.dto.request.openid.webhook;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegisterWebhookRequestDto {
  private Integer scope;
  private Integer event;
  private String redirectUrl;
}
