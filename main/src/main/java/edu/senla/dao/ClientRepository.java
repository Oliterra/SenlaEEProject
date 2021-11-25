package edu.senla.dao;

import edu.senla.dao.daointerface.ClientRepositoryInterface;
import edu.senla.entity.Client;
import edu.senla.entity.Client_;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityGraph;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.HashMap;
import java.util.Map;


@Repository
public class ClientRepository extends AbstractDAO<Client, Integer> implements ClientRepositoryInterface {

    public ClientRepository() {
        super(Client.class);
    }


    @Override
    public int getIdByEmail(String clientEmail) {
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Client> clientCriteriaQuery = criteriaBuilder.createQuery(Client.class);
        final Root<Client> clientRoot = clientCriteriaQuery.from(Client.class);
        return entityManager.createQuery(
                clientCriteriaQuery.select(clientRoot).where(criteriaBuilder.equal(clientRoot.get(Client_.email), clientEmail)))
                .getSingleResult().getId();
    }

    @Override
    public Client getByIdWithOrders(int clientId) {
        EntityGraph<?> graph = this.entityManager.getEntityGraph("client-entity-graph");
        Map<String, Object> hints = new HashMap<String, Object>();
        hints.put("javax.persistence.loadgraph", graph);
        return this.entityManager.find(Client.class, clientId, hints);
    }

    @Override
    public Client getByIdWithOrdersJPQL(int clientId) {
        return entityManager.createQuery("SELECT client FROM Client client LEFT JOIN FETCH client.orders WHERE client.id =:id", Client.class)
                .setParameter("id", clientId).getSingleResult();
    }

}
