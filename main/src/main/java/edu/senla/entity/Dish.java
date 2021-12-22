package edu.senla.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "dishes")
@NamedEntityGraph(
        name = "dish-entity-graph",
        attributeNodes = {@NamedAttributeNode(value = "dishInformation")}
)
public class Dish implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(name = "dish_type")
    private String dishType;

    private String name;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
    @JoinColumn(name = "dish_information_id")
    private DishInformation dishInformation;

    @ManyToMany(mappedBy = "dishes")
    private List<TypeOfContainer> typesOfContainer;

}
