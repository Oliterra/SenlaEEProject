package edu.senla.dao;

import edu.senla.dao.daointerface.CourierRepositoryInterface;
import edu.senla.dto.ClientDTO;
import edu.senla.entity.Client;
import edu.senla.entity.Courier;
import edu.senla.entity.Courier_;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityGraph;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.HashMap;
import java.util.Map;

@Repository
public class CourierRepository extends AbstractDAO<Courier, Integer> implements CourierRepositoryInterface {

    public CourierRepository() {
        super(Courier.class);
    }

    @Override
    public int getIdByPhone(String courierPhone) {
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Courier> courierCriteriaQuery = criteriaBuilder.createQuery(Courier.class);
        final Root<Courier> courierRoot = courierCriteriaQuery.from(Courier.class);
        return entityManager.createQuery(
                courierCriteriaQuery.select(courierRoot).where(criteriaBuilder.equal(courierRoot.get(Courier_.phone), courierPhone)))
                .getSingleResult().getId();
    }

    @Override
    public Courier getByIdWithOrders(int courierId) {
        EntityGraph<?> graph = this.entityManager.getEntityGraph("courier-entity-graph");
        Map<String, Object> hints = new HashMap<String, Object>();
        hints.put("javax.persistence.loadgraph", graph);
        return this.entityManager.find(Courier.class, courierId, hints);
    }

    @Override
    public Courier getByIdWithOrdersJPQL(int courierId) {
        return entityManager.createQuery("SELECT courier FROM Courier courier LEFt JOIN FETCH courier.orders WHERE courier.id =:id", Courier.class)
                .setParameter("id", courierId).getSingleResult();
    }

}
