package edu.senla.dao;

import edu.senla.entity.DishInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DishInformationRepositoryInterface extends JpaRepository<DishInformation, Long>{

}
