package com.jorgepozes04.barbershop_api.entities;

import com.jorgepozes04.barbershop_api.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
public class UserCredentials implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role == Role.ADMIN) {
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_BARBER"), new SimpleGrantedAuthority("ROLE_CLIENT"));
        } else if (this.role == Role.BARBER) {
            return List.of(new SimpleGrantedAuthority("ROLE_BARBER"));
        } else {
            return List.of(new SimpleGrantedAuthority("ROLE_CLIENT"));
        }
    }
}
