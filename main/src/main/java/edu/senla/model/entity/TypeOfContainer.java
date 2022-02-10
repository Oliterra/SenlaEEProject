package edu.senla.model.entity;

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
    @Column(name = "number_of_calories")
    private long numberOfCalories;

    private String name;

    private int price;

    @OneToMany(mappedBy = "typeOfContainer", cascade = CascadeType.ALL)
    private List<Container> containers;

}
