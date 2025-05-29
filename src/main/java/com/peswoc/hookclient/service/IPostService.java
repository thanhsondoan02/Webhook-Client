package com.peswoc.hookclient.service;

import com.peswoc.hookclient.dto.request.post.CreatePostRequestDto;
import com.peswoc.hookclient.dto.request.post.UpdatePostRequestDto;
import com.peswoc.hookclient.dto.response.post.PostListResponseDto;
import com.peswoc.hookclient.dto.response.post.PostResponseDto;
import com.peswoc.hookclient.model.post.Post;

import java.util.List;

public interface IPostService {
  PostResponseDto createPost(String title, String content);

  boolean validateCreateFields(CreatePostRequestDto request);

  boolean validateCreateFormat(CreatePostRequestDto request);

  boolean validateUpdateFields(UpdatePostRequestDto request);

  boolean validateUpdateFormat(UpdatePostRequestDto request);

  Post getPostById(String postId);

  PostResponseDto updatePost(String id, String title, String content);

  void deletePost(String id);

  List<Post> getAll();

  List<Post> searchPostByTitle(String title);

  List<Post> searchPostByUser(String userId);

  List<Post> searchPostByTitleAndUser(String title, String userId);

  void syncPosts(PostListResponseDto data);
}
