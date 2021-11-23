package edu.senla.entity;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.sql.Timestamp;
import java.time.LocalDate;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Order.class)
public abstract class Order_ {

	public static volatile SingularAttribute<Order, LocalDate> date;
	public static volatile SingularAttribute<Order, Courier> courier;
	public static volatile SingularAttribute<Order, Client> client;
	public static volatile SingularAttribute<Order, Integer> id;
	public static volatile SingularAttribute<Order, Timestamp> time;
	public static volatile SingularAttribute<Order, String> paymentType;
	public static volatile SingularAttribute<Order, String> status;
	public static volatile ListAttribute<Order, TypeOfContainer> typesOfContainer;

	public static final String DATE = "date";
	public static final String COURIER = "courier";
	public static final String CLIENT = "client";
	public static final String ID = "id";
	public static final String TIME = "time";
	public static final String PAYMENT_TYPE = "paymentType";
	public static final String STATUS = "status";
	public static final String TYPES_OF_CONTAINER = "typesOfContainer";

}

