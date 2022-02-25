package edu.senla.dao;

import edu.senla.model.entity.User;
import edu.senla.model.entity.Courier;
import edu.senla.model.entity.Order;
import edu.senla.model.enums.OrderStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT order FROM Order order WHERE order.courier =?1 AND order.status =?2")
    Order getByCourierAndStatus(Courier courier, OrderStatus status);

    List<Order> getAllByCourier(Courier courier, Pageable pageable);

    List<Order> getAllByUser(User user, Pageable pageable);

    List<Order> getByStatusOrderByTimeAsc(OrderStatus status);

    @Query("SELECT order.courier FROM Order order WHERE order.status =?1")
    List<Courier> getAllCouriersByStatus(OrderStatus status);
}
