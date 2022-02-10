package edu.senla.dao;

import edu.senla.model.entity.Courier;
import edu.senla.model.enums.CourierStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourierRepository extends JpaRepository<Courier, Long> {

    Courier getByPhone(String phone);

    List<Courier> getByStatus(CourierStatus status, Pageable pageable);
}
