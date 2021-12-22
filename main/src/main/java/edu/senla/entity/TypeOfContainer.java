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
    private long numberOfCalories;

    private String name;

    private int price;

    @ManyToMany
    @JoinTable(name = "types_of_container_types_of_dish",
            joinColumns = @JoinColumn(name = "number_of_caloriesdish_type"),
            inverseJoinColumns = @JoinColumn(name = "dish_type")
    )
    private List<Dish> dishes;

    @OneToMany(mappedBy = "typeOfContainer")
    private List<OrderTypeOfContainer> orders;

}
