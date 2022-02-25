package edu.senla.dao.impl;

import edu.senla.dao.RoleRepository;
import edu.senla.exeption.UnexpectedInternalError;
import edu.senla.model.entity.Role;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.datasource.ConnectionHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@AllArgsConstructor
public class RoleRepositoryImpl implements RoleRepository {

    private final ConnectionHolder connectionHolder;

    @Override
    public List<Role> findAll() {
        List<Role> roles = new ArrayList<>();
        try (PreparedStatement preparedStatement = connectionHolder.getConnection().prepareStatement("SELECT * FROM roles")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Role role = new Role();
                role.setId(resultSet.getLong("id"));
                role.setName(resultSet.getString("name"));
                roles.add(role);
            }
        } catch (Exception e) {
            throw new UnexpectedInternalError("Sorry, an internal error has occurred. Try again later");
        }
        return roles;
    }

    @Override
    public void save(Role roleEntity) {
        try (PreparedStatement preparedStatement = connectionHolder.getConnection().prepareStatement("INSERT INTO roles(name) VALUES(?)")) {
            preparedStatement.setString(1, roleEntity.getName());
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public Role getById(long id) {
        Role role = null;
        try (PreparedStatement preparedStatement = connectionHolder.getConnection().prepareStatement("SELECT * FROM roles WHERE id=?")) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            role = new Role();
            role.setId(resultSet.getLong("id"));
            role.setName(resultSet.getString("name"));
        } catch (Exception e) {
            throw new UnexpectedInternalError("Sorry, an internal error has occurred. Try again later");
        }
        return role;
    }

    @Override
    public void update(long id, Role updatedRoleEntity) {
        try (PreparedStatement preparedStatement = connectionHolder.getConnection().prepareStatement("UPDATE roles SET name=? WHERE id=?")) {
            preparedStatement.setString(1, updatedRoleEntity.getName());
        } catch (Exception e) {
            throw new UnexpectedInternalError("Sorry, an internal error has occurred. Try again later");
        }
    }

    @Override
    public void deleteById(long id) {
        try (PreparedStatement preparedStatement = connectionHolder.getConnection().prepareStatement("DELETE FROM roles WHERE id=?")) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new UnexpectedInternalError("Sorry, an internal error has occurred. Try again later");
        }
    }

    @Override
    public Role getByName(String name) {
        Role role = null;
        try (PreparedStatement preparedStatement = connectionHolder.getConnection().prepareStatement("SELECT * FROM roles WHERE name=?")) {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            role = new Role();
            role.setId(resultSet.getLong("id"));
            role.setName(resultSet.getString("name"));
        } catch (Exception e) {
            throw new UnexpectedInternalError("Sorry, an internal error has occurred. Try again later");
        }
        return role;
    }
}
