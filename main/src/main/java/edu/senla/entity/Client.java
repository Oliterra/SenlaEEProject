package edu.senla.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="clients")
@NamedEntityGraph(
        name = "client-entity-graph",
        attributeNodes = {@NamedAttributeNode(value = "orders")}
)
public class Client implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String phone;

    private String email;

    private String address;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "client")
    private List<Order> orders;

}
