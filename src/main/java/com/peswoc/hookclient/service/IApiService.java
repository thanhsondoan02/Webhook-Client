package com.peswoc.hookclient.service;

import com.peswoc.hookclient.dto.request.openid.connect.ConnectRequestDto;
import com.peswoc.hookclient.dto.response.base.BaseResponseDto;
import com.peswoc.hookclient.dto.response.openid.connect.ConnectionDto;

public interface IApiService {
  void setServerUrl(String serverUrl);
  BaseResponseDto<ConnectionDto> registerConnection(ConnectRequestDto body);
}
