package edu.senla.dao;

<<<<<<< Updated upstream
import edu.senla.model.entity.Client;
=======
>>>>>>> Stashed changes
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

    Order getByCourierAndStatus(Courier courier, OrderStatus status);

    List<Order> getAllByCourier(Courier courier);

<<<<<<< Updated upstream
    List<Order> getAllByClient(Client client, Pageable pageable);
=======
    List<Order> getAllByUser(User user);
>>>>>>> Stashed changes

    List<Order> getByStatusOrderByTimeAsc(OrderStatus status);

    List<Courier> getAllCouriersByStatus(OrderStatus status);
}
