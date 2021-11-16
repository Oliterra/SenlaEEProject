package edu.senla.dao;

import edu.senla.helper.ConnectionHolder;
import edu.senla.entity.Client;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Repository
@AllArgsConstructor
public class ClientDAO implements ClientJDBC{

    private final ConnectionHolder connectionHolder;

    @Override
    public void create(Client clientEntity) {
        try (PreparedStatement preparedStatement =  connectionHolder.getConnection().prepareStatement("INSERT INTO clients VALUES(?, ?, ?, ?, ?, ?)")){
            preparedStatement.setInt(1, clientEntity.getId());
            preparedStatement.setString(2, clientEntity.getFirstName());
            preparedStatement.setString(3, clientEntity.getLastName());
            preparedStatement.setString(4, clientEntity.getPhone());
            preparedStatement.setString(5, clientEntity.getEmail());
            preparedStatement.setString(6, clientEntity.getAddress());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Client read(int id) {
        Client client = null;
        try (PreparedStatement preparedStatement =  connectionHolder.getConnection().prepareStatement("SELECT * FROM clients WHERE id=?")){
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            client = new Client();
            client.setId(resultSet.getInt("id"));
            client.setFirstName(resultSet.getString("first_name"));
            client.setLastName(resultSet.getString("last_name"));
            client.setPhone(resultSet.getString("phone"));
            client.setEmail(resultSet.getString("email"));
            client.setAddress(resultSet.getString("address"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return client;
    }

    @Override
    public void update(int id, Client updatedClientEntity) {
        try (PreparedStatement preparedStatement =  connectionHolder.getConnection().prepareStatement("UPDATE clients SET first_name=?, last_name=?, phone=?, email=?, address=? WHERE id=?")){
            preparedStatement.setString(1, updatedClientEntity.getFirstName());
            preparedStatement.setString(2, updatedClientEntity.getLastName());
            preparedStatement.setString(3, updatedClientEntity.getPhone());
            preparedStatement.setString(4, updatedClientEntity.getEmail());
            preparedStatement.setString(5, updatedClientEntity.getAddress());
            preparedStatement.setInt(6, id);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int id) {
        try (PreparedStatement preparedStatement =  connectionHolder.getConnection().prepareStatement("DELETE FROM clients WHERE id=?")){
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
