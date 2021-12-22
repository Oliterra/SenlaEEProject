package edu.senla.dao;

import edu.senla.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepositoryInterface extends JpaRepository<Client, Long> {

    public Client getClientByEmail(String email);

    public Client getClientByUsername(String username);

}

