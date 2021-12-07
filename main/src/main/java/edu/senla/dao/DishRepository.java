package edu.senla.dao;

import edu.senla.dao.daointerface.DishRepositoryInterface;
import edu.senla.entity.Client;
import edu.senla.entity.Dish;
import edu.senla.entity.Dish_;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityGraph;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.HashMap;
import java.util.Map;

@Repository
public class DishRepository extends AbstractDAO<Dish, Integer> implements DishRepositoryInterface {

    public DishRepository() {
        super(Dish.class);
    }

    @Override
    public Dish getDishByName(String dishName) {
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Dish> dishCriteriaQuery = criteriaBuilder.createQuery(Dish.class);
        final Root<Dish> dishRoot = dishCriteriaQuery.from(Dish.class);
        return entityManager.createQuery(
                dishCriteriaQuery.select(dishRoot).where(criteriaBuilder.equal(dishRoot.get(Dish_.name), dishName)))
                .getSingleResult();
    }

    @Override
    public Dish getByIdWithFullInformation(int dishId) {
        EntityGraph<?> graph = this.entityManager.getEntityGraph("dish-entity-graph");
        Map<String, Object> hints = new HashMap<String, Object>();
        hints.put("javax.persistence.loadgraph", graph);
        return this.entityManager.find(Dish.class, dishId, hints);
    }

}
