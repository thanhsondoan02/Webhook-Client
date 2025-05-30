package com.peswoc.hookclient.repository;

import com.peswoc.hookclient.constant.State;
import com.peswoc.hookclient.model.user.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

  Optional<User> findByUsernameAndState(String username, State state);

  Optional<User> findByEmailAndState(String email, State state);

  boolean existsByUsername(String username);

  boolean existsByEmail(String email);

  @Query("SELECT u FROM User u WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
  List<User> searchByKeyword(@Param("keyword") String keyword);

  List<User> findAllByState(State state);

  @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.id = :id AND u.state = 1")
  boolean existsAndActiveById(@Param("id") String id);

  @Transactional
  @Modifying
  @Query("UPDATE User u SET u.state = :state WHERE u.id = :id")
  void updateState(@Param("id") String id, @Param("state") State state);
}
