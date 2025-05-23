package com.peswoc.hookclient.repository;

import com.peswoc.hookclient.constant.State;
import com.peswoc.hookclient.model.openid.PendingConnection;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PendingConnectionRepository extends JpaRepository<PendingConnection, String> {
  @Query("SELECT COUNT(c) > 0 FROM PendingConnection c WHERE c.id = :id AND c.state = 1")
  boolean existsAndActiveById(@Param("id") String id);

  @Transactional
  @Modifying
  @Query("UPDATE PendingConnection c SET c.state = :state WHERE c.id = :id")
  void updateState(@Param("id") String id, @Param("state") State state);

  Optional<PendingConnection> findByIdAndState(String id, State state);

  List<PendingConnection> findAllByState(@NotNull State state);
}
