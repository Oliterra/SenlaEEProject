package edu.senla.dao;

import edu.senla.dao.daointerface.RoleRepositoryInterface;
import edu.senla.entity.Role;
import edu.senla.entity.Role_;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Repository
public class RoleRepository extends AbstractDAO<Role, Integer> implements RoleRepositoryInterface {

    public RoleRepository() {
        super(Role.class);
    }

    @Override
    public Role getRoleByName(String roleName) throws NoResultException {
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Role> roleCriteriaQuery = criteriaBuilder.createQuery(Role.class);
        final Root<Role> roleRoot = roleCriteriaQuery.from(Role.class);
        return entityManager.createQuery(
                roleCriteriaQuery.select(roleRoot).where(criteriaBuilder.equal(roleRoot.get(Role_.name), roleName)))
                .getSingleResult();
    }

}
