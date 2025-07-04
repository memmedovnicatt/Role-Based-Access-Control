package com.nicat.rolebasedaccesscontrol.dao.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import jakarta.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "tokens")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(length = 1000)
    String accessToken;

    @Column(length = 1000)
    String refreshToken;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
    Boolean isLoggedOut;

    @PostPersist
    public void setIsLoggedOut() {
        if (this.isLoggedOut == null) {
            this.isLoggedOut = Boolean.FALSE;
        }
    }
}