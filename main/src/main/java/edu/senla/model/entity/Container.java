package edu.senla.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="containers")
public class Container {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "number_of_calories")
    private TypeOfContainer typeOfContainer;

    @Column(name = "meat_id")
    private int meat;

    @Column(name = "garnish_id")
    private int garnish;

    @Column(name = "salad_id")
    private int salad;

    @Column(name = "sauce_id")
    private int sauce;

}
