package com.peswoc.hookclient.model.user;

import com.peswoc.hookclient.constant.Gender;
import com.peswoc.hookclient.constant.Role;
import com.peswoc.hookclient.model.base.MultiSourceEntity;
import com.peswoc.hookclient.util.convert.RoleConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User extends MultiSourceEntity {

  @NotNull
  @Column(name = "username", length = 50, nullable = false, unique = true)
  private String username;

  @NotNull
  @Column(name = "email", length = 100, nullable = false, unique = true)
  private String email;

  @Column(name = "password_hash")
  private String passwordHash;

  @Column(name = "full_name", length = 100)
  private String fullName;

  @Column(name = "gender", columnDefinition = "TINYINT")
  @Convert(converter = GenderConverter.class)
  private Gender gender;

  @Column(name = "role", columnDefinition = "TINYINT")
  @Convert(converter = RoleConverter.class)
  private Role role;

  @Column(name = "date_of_birth")
  private Long dateOfBirth;

  public User(String username, String email, String passwordHash, String fullName, Gender gender, Long dateOfBirth) {
    super();
    this.username = username;
    this.email = email;
    this.passwordHash = passwordHash;
    this.fullName = fullName;
    this.gender = gender;
    this.dateOfBirth = dateOfBirth;
  }
}
