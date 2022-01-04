package edu.senla.dao;

import edu.senla.entity.Container;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContainerRepositoryInterface extends JpaRepository<Container, Long> {

    List<Container> findAllByOrderId(long id);

}
