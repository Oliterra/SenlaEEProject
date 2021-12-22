package edu.senla.dao;

import edu.senla.entity.TypeOfContainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeOfContainerRepositoryInterface extends JpaRepository<TypeOfContainer, Long> {

    public TypeOfContainer getTypeOfContainerByCaloricContent(int caloricContent);

}

