CREATE TABLE types_of_container_types_of_dish (
    number_of_calories INTEGER NOT NULL,
    dish_type VARCHAR(30) NOT NULL,
    CONSTRAINT typesofcontainer_typesofdish_pkey PRIMARY KEY (number_of_calories, dish_type),
    CONSTRAINT typesofcontainer_typesofdish_dishtype_fkey FOREIGN KEY (dish_type) REFERENCES dishes(dish_type),
    CONSTRAINT typesofcontainer_typesofdish_numberofcalories_fkey FOREIGN KEY (number_of_calories) REFERENCES types_of_container(number_of_calories)
);
