package edu.senla.dao;

import edu.senla.model.entity.ContainerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeOfContainerRepository extends JpaRepository<ContainerType, Long> {

    ContainerType getByName(String name);

    @Query("SELECT typeOfContainer.name FROM ContainerType typeOfContainer WHERE typeOfContainer.caloricContent =?1")
    String getNameById(long id);

    @Query("SELECT typeOfContainer.price FROM ContainerType typeOfContainer WHERE typeOfContainer.name =?1")
    double getPriceByName(String name);
}

