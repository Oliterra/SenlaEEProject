CREATE TABLE TypesOfContainer_TypesOfDish (
    NumberOfCalories INTEGER NOT NULL,
    DishTypeID INTEGER NOT NULL,
    CONSTRAINT typesofcontainer_typesofdish_pkey PRIMARY KEY (NumberOfCalories, DishTypeID),
    CONSTRAINT typesofcontainer_typesofdish_dishtypeid_fkey FOREIGN KEY (DishTypeID) REFERENCES TypesOfDish(DishTypeID),
    CONSTRAINT typesofcontainer_typesofdish_numberofcalories_fkey FOREIGN KEY (NumberOfCalories) REFERENCES TypesOfContainer(NumberOfCalories)
);

