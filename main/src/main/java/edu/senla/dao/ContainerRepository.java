package edu.senla.dao;

import edu.senla.model.entity.Container;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContainerRepository extends JpaRepository<Container, Long> {

    @EntityGraph
    List<Container> findAllByOrderId(long id);
}
