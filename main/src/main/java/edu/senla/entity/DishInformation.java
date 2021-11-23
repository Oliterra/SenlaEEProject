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
public class DishInformation{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @OneToOne(fetch = FetchType.LAZY, mappedBy="dishInformation")
    private Dish dish;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "proteins", nullable = false)
    private int proteins;

    @Column(name = "fats", nullable = false)
    private int fats;

    @Column(name = "carbohydrates", nullable = false)
    private int carbohydrates;

    @Column(name = "caloric_content", nullable = false)
    private int caloricContent;

    @Column(name = "cooking_date", nullable = false)
    private LocalDate cookingDate;

    @Column(name = "expiration_date", nullable = false)
    private LocalDate expirationDate;

}
