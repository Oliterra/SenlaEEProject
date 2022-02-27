package edu.senla.dao;

import edu.senla.model.entity.Courier;
import edu.senla.model.entity.Order;
import edu.senla.model.entity.User;
import edu.senla.model.enums.OrderPaymentType;
import edu.senla.model.enums.OrderStatus;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository {

    List<Order> findAll();

    Order save(Order orderEntity);

    Order getById(long id);

    void updateStatus(long id, OrderStatus status);

    void updatePaymentType(long id, OrderPaymentType paymentType);

    void deleteById(long id);

    boolean existsById(long id);

    List<Order> getAllByCourier(Courier courier);

    List<Order> getAllByUser(User user);

    List<Order> getByStatusOrderByTimeAsc(OrderStatus status);

    Order getByCourierAndStatus(Courier courier, OrderStatus status);

    List<Courier> getAllCouriersByStatus(OrderStatus status);
}
