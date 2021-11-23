package edu.senla.dao;

import edu.senla.dao.daointerface.CourierRepositoryInterface;
import edu.senla.entity.Courier;
import edu.senla.entity.Courier_;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

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

}
