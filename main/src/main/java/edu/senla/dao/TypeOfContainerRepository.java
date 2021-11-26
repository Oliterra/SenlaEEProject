package edu.senla.dao;

import edu.senla.dao.daointerface.TypeOfContainerRepositoryInterface;
import edu.senla.entity.TypeOfContainer;
import edu.senla.entity.TypeOfContainer_;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class TypeOfContainerRepository extends AbstractDAO<TypeOfContainer, Integer> implements TypeOfContainerRepositoryInterface {

    public TypeOfContainerRepository() {
        super(TypeOfContainer.class);
    }

    @Override
    public List<TypeOfContainer> getAllContainersByCaloricContent(int caloricContent) {
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<TypeOfContainer> typeOfContainerCriteriaQuery = criteriaBuilder.createQuery(TypeOfContainer.class);
        final Root<TypeOfContainer> typeOfContainerRoot = typeOfContainerCriteriaQuery.from(TypeOfContainer.class);
        return entityManager.createQuery(
                typeOfContainerCriteriaQuery.select(typeOfContainerRoot).where(criteriaBuilder.equal(typeOfContainerRoot.get(TypeOfContainer_.NUMBER_OF_CALORIES), caloricContent)))
                .getResultList();
    }

}

