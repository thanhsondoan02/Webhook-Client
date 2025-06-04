package com.peswoc.hookclient.repository;

import com.peswoc.hookclient.constant.ConnectionStatus;
import com.peswoc.hookclient.model.openid.Connection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ConnectionRepository extends JpaRepository<Connection, String> {

  @Query("SELECT c FROM Connection c WHERE c.id = :id AND c.status = :status AND c.state = 1")
  Optional<Connection> findActiveByIdAndStatus(@Param("id") String id, @Param("status") ConnectionStatus status);

  @Query("SELECT c FROM Connection c WHERE c.state = 1 AND c.status = :status")
  List<Connection> findActiveByStatus(@Param("status") ConnectionStatus status);
}
