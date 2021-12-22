package edu.senla.dao;

import edu.senla.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepositoryInterface extends JpaRepository<Role, Long>{

    public Role getRoleByName(String name);

}
