package edu.senla.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "orders")
@NamedEntityGraph(
        name = "order-entity-graph",
        attributeNodes = {@NamedAttributeNode(value = "typesOfContainer")}
)
public class Order implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courier_id")
    private Courier courier;

    private LocalDate date;

    @Column(name = "payment_type")
    private String paymentType;

    private String status;

    @OneToMany(mappedBy = "order")
    private List<OrderTypeOfContainer> typesOfContainer;

}
