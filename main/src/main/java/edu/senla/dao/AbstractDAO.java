package edu.senla.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class AbstractDAO<Entity, Id>{

    private Class<Entity> entityClass;

    @PersistenceContext
    protected EntityManager entityManager;

    public AbstractDAO(Class<Entity> entityClass){
        this.entityClass = entityClass;
    }

    public Entity create(Entity entity) {
        entityManager.persist(entity);
        return entity;
    }

    public Entity read(Id entityId) {
        return entityManager.find(entityClass, entityId);
    }

    public Entity update(Entity entity) {
        return entityManager.merge(entity);
    }

    public void delete(Id entityId) {
        final Entity entity = read(entityId);
        entityManager.remove(entity);
    }

}


