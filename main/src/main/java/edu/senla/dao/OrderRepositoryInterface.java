package edu.senla.dao;

import edu.senla.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepositoryInterface extends JpaRepository<Order, Long>{

}
