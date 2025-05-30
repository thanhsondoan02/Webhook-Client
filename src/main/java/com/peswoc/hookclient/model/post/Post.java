package com.peswoc.hookclient.model.post;

import com.peswoc.hookclient.dto.response.post.PostResponseDto;
import com.peswoc.hookclient.model.base.BaseMongoEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Document(collection = "posts")
@NoArgsConstructor
public class Post extends BaseMongoEntity {

  @Field("title")
  private String title;

  @Field("content")
  private String content;

  @Field("user_id")
  @Indexed
  private String userId;

  public Post(String title, String content, String userId) {
    super();
    this.title = title;
    this.content = content;
    this.userId = userId;
  }

  public static Post fromDto(PostResponseDto dto) {
    Post post = new Post();
    post.setId(dto.getId());
    post.setTitle(dto.getTitle());
    post.setContent(dto.getContent());
    post.setUserId(dto.getUserId());
    return post;
  }
}
