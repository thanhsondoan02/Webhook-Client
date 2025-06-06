package com.peswoc.hookclient.model.openid;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pending_connections")
@Getter
@Setter
@NoArgsConstructor
public class PendingConnection extends BaseConnection {
}
