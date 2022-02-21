package edu.senla.model.entity;

import edu.senla.model.enums.DishType;
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
@Table(name = "dishes")
public class Dish implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private DishType type;

    @Column(name = "name")
    private String name;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "dish")
    private DishInformation dishInformation;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "dishes")
    private List<Container> containers;

}
