package edu.senla.dao;

import edu.senla.entity.Courier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourierRepositoryInterface extends JpaRepository<Courier, Long>{

    public Courier getCourierByPhone(String phone);

}
