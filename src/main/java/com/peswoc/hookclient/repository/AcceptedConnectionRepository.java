package com.peswoc.hookclient.repository;

import com.peswoc.hookclient.constant.State;
import com.peswoc.hookclient.model.openid.AcceptedConnection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AcceptedConnectionRepository extends JpaRepository<AcceptedConnection, String> {

  List<AcceptedConnection> findAllByState(State state);

  @Query("SELECT c FROM AcceptedConnection c WHERE c.id = :id AND c.state = 1")
  Optional<AcceptedConnection> getActiveById(@Param("id") String id);

  @Query("SELECT c FROM AcceptedConnection c WHERE c.targetId = :id AND c.state = 1")
  Optional<AcceptedConnection> getActiveByTargetId(@Param("id") String id);
}
