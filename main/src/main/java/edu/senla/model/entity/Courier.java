package edu.senla.model.entity;

import edu.senla.model.enums.CourierStatus;
import edu.senla.model.enums.PostgreSQLEnumType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@TypeDef(
        name = "pgsql_enum",
        typeClass = PostgreSQLEnumType.class
)
@Table(name = "couriers")
public class Courier implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String phone;

    private String password;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "courier_status")
    @Type( type = "pgsql_enum" )
    private CourierStatus status;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "courier")
    private List<Order> orders;

}