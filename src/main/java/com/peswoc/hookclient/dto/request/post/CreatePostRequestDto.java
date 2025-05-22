package com.peswoc.hookclient.dto.request.post;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreatePostRequestDto {
  @NotNull
  private String title;
  @NotNull
  private String content;
}
