package edu.senla.dao.daointerface;

public interface GenericDAO<Entity, Id> {

    public Entity create(Entity entity);

    public Entity read(Id entityId);

    public Entity update(Entity entity);

    public void delete(Id entityId);

}
