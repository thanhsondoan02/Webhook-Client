package com.peswoc.hookclient.repository;

import com.peswoc.hookclient.constant.State;
import com.peswoc.hookclient.model.openid.AcceptedConnection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AcceptedConnectionRepository extends JpaRepository<AcceptedConnection, String> {

  List<AcceptedConnection> findAllByState(State state);
}
