package edu.senla.entity;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Client.class)
public abstract class Client_ {

	public static volatile SingularAttribute<Client, String> firstName;
	public static volatile SingularAttribute<Client, String> lastName;
	public static volatile SingularAttribute<Client, String> address;
	public static volatile SingularAttribute<Client, String> phone;
	public static volatile ListAttribute<Client, Order> orders;
	public static volatile SingularAttribute<Client, Integer> id;
	public static volatile SingularAttribute<Client, String> email;

	public static final String FIRST_NAME = "firstName";
	public static final String LAST_NAME = "lastName";
	public static final String ADDRESS = "address";
	public static final String PHONE = "phone";
	public static final String ORDERS = "orders";
	public static final String ID = "id";
	public static final String EMAIL = "email";

}

