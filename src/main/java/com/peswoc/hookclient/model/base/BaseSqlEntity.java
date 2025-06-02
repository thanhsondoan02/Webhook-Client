package com.peswoc.hookclient.model.base;

import com.peswoc.hookclient.constant.State;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseSqlEntity implements Serializable {

  @Id
  @Column(name = "id", length = 32, nullable = false)
  protected String id;

  @Column(name = "created_at", nullable = false)
  protected Long createdAt;

  @Column(name = "updated_at")
  protected Long updatedAt;

  @Column(name = "state", nullable = false, columnDefinition = "TINYINT")
  @Convert(converter = StateConverter.class)
  protected State state;

  protected BaseSqlEntity() {
    id = UUID.randomUUID().toString().replace("-", "");
    createdAt = System.currentTimeMillis();
    updatedAt = null;
    state = State.ACTIVE;
  }

  @PreUpdate
  public void preUpdate() {
    this.updatedAt = System.currentTimeMillis();
  }
}
