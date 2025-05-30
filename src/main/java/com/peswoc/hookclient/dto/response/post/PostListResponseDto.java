package com.peswoc.hookclient.dto.response.post;

import com.peswoc.hookclient.model.post.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PostListResponseDto {
  private int total;
  private List<PostResponseDto> posts;

  public PostListResponseDto(List<Post> posts) {
    this.total = posts.size();
    this.posts = posts.stream()
      .map(PostResponseDto::new)
      .toList();
  }
}

