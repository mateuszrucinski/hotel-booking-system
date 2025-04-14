package pl.mati.hotel_booking_system.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.mati.hotel_booking_system.entity.HotelUser;

import java.util.Collection;
import java.util.Collections;

public class UserDetailsImpl implements UserDetails {

    private final HotelUser hotelUser;

    public UserDetailsImpl(HotelUser hotelUser) {
        this.hotelUser = hotelUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(
                //security config add automatically role prefix so in order to matching roles
                //has to be added role prefix here
                new SimpleGrantedAuthority("ROLE_" + hotelUser.getUserRole().name())
        );
    }

    @Override
    public String getPassword() {
        return hotelUser.getPassword();
    }

    @Override
    public String getUsername() {
        return hotelUser.getLogin();
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}