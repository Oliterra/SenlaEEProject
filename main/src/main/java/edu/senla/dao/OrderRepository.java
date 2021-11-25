package edu.senla.dao;

import edu.senla.dao.daointerface.OrderRepositoryInterface;
import edu.senla.entity.*;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityGraph;
import javax.persistence.Subgraph;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public Order getByIdWithOrders(int orderId) {
        EntityGraph<?> graph = this.entityManager.getEntityGraph("order-entity-graph");
        Map<String, Object> hints = new HashMap<String, Object>();
        hints.put("javax.persistence.loadgraph", graph);
        return this.entityManager.find(Order.class, orderId, hints);
    }

}
