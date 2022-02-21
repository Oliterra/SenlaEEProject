package edu.senla.service.impl;

import edu.senla.dao.UserRepository;
import edu.senla.dao.CourierRepository;
import edu.senla.model.entity.User;
import edu.senla.model.entity.Courier;
import edu.senla.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientUserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final CourierRepository courierRepository;

    @Override
    public UserDetailsImpl loadUserByUsername(String username) {
        if (username.startsWith("+375")) {
            Courier courier = courierRepository.getByPhone(username);
            return UserDetailsImpl.fromCourierEntityToCourierUserDetails(courier);
        } else {
            User user = userRepository.getByUsername(username);
            return UserDetailsImpl.fromClientEntityToClientUserDetails(user);
        }
    }
}
