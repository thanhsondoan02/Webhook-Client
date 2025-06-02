package com.peswoc.hookclient.model.base;

import com.peswoc.hookclient.model.openid.Server;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class MultiSourceEntity extends BaseSqlEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "from_server_id", nullable = false)
  protected Server fromServer;

  protected MultiSourceEntity() {
    super();
  }
}
