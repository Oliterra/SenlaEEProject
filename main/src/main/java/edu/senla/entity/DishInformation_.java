package edu.senla.entity;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.time.LocalDate;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(DishInformation.class)
public abstract class DishInformation_ {

	public static volatile SingularAttribute<DishInformation, Integer> carbohydrates;
	public static volatile SingularAttribute<DishInformation, Integer> caloricContent;
	public static volatile SingularAttribute<DishInformation, Dish> dish;
	public static volatile SingularAttribute<DishInformation, Integer> fats;
	public static volatile SingularAttribute<DishInformation, Integer> proteins;
	public static volatile SingularAttribute<DishInformation, String> description;
	public static volatile SingularAttribute<DishInformation, LocalDate> cookingDate;
	public static volatile SingularAttribute<DishInformation, Integer> id;
	public static volatile SingularAttribute<DishInformation, LocalDate> expirationDate;

	public static final String CARBOHYDRATES = "carbohydrates";
	public static final String CALORIC_CONTENT = "caloricContent";
	public static final String DISH = "dish";
	public static final String FATS = "fats";
	public static final String PROTEINS = "proteins";
	public static final String DESCRIPTION = "description";
	public static final String COOKING_DATE = "cookingDate";
	public static final String ID = "id";
	public static final String EXPIRATION_DATE = "expirationDate";

}

