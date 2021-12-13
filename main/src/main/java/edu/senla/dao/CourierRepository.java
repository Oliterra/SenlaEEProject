package edu.senla.dao;

import edu.senla.dao.daointerface.CourierRepositoryInterface;
import edu.senla.entity.Courier;
import edu.senla.entity.Courier_;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityGraph;
import javax.persistence.NoResultException;
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
    public Courier getCourierByPhone(String courierPhone) throws NoResultException{
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Courier> courierCriteriaQuery = criteriaBuilder.createQuery(Courier.class);
        final Root<Courier> courierRoot = courierCriteriaQuery.from(Courier.class);
        return entityManager.createQuery(
                courierCriteriaQuery.select(courierRoot).where(criteriaBuilder.equal(courierRoot.get(Courier_.phone), courierPhone)))
                .getSingleResult();
    }

    @Override
    public Courier getByIdWithOrders(int courierId) throws NoResultException {
        EntityGraph<?> graph = this.entityManager.getEntityGraph("courier-entity-graph");
        Map<String, Object> hints = new HashMap<String, Object>();
        hints.put("javax.persistence.loadgraph", graph);
        return this.entityManager.find(Courier.class, courierId, hints);
    }

}
