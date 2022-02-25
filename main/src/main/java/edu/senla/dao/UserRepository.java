package edu.senla.dao;

import edu.senla.model.entity.User;
import edu.senla.model.entity.Role;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User getByEmail(String email);

    User getByPhone(String phone);

    @Query("SELECT client FROM User client LEFT JOIN FETCH client.roles WHERE client.username =?1")
    User getByUsername(String username);

    List<User> getAllByRoles(Role role, Pageable pageable);
}

