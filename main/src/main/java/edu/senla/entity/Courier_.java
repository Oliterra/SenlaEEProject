package edu.senla.entity;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Courier.class)
public abstract class Courier_ {

	public static volatile SingularAttribute<Courier, String> firstName;
	public static volatile SingularAttribute<Courier, String> lastName;
	public static volatile SingularAttribute<Courier, String> phone;
	public static volatile ListAttribute<Courier, Order> orders;
	public static volatile SingularAttribute<Courier, Integer> id;

	public static final String FIRST_NAME = "firstName";
	public static final String LAST_NAME = "lastName";
	public static final String PHONE = "phone";
	public static final String ORDERS = "orders";
	public static final String ID = "id";

}

