package com.peswoc.hookclient.service;

import com.peswoc.hookclient.dto.request.openid.auth.OpenIdLoginRequestDto;
import com.peswoc.hookclient.dto.request.openid.connect.ConnectRequestDto;
import com.peswoc.hookclient.dto.request.openid.webhook.RegisterWebhookRequestDto;
import com.peswoc.hookclient.dto.response.auth.JwtResponseDto;
import com.peswoc.hookclient.dto.response.base.BaseResponseDto;
import com.peswoc.hookclient.dto.response.group.GroupListResponseDto;
import com.peswoc.hookclient.dto.response.openid.connect.ConnectionDto;
import com.peswoc.hookclient.dto.response.openid.event.ScopeListDto;
import com.peswoc.hookclient.dto.response.openid.webhook.WebhookListResponseDto;
import com.peswoc.hookclient.dto.response.openid.webhook.WebhookResponseDto;
import com.peswoc.hookclient.dto.response.post.PostListResponseDto;
import com.peswoc.hookclient.dto.response.user.UserListResponseDto;

public interface IApiService {
  BaseResponseDto<ConnectionDto> registerConnection(String url, ConnectRequestDto body);

  BaseResponseDto<JwtResponseDto> getAccessToken(String url, OpenIdLoginRequestDto body);

  BaseResponseDto<UserListResponseDto> getUsers(String url, String token);

  BaseResponseDto<PostListResponseDto> getPosts(String url, String token);

  BaseResponseDto<GroupListResponseDto> getGroups(String url, String token);

  ScopeListDto getScopeEvents(String url, String token);

  WebhookResponseDto registerWebhook(String url, String token, RegisterWebhookRequestDto body);

  WebhookListResponseDto getWebhooks(String url, String token);

  void deleteWebhook(String url, String token);
}
