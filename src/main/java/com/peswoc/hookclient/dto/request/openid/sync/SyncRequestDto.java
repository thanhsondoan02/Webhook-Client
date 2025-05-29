package com.peswoc.hookclient.dto.request.openid.sync;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class SyncRequestDto {
  private String connectionId;
  private List<String> scopes;
}
