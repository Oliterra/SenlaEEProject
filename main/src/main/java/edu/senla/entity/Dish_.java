package edu.senla.entity;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Dish.class)
public abstract class Dish_ {

	public static volatile SingularAttribute<Dish, String> dishType;
	public static volatile SingularAttribute<Dish, DishInformation> dishInformation;
	public static volatile SingularAttribute<Dish, String> name;
	public static volatile SingularAttribute<Dish, Integer> id;
	public static volatile ListAttribute<Dish, TypeOfContainer> typesOfContainer;

	public static final String DISH_TYPE = "dishType";
	public static final String DISH_INFORMATION = "dishInformation";
	public static final String NAME = "name";
	public static final String ID = "id";
	public static final String TYPES_OF_CONTAINER = "typesOfContainer";

}

