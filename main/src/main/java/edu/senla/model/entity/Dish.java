package edu.senla.model.entity;

import edu.senla.model.enums.DishType;
import edu.senla.model.enums.PostgreSQLEnumType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@TypeDef(
        name = "pgsql_enum",
        typeClass = PostgreSQLEnumType.class
)
@Table(name = "dishes")
public class Dish implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "dish_type")
    @Type( type = "pgsql_enum" )
    private DishType dishType;

    private String name;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "dish")
    private DishInformation dishInformation;

}
