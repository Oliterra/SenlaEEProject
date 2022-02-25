package edu.senla.dao;

import edu.senla.model.entity.Courier;
import edu.senla.model.enums.CourierStatus;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourierRepository {

    List<Courier> findAll();

    Courier save(Courier courierEntity);

    Courier getById(long id);

    void update(long id, Courier updatedCourierEntity);

    void deleteById(long id);

    boolean existsById(long id);

    Courier getByPhone(String phone);

    List<Courier> getByStatus(CourierStatus status);
}
