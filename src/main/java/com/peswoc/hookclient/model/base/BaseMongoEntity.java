package com.peswoc.hookclient.model.base;

import com.peswoc.hookclient.constant.State;
import jakarta.persistence.Convert;
import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public abstract class BaseMongoEntity implements Serializable {

  @Id
  @NotNull
  private String id;

  @Field("created_at")
  @NotNull
  private Instant createdAt;

  @Field("updated_at")
  @LastModifiedDate
  private Instant updatedAt;

  @Field("state")
  @Indexed
  @NotNull
  @Convert(converter = StateConverter.class)
  private State state;

  protected BaseMongoEntity() {
    id = UUID.randomUUID().toString().replace("-", "");
    createdAt = Instant.now();
    updatedAt = null;
    state = State.ACTIVE;
  }

  @PreUpdate
  public void preUpdate() {
    this.updatedAt = Instant.now();
  }
}
