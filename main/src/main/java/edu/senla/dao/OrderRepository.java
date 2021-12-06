package edu.senla.dao;

import edu.senla.dao.daointerface.OrderRepositoryInterface;
import edu.senla.entity.Order;
import edu.senla.entity.Order_;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class OrderRepository extends AbstractDAO<Order, Integer> implements OrderRepositoryInterface {

    public OrderRepository() {
        super(Order.class);
    }

    @Override
    public List<Order> getAllClientsOrders(int clientId) {
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Order> orderCriteriaQuery = criteriaBuilder.createQuery(Order.class);
        final Root<Order> orderRoot = orderCriteriaQuery.from(Order.class);
        orderRoot.fetch(Order_.client, JoinType.LEFT);
        return entityManager.createQuery(
                orderCriteriaQuery.select(orderRoot).where(criteriaBuilder.equal(orderRoot.get(Order_.client), clientId)))
                .getResultList();

    }

    @Override
    public List<Order> getAllCouriersOrders(int courierId) {
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Order> orderCriteriaQuery = criteriaBuilder.createQuery(Order.class);
        final Root<Order> orderRoot = orderCriteriaQuery.from(Order.class);
        orderRoot.fetch(Order_.courier, JoinType.LEFT);
        return entityManager.createQuery(
                orderCriteriaQuery.select(orderRoot).where(criteriaBuilder.equal(orderRoot.get(Order_.courier), courierId)))
                .getResultList();
    }

}
