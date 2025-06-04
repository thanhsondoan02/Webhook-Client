package com.peswoc.hookclient.repository;

import com.peswoc.hookclient.model.openid.Server;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ServerRepository extends JpaRepository<Server, String> {
  @Query("SELECT COUNT(c) > 0 FROM Server c WHERE c.domain = :domain AND c.state = 1")
  boolean existsByDomain(@Param("domain") String domain);

  @Query("SELECT s FROM Server s WHERE s.domain = :domain AND s.state = 1")
  Optional<Server> findByDomain(@Param("domain") String domain);

  @Query("SELECT COUNT(c) > 0 FROM Server c WHERE c.owner = 1 AND c.state = 1")
  boolean existsOwnerServer();

  @Query("SELECT c FROM Server c WHERE c.owner = 1 AND c.state = 1")
  Optional<Server> findOwnerServer();
}
