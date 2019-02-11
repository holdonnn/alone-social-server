package com.freestudy.api.interest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.freestudy.api.user.User;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(
        indexes = {
                @Index(name = "idx_value", columnList = "value", unique = true)
        }
)
@EqualsAndHashCode(of = "value")
public class Interest {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String value;

  @ManyToMany(mappedBy = "interests")
  @JsonIgnore
  private Set<User> users;

  public Interest(String value) {
    this.value = value;
    this.users = new HashSet<>();
  }

}
