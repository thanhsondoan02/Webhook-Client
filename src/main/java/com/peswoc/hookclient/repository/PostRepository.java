package com.peswoc.hookclient.repository;

import com.peswoc.hookclient.model.post.Post;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface PostRepository extends MongoRepository<Post, String> {
  @Query("{'state': 1}")
  List<Post> findActivePost();

  @Query("{'_id': ?0, 'state': 1}")
  Post findActiveById(String id);

  @Query("{'userId': ?0, 'state': 1}")
  List<Post> findByUserId(String userId);

  @Query("{'title': {$regex: ?0, $options: 'i'}, 'state': 1}")
  List<Post> findByTitle(String title);

  @Query("{'title': {$regex: ?0, $options: 'i'}, 'userId': ?1, 'state': 1}")
  List<Post> findByTitleAndUserId(String title, String userId);
}
