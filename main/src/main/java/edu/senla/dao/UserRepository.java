package edu.senla.dao;

import edu.senla.model.entity.Role;
import edu.senla.model.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository {

    List<User> findAll();

    User save(User userEntity);

    User getById(long id);

    void update(long id, User updatedUserEntity);

    void deleteById(long id);

    boolean existsById(long id);

    User getByEmail(String email);

    User getByPhone(String phone);

    User getByUsername(String username);

    List<User> getAllByRoles(Role role);
}

