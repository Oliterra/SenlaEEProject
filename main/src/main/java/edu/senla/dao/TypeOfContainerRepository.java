package edu.senla.dao;

import edu.senla.model.entity.TypeOfContainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeOfContainerRepository extends JpaRepository<TypeOfContainer, Long> {

    TypeOfContainer getByName(String name);

    @Query("SELECT typeOfContainer.name FROM TypeOfContainer typeOfContainer WHERE typeOfContainer.numberOfCalories =?1")
    String getNameById(long id);

    @Query("SELECT typeOfContainer.price FROM TypeOfContainer typeOfContainer WHERE typeOfContainer.name =?1")
    double getPriceByName(String name);
}

