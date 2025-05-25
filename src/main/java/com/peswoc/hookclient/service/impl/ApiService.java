package com.peswoc.hookclient.service.impl;

import com.peswoc.hookclient.dto.request.openid.connect.ConnectRequestDto;
import com.peswoc.hookclient.dto.response.base.BaseResponseDto;
import com.peswoc.hookclient.dto.response.openid.connect.ConnectionDto;
import com.peswoc.hookclient.service.IApiService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ApiService implements IApiService {

  private final RestTemplate restTemplate;
  private String serverUrl;

  public ApiService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  @Override
  public void setServerUrl(String serverUrl) {
    this.serverUrl = serverUrl;
  }

  @Override
  public BaseResponseDto<ConnectionDto> registerConnection(ConnectRequestDto body) {
    var request = new HttpEntity<>(body);
    var response = restTemplate.exchange(
      serverUrl + "/api/connections",
      HttpMethod.POST,
      request,
      new ParameterizedTypeReference<BaseResponseDto<ConnectionDto>>() {
      }
    );
    return response.getBody();
  }
}
