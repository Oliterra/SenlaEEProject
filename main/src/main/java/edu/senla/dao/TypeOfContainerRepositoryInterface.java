package edu.senla.dao;

import edu.senla.entity.TypeOfContainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeOfContainerRepositoryInterface extends JpaRepository<TypeOfContainer, Long> {

    public TypeOfContainer getByName(String name);

    @Query("SELECT typeOfContainer.name FROM TypeOfContainer typeOfContainer WHERE typeOfContainer.numberOfCalories =?1")
    public String getNameById(long id);

    @Query("SELECT typeOfContainer.price FROM TypeOfContainer typeOfContainer WHERE typeOfContainer.name =?1")
    public double getPriceByName(String name);

}

