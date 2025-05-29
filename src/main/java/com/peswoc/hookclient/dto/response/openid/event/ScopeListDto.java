package com.peswoc.hookclient.dto.response.openid.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ScopeListDto {
  private List<ScopeDto> scopes;

//  public ScopeListDto(List<Scope> scopes) {
//    this.scopes = scopes.stream()
//      .map(ScopeDto::new)
//      .toList();
//  }
}

