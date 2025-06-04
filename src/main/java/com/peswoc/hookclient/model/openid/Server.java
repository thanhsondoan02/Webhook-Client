package com.peswoc.hookclient.model.openid;

import com.peswoc.hookclient.constant.ServerOwner;
import com.peswoc.hookclient.model.base.BaseSqlEntity;
import com.peswoc.hookclient.util.convert.ServerOwnerConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "servers")
@Getter
@Setter
public class Server extends BaseSqlEntity {
  @Column(name = "domain", nullable = false, unique = true)
  protected String domain;

  @Column(name = "owner", nullable = false, columnDefinition = "TINYINT")
  @Convert(converter = ServerOwnerConverter.class)
  protected ServerOwner owner;

  public Server() {
    super();
    this.owner = ServerOwner.EXTERNAL;
  }

  public Server(String domain) {
    super();
    this.domain = domain;
    this.owner = ServerOwner.EXTERNAL;
  }
}
