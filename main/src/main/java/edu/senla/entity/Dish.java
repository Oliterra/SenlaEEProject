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
@Table(name="dishes")
public class Dish implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @Column(name = "dish_type")
    private String dishType;

    private String name;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dish_information_id")
    private DishInformation dishInformation;

    @ManyToMany(mappedBy = "dishes")
    private List<TypeOfContainer> typesOfContainer;

}
