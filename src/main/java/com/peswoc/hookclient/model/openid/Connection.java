package com.peswoc.hookclient.model.openid;

import com.peswoc.hookclient.constant.ConnectionStatus;
import com.peswoc.hookclient.model.base.BaseSqlEntity;
import com.peswoc.hookclient.util.convert.ConnectionStatusConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "connections")
@Getter
@Setter
@NoArgsConstructor
public class Connection extends BaseSqlEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "target_server_id", nullable = false)
  private Server targetServer;

  @Column(name = "name", length = 100, nullable = false)
  protected String name;

  @Column(name = "callback_url", nullable = false)
  protected String callbackUrl;

  @Column(name = "client_id", length = 32, unique = true)
  private String clientId;

  @Column(name = "client_secret")
  private String clientSecret;

  @Column(name = "status", nullable = false, columnDefinition = "TINYINT")
  @Convert(converter = ConnectionStatusConverter.class)
  protected ConnectionStatus status;
}
