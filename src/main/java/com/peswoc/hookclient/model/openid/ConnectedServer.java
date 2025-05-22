package com.peswoc.hookclient.model.openid;

import com.peswoc.hookclient.model.base.BaseSqlEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "connected_servers")
@Getter
@Setter
@NoArgsConstructor
public class ConnectedServer extends BaseSqlEntity {

  @Column(name = "name", length = 100, nullable = false)
  private String name;

  @Column(name = "domain", nullable = false)
  private String domain;

  @Column(name = "callback_url", nullable = false)
  private String callbackUrl;

  @Column(name = "client_id", length = 32, nullable = false, unique = true)
  private String clientId;

  @Column(name = "client_secret", nullable = false)
  private String clientSecret;

  @OneToMany(mappedBy = "connectedServer", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Webhook> webhooks = new ArrayList<>();

  public ConnectedServer(String name, String domain, String callbackUrl, String clientId, String clientSecret) {
    super();
    this.name = name;
    this.domain = domain;
    this.callbackUrl = callbackUrl;
    this.clientId = clientId;
    this.clientSecret = clientSecret;
  }
}
