package edu.senla.dao;

public interface DAO<T> {

    public void create(T entity);

    public T read(int id);

    public void update(T updatedEntity);

    public void delete(int id);

}
