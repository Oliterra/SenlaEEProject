package edu.senla.dao;

import edu.senla.dao.daointerface.DishRepositoryInterface;
import edu.senla.entity.Dish;
import edu.senla.entity.Dish_;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Repository
public class DishRepository extends AbstractDAO<Dish, Integer> implements DishRepositoryInterface {

    public DishRepository() {
        super(Dish.class);
    }

    @Override
    public int getIdByName(String dishName) {
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Dish> dishCriteriaQuery = criteriaBuilder.createQuery(Dish.class);
        final Root<Dish> dishRoot = dishCriteriaQuery.from(Dish.class);
        return entityManager.createQuery(
                dishCriteriaQuery.select(dishRoot).where(criteriaBuilder.equal(dishRoot.get(Dish_.name), dishName)))
                .getSingleResult().getId();
    }

}
