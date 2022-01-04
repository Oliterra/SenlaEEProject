package edu.senla.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="dish_information")
public class DishInformation implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="dishInformation")
    private Dish dish;

    private String description;

    private double proteins;

    private double fats;

    private double carbohydrates;

    @Column(name = "caloric_content")
    private double caloricContent;

}
