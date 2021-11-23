package edu.senla.entity;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(TypeOfContainer.class)
public abstract class TypeOfContainer_ {

	public static volatile SingularAttribute<TypeOfContainer, Integer> numberOfCalories;
	public static volatile SingularAttribute<TypeOfContainer, Integer> price;
	public static volatile SingularAttribute<TypeOfContainer, String> name;
	public static volatile ListAttribute<TypeOfContainer, Dish> dishes;
	public static volatile ListAttribute<TypeOfContainer, Order> orders;

	public static final String NUMBER_OF_CALORIES = "numberOfCalories";
	public static final String PRICE = "price";
	public static final String NAME = "name";
	public static final String DISHES = "dishes";
	public static final String ORDERS = "orders";

}

