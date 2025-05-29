package com.peswoc.hookclient.service.impl;

import com.peswoc.hookclient.dto.request.openid.auth.OpenIdLoginRequestDto;
import com.peswoc.hookclient.dto.request.openid.connect.ConnectRequestDto;
import com.peswoc.hookclient.dto.response.auth.JwtResponseDto;
import com.peswoc.hookclient.dto.response.base.BaseResponseDto;
import com.peswoc.hookclient.dto.response.group.GroupListResponseDto;
import com.peswoc.hookclient.dto.response.openid.connect.ConnectionDto;
import com.peswoc.hookclient.dto.response.post.PostListResponseDto;
import com.peswoc.hookclient.dto.response.user.UserListResponseDto;
import com.peswoc.hookclient.service.IApiService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ApiService implements IApiService {

  private final RestTemplate restTemplate;

  public ApiService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  @Override
  public BaseResponseDto<ConnectionDto> registerConnection(String url, ConnectRequestDto body) {
    var request = new HttpEntity<>(body);
    var response = restTemplate.exchange(
      url, HttpMethod.POST, request,
      new ParameterizedTypeReference<BaseResponseDto<ConnectionDto>>() {
      }
    );
    return response.getBody();
  }

  @Override
  public BaseResponseDto<JwtResponseDto> getAccessToken(String url, OpenIdLoginRequestDto body) {
    var request = new HttpEntity<>(body);
    var response = restTemplate.exchange(
      url, HttpMethod.POST, request,
      new ParameterizedTypeReference<BaseResponseDto<JwtResponseDto>>() {
      }
    );
    return response.getBody();
  }

  @Override
  public BaseResponseDto<UserListResponseDto> getUsers(String url, String token) {
    var request = new HttpEntity<>(headersFromBearToken(token));
    var response = restTemplate.exchange(
      url, HttpMethod.GET, request,
      new ParameterizedTypeReference<BaseResponseDto<UserListResponseDto>>() {
      }
    );
    return response.getBody();
  }

  @Override
  public BaseResponseDto<PostListResponseDto> getPosts(String url, String token) {
    var request = new HttpEntity<>(headersFromBearToken(token));
    var response = restTemplate.exchange(
      url, HttpMethod.GET, request,
      new ParameterizedTypeReference<BaseResponseDto<PostListResponseDto>>() {
      }
    );
    return response.getBody();
  }

  @Override
  public BaseResponseDto<GroupListResponseDto> getGroups(String url, String token) {
    var request = new HttpEntity<>(headersFromBearToken(token));
    var response = restTemplate.exchange(
      url, HttpMethod.GET, request,
      new ParameterizedTypeReference<BaseResponseDto<GroupListResponseDto>>() {
      }
    );
    return response.getBody();
  }

  private HttpHeaders headersFromBearToken(String token) {
    var headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + token);
    return headers;
  }
}
