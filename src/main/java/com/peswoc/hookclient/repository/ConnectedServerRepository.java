package com.peswoc.hookclient.repository;

import com.peswoc.hookclient.model.openid.ConnectedServer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConnectedServerRepository extends JpaRepository<ConnectedServer, String> {

}
