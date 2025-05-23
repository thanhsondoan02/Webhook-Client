package com.peswoc.hookclient.model.openid;

import com.peswoc.hookclient.constant.HookEvent;
import com.peswoc.hookclient.constant.HookScope;
import com.peswoc.hookclient.model.base.BaseSqlEntity;
import com.peswoc.hookclient.util.convert.HookEventConverter;
import com.peswoc.hookclient.util.convert.HookScopeConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
  name = "webhooks",
  indexes = {
    @Index(name = "idx_webhook_server_id", columnList = "server_id")
  },
  uniqueConstraints = {
    @UniqueConstraint(columnNames = {"server_id", "event"})
  }
)
@Getter
@Setter
@NoArgsConstructor
public class Webhook extends BaseSqlEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "server_id", nullable = false)
  private AcceptedConnection connection;

  @Column(name = "event", columnDefinition = "TINYINT", nullable = false)
  @Convert(converter = HookEventConverter.class)
  private HookEvent event;

  @Column(name = "scope", columnDefinition = "TINYINT", nullable = false)
  @Convert(converter = HookScopeConverter.class)
  private HookScope scope;

  @Column(name = "redirect_url", nullable = false)
  private String redirectUrl;

  public Webhook(AcceptedConnection connection, HookEvent event, HookScope scope, String redirectUrl) {
    super();
    this.connection = connection;
    this.event = event;
    this.scope = scope;
    this.redirectUrl = redirectUrl;
  }
}
