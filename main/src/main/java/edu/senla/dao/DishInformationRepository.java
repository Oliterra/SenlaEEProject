package edu.senla.dao;

import edu.senla.model.entity.DishInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DishInformationRepository extends JpaRepository<DishInformation, Long>, JpaSpecificationExecutor<DishInformation> {

}
