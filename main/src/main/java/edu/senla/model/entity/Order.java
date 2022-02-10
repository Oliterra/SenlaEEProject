package edu.senla.model.entity;

import edu.senla.model.enums.OrderPaymentType;
import edu.senla.model.enums.OrderStatus;
import edu.senla.model.enums.PostgreSQLEnumType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@TypeDef(
        name = "pgsql_enum",
        typeClass = PostgreSQLEnumType.class
)
@Table(name = "orders")
public class Order implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courier_id")
    private Courier courier;

    private LocalDate date;

    private LocalTime time;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type")
    @Type( type = "pgsql_enum" )
    private OrderPaymentType paymentType;

    @Enumerated(EnumType.STRING)
    @Type( type = "pgsql_enum" )
    private OrderStatus status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<Container> containers;

}
