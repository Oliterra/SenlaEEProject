package edu.senla.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="types_of_container")
public class TypeOfContainer implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "number_of_calories")
    private int numberOfCalories;

    private String name;

    private int price;

    @ManyToMany
    @JoinTable(name = "types_of_container_types_of_dish",
            joinColumns = @JoinColumn(name = "dish_id"),
            inverseJoinColumns = @JoinColumn(name = "types_of_container_id")
    )
    private List<Dish> dishes;

    @ManyToMany
    @JoinTable(name = "orders_types_of_container",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "types_of_container_id")
    )
    private List<Order> orders;

}
