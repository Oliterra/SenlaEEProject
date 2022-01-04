package edu.senla.dao;

import edu.senla.entity.Client;
import edu.senla.entity.Role;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepositoryInterface extends JpaRepository<Client, Long>{

    public Client getByEmail(String email);

    public Client getByPhone(String phone);

    @Query("SELECT client FROM Client client LEFT JOIN FETCH client.role WHERE client.username =?1")
    public Client getByUsername(String username);

    //@Query("SELECT client FROM Client client WHERE client.role =?1")
    public List<Client> getAllByRole (Role role, Pageable pageable);

}

