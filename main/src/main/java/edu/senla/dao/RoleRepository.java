package edu.senla.dao;

import edu.senla.model.entity.Role;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository {

    List<Role> findAll();

    void save(Role roleEntity);

    Role getById(long id);

    void update(long id, Role updatedRoleEntity);

    void deleteById(long id);

    Role getByName(String name);
}
