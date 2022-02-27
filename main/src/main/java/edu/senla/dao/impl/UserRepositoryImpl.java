package edu.senla.dao.impl;

import edu.senla.dao.UserRepository;
import edu.senla.model.entity.Role;
import edu.senla.model.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@AllArgsConstructor
@Transactional
public class UserRepositoryImpl implements UserRepository {

    private final EntityManager entityManager;

    @Override
    public List<User> findAll() {
        return entityManager.createNativeQuery("SELECT * FROM users", User.class).getResultList();
    }

    @Override
    public User save(User userEntity) {
        entityManager.createNativeQuery("INSERT INTO users(first_name, last_name, email, phone, username, password) VALUES (?,?,?,?,?,?)")
                .setParameter(1, userEntity.getFirstName())
                .setParameter(2, userEntity.getLastName())
                .setParameter(3, userEntity.getEmail())
                .setParameter(4, userEntity.getPhone())
                .setParameter(5, userEntity.getUsername())
                .setParameter(6, userEntity.getPassword())
                .executeUpdate();
        return userEntity;
    }

    @Override
    public User getById(long id) {
        return (User) entityManager.createNativeQuery("SELECT * FROM users WHERE id=?", User.class)
                .setParameter(1, id).getSingleResult();
    }

    @Override
    public void update(long id, User updatedUserEntity) {
        entityManager.createNativeQuery("UPDATE users SET first_name=?, last_name=?, email=?, phone=?, username=?, password=? WHERE id=?")
                .setParameter(1, updatedUserEntity.getFirstName())
                .setParameter(2, updatedUserEntity.getLastName())
                .setParameter(3, updatedUserEntity.getEmail())
                .setParameter(4, updatedUserEntity.getPhone())
                .setParameter(5, updatedUserEntity.getUsername())
                .setParameter(6, updatedUserEntity.getPassword())
                .setParameter(7, id)
                .executeUpdate();
    }

    @Override
    public void deleteById(long id) {
        entityManager.createNativeQuery("DELETE FROM users WHERE id=?")
                .setParameter(1, id)
                .executeUpdate();
    }

    @Override
    public boolean existsById(long id) {
        return ((Number) entityManager.createNativeQuery("SELECT count(*) FROM users WHERE id=?")
                .setParameter(1, id)
                .getSingleResult()).intValue() > 0;
    }

    @Override
    public User getByEmail(String email) {
        return (User) entityManager.createNativeQuery("SELECT * FROM users WHERE email=?", User.class)
                .setParameter(1, email)
                .getSingleResult();
    }

    @Override
    public User getByPhone(String phone) {
        return (User) entityManager.createNativeQuery("SELECT * FROM users WHERE phone=?", User.class)
                .setParameter(1, phone)
                .getSingleResult();
    }

    @Override
    public User getByUsername(String username) {
        return (User) entityManager.createNativeQuery("SELECT * FROM users WHERE username=?", User.class)
                .setParameter(1, username)
                .getSingleResult();
    }

    @Override
    public List<User> getAllByRoles(Role role) {
        return null;
    }
}

