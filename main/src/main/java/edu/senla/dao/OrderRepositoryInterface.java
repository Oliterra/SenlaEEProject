package edu.senla.dao;

import edu.senla.entity.Client;
import edu.senla.entity.Courier;
import edu.senla.entity.Order;
import edu.senla.enums.OrderStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepositoryInterface extends JpaRepository<Order, Long>{

    @Query("SELECT order FROM Order order WHERE order.courier =?1 AND order.status =?2")
    public Order getByCourierAndStatus(Courier courier, OrderStatus status);

    public List<Order> getAllByCourier(Courier courier, Pageable pageable);

    public List<Order> getAllByClient(Client client, Pageable pageable);

    public List<Order> getByStatusOrderByTimeAsc(OrderStatus status);

    @Query("SELECT order.courier FROM Order order WHERE order.status =?1")
    public List<Courier> getAllCouriersByStatus(OrderStatus status);

}
