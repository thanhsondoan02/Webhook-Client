package com.peswoc.hookclient.model.openid;

import com.peswoc.hookclient.model.base.BaseSqlEntity;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
public class BaseConnection extends BaseSqlEntity {

  @Column(name = "name", length = 100, nullable = false)
  protected String name;

  @Column(name = "domain", nullable = false)
  protected String domain;

  @Column(name = "callback_url", nullable = false)
  protected String callbackUrl;

  @Column(name = "target_domain", nullable = false)
  protected String targetDomain;

  @Column(name = "target_id", nullable = false)
  protected String targetId;
}
