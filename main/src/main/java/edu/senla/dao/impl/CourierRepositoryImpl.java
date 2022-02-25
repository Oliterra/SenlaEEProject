package edu.senla.dao.impl;

import edu.senla.dao.CourierRepository;
import edu.senla.model.entity.Courier;
import edu.senla.model.enums.CourierStatus;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@AllArgsConstructor
@Transactional
public class CourierRepositoryImpl implements CourierRepository {

    private final EntityManager entityManager;

    @Override
    public List<Courier> findAll() {
        return entityManager.createQuery("SELECT c FROM Courier c").getResultList();
    }

    @Override
    public Courier save(Courier courierEntity) {
        entityManager.createNativeQuery("INSERT INTO couriers(first_name, last_name, phone, status, password) VALUES (?,?,?,?,?)")
                .setParameter(1, courierEntity.getFirstName())
                .setParameter(2, courierEntity.getLastName())
                .setParameter(3, courierEntity.getPhone())
                .setParameter(4, courierEntity.getStatus())
                .setParameter(5, courierEntity.getPassword())
                .executeUpdate();
        return courierEntity;
    }

    @Override
    public Courier getById(long id) {
        return entityManager.createQuery("SELECT c FROM Courier c WHERE c.id=:id", Courier.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    @Override
    public void update(long id, Courier updatedCourierEntity) {
        entityManager.createNativeQuery("UPDATE couriers SET first_name=?, last_name=?, phone=?, password=? WHERE id=?")
                .setParameter(1, updatedCourierEntity.getFirstName())
                .setParameter(2, updatedCourierEntity.getLastName())
                .setParameter(3, updatedCourierEntity.getPhone())
                .setParameter(4, updatedCourierEntity.getPassword())
                .setParameter(5, id)
                .executeUpdate();
    }

    @Override
    @Modifying
    public void deleteById(long id) {
        entityManager.createNativeQuery("DELETE FROM couriers WHERE id=?")
                .setParameter(1, id)
                .executeUpdate();
    }

    @Override
    public boolean existsById(long id) {
        return ((Number) entityManager.createQuery("SELECT count(c) FROM Courier c WHERE c.id=:id")
                .setParameter("id", id)
                .getSingleResult()).intValue() > 0;
    }

    @Override
    public Courier getByPhone(String phone) {
        return entityManager.createQuery("SELECT c FROM Courier c WHERE c.phone=:phone", Courier.class)
                .setParameter("phone", phone)
                .getSingleResult();
    }

    @Override
    public List<Courier> getByStatus(CourierStatus status) {
        return entityManager.createQuery("SELECT c FROM Courier c WHERE c.status=:status", Courier.class)
                .setParameter("status", status)
                .getResultList();
    }
}
