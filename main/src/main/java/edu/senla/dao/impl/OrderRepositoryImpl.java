package edu.senla.dao.impl;

import edu.senla.dao.OrderRepository;
import edu.senla.model.entity.Courier;
import edu.senla.model.entity.Order;
import edu.senla.model.entity.Order_;
import edu.senla.model.entity.User;
import edu.senla.model.enums.OrderPaymentType;
import edu.senla.model.enums.OrderStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.List;

@Repository
@AllArgsConstructor
@Transactional
public class OrderRepositoryImpl implements OrderRepository {

    private final EntityManager entityManager;

    @Override
    public List<Order> findAll() {
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Order> orderCriteriaQuery = criteriaBuilder.createQuery(Order.class);
        final Root<Order> orderRoot = orderCriteriaQuery.from(Order.class);
        return entityManager.createQuery(orderCriteriaQuery
                .select(orderRoot))
                .getResultList()
                .stream()
                .map(o -> {
                    OrderStatus orderStatus = OrderStatus.valueOf(o.getStatus().toString());
                    o.setStatus(orderStatus);
                    return o;
                })
                .map(o -> {
                    OrderPaymentType paymentType = OrderPaymentType.valueOf(o.getPaymentType().toString());
                    o.setPaymentType(paymentType);
                    return o;
                })
                .toList();
    }

    @Override
    public Order save(Order orderEntity) {
        entityManager.createNativeQuery("INSERT INTO orders(user_id, date, time, status, payment_type) VALUES (?,?,?,?,?)")
                .setParameter(1, orderEntity.getUser())
                .setParameter(2, orderEntity.getDate())
                .setParameter(3, orderEntity.getTime())
                .setParameter(4, orderEntity.getStatus())
                .setParameter(5, orderEntity.getPaymentType())
                .executeUpdate();
        return orderEntity;
    }

    @Override
    public Order getById(long id) {
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Order> orderCriteriaQuery = criteriaBuilder.createQuery(Order.class);
        final Root<Order> orderRoot = orderCriteriaQuery.from(Order.class);
        return entityManager.createQuery(orderCriteriaQuery
                .select(orderRoot)
                .where(criteriaBuilder.equal(orderRoot.get(Order_.id), id)))
                .getSingleResult();
    }

    @Override
    public void updateStatus(long id, OrderStatus status) {
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaUpdate<Order> orderUpdateCriteriaQuery = criteriaBuilder.createCriteriaUpdate(Order.class);
        final Root<Order> orderRoot = orderUpdateCriteriaQuery.from(Order.class);
        orderUpdateCriteriaQuery.set("status", status).where(criteriaBuilder.equal(orderRoot.get(Order_.id), id));
        entityManager.createQuery(orderUpdateCriteriaQuery).executeUpdate();
    }

    @Override
    public void updatePaymentType(long id, OrderPaymentType paymentType) {
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaUpdate<Order> orderUpdateCriteriaQuery = criteriaBuilder.createCriteriaUpdate(Order.class);
        final Root<Order> orderRoot = orderUpdateCriteriaQuery.from(Order.class);
        orderUpdateCriteriaQuery.set("paymentType", paymentType).where(criteriaBuilder.equal(orderRoot.get(Order_.id), id));
        entityManager.createQuery(orderUpdateCriteriaQuery).executeUpdate();
    }

    @Override
    public void deleteById(long id) {
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaDelete<Order> orderDeleteCriteriaQuery = criteriaBuilder.createCriteriaDelete(Order.class);
        final Root<Order> orderRoot = orderDeleteCriteriaQuery.from(Order.class);
        orderDeleteCriteriaQuery.where(criteriaBuilder.equal(orderRoot.get(Order_.id), id));
        entityManager.createQuery(orderDeleteCriteriaQuery).executeUpdate();
    }

    @Override
    public boolean existsById(long id) {
        try {
            getById(id);
        } catch (RuntimeException e) {
            return false;
        }
        return true;
    }

    @Override
    public Order getByCourierAndStatus(Courier courier, OrderStatus status) {
        return null;
    }

    @Override
    public List<Order> getAllByCourier(Courier courier) {
        return null;
    }

    @Override
    public List<Order> getAllByUser(User user) {
        return null;
    }

    @Override
    public List<Order> getByStatusOrderByTimeAsc(OrderStatus status) {
        return null;
    }

    @Override
    public List<Courier> getAllCouriersByStatus(OrderStatus status) {
        return null;
    }
}

