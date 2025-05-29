package com.peswoc.hookclient.service.impl;

import com.peswoc.hookclient.constant.State;
import com.peswoc.hookclient.dto.request.post.CreatePostRequestDto;
import com.peswoc.hookclient.dto.request.post.UpdatePostRequestDto;
import com.peswoc.hookclient.dto.response.post.PostListResponseDto;
import com.peswoc.hookclient.dto.response.post.PostResponseDto;
import com.peswoc.hookclient.dto.response.user.UserResponseDto;
import com.peswoc.hookclient.model.post.Post;
import com.peswoc.hookclient.repository.PostRepository;
import com.peswoc.hookclient.service.IAuthService;
import com.peswoc.hookclient.service.IPostService;
import com.peswoc.hookclient.util.ValidationUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class PostService implements IPostService {

  private final PostRepository postRepository;
  private final IAuthService authService;

  public PostService(PostRepository postRepository, IAuthService authService) {
    this.postRepository = postRepository;
    this.authService = authService;
  }

  @Override
  public PostResponseDto createPost(String title, String content) {
    Post post = new Post(title, content, authService.getAuthUser().getId());
    return new PostResponseDto(postRepository.save(post));
  }

  @Override
  public boolean validateCreateFields(CreatePostRequestDto request) {
    return request.getTitle() != null && request.getContent() != null;
  }

  @Override
  public boolean validateCreateFormat(CreatePostRequestDto request) {
    return ValidationUtils.isPostTitleValid(request.getTitle())
      && ValidationUtils.isPostContentValid(request.getContent());
  }

  @Override
  public boolean validateUpdateFields(UpdatePostRequestDto request) {
    return request.getTitle() != null && request.getContent() != null;
  }

  @Override
  public boolean validateUpdateFormat(UpdatePostRequestDto request) {
    return ValidationUtils.isPostTitleValid(request.getTitle())
      && ValidationUtils.isPostContentValid(request.getContent());
  }

  @Override
  public Post getPostById(String id) {
    return postRepository.findActiveById(id);
  }

  @Override
  public PostResponseDto updatePost(String id, String title, String content) {
    Post post = postRepository.findById(id).orElse(null);
    if (post != null) {
      post.setTitle(title);
      post.setContent(content);
      return new PostResponseDto(postRepository.save(post));
    } else {
      return null;
    }
  }

  @Override
  public void deletePost(String id) {
    Post post = postRepository.findById(id).orElse(null);
    if (post != null) {
      post.setState(State.INACTIVE);
      postRepository.save(post);
    }
  }

  @Override
  public List<Post> getAll() {
    return postRepository.findActivePost();
  }

  @Override
  public List<Post> searchPostByTitle(String title) {
    return postRepository.findByTitle(title);
  }

  @Override
  public List<Post> searchPostByUser(String userId) {
    return postRepository.findByUserId(userId);
  }

  @Override
  public List<Post> searchPostByTitleAndUser(String title, String userId) {
    return postRepository.findByTitleAndUserId(title, userId);
  }

  @Override
  public void syncPosts(PostListResponseDto data) {
    var posts = data.getPosts().stream()
      .map(PostResponseDto::toPost)
      .filter(Objects::nonNull)
      .toList();
    postRepository.saveAll(posts);
  }
}
