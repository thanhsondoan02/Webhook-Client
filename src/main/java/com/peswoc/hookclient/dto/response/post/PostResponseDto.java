package com.peswoc.hookclient.dto.response.post;

import com.peswoc.hookclient.model.post.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostResponseDto {
  private String id;
  private String title;
  private String content;
  private String userId;

  public PostResponseDto(Post post) {
    this.id = post.getId();
    this.title = post.getTitle();
    this.content = post.getContent();
    this.userId = post.getUserId();
  }

  public Post toPost() {
    Post post = new Post();
    post.setId(id);
    post.setTitle(title);
    post.setContent(content);
    post.setUserId(userId);
    return post;
  }
}

