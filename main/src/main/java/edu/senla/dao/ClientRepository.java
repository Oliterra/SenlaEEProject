package edu.senla.dao;

import edu.senla.dao.daointerface.ClientRepositoryInterface;
import edu.senla.entity.Client;
import edu.senla.entity.Client_;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;


@Repository
public class ClientRepository extends AbstractDAO<Client, Integer> implements ClientRepositoryInterface {

    public ClientRepository() {
        super(Client.class);
    }

    public int getIdByEmail(String clientEmail) {
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Client> clientCriteriaQuery = criteriaBuilder.createQuery(Client.class);
        final Root<Client> clientRoot = clientCriteriaQuery.from(Client.class);
        return entityManager.createQuery(
                clientCriteriaQuery.select(clientRoot).where(criteriaBuilder.equal(clientRoot.get(Client_.email), clientEmail)))
                .getSingleResult().getId();
    }
    
}
