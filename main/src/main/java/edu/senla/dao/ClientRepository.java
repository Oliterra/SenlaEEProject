package edu.senla.dao;

import edu.senla.model.entity.Client;
import edu.senla.model.entity.Role;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Client getByEmail(String email);

    Client getByPhone(String phone);

    @Query("SELECT client FROM Client client LEFT JOIN FETCH client.role WHERE client.username =?1")
    Client getByUsername(String username);

    List<Client> getAllByRole(Role role, Pageable pageable);
}

