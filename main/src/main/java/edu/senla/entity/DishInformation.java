package edu.senla.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="dish_information")
public class DishInformation implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL,  mappedBy="dishInformation")
    @PrimaryKeyJoinColumn
    private Dish dish;

    private String description;

    private int proteins;

    private int fats;

    private int carbohydrates;

    @Column(name = "caloric_content")
    private int caloricContent;

    @Column(name = "cooking_date")
    private LocalDate cookingDate;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

}
