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
@Table(name="container_types")
public class ContainerType implements Serializable{

    @Id
    @Column(name = "caloric_content")
    private long caloricContent;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private int price;

    @OneToMany(mappedBy = "containerType", cascade = CascadeType.ALL)
    private List<Container> containers;

}
