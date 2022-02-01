package edu.senla.dao;

import edu.senla.entity.Courier;
import edu.senla.enums.CourierStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourierRepositoryInterface extends JpaRepository<Courier, Long>{

    public Courier getByPhone(String phone);

    public List<Courier> getByStatus(CourierStatus status, Pageable pageable);

}
