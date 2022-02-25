package edu.senla.security;

import edu.senla.model.entity.Courier;
import edu.senla.model.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class UserDetailsImpl implements UserDetails {

    private String username;
    private String password;

    private Collection<? extends GrantedAuthority> grantedAuthorities;

    public static UserDetailsImpl fromClientEntityToClientUserDetails(User user) {
        UserDetailsImpl userDetailsImpl = new UserDetailsImpl();
        userDetailsImpl.username = user.getUsername();
        userDetailsImpl.password = user.getPassword();
        userDetailsImpl.grantedAuthorities = List.copyOf(user.getRoles()).stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
        return userDetailsImpl;
    }

    public static UserDetailsImpl fromCourierEntityToCourierUserDetails(Courier courier) {
        UserDetailsImpl courierUserDetailsImpl = new UserDetailsImpl();
        courierUserDetailsImpl.username = courier.getPhone();
        courierUserDetailsImpl.password = courier.getPassword();
        courierUserDetailsImpl.grantedAuthorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_COURIER"));
        return courierUserDetailsImpl;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
