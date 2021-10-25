CREATE TABLE Dishes (
    DishID SERIAL,
    DishTypeID INTEGER NOT NULL,
    Name VARCHAR(30) NOT NULL,
    CaloricContent INTEGER NOT NULL,
    CONSTRAINT dishes_caloriccontent_check CHECK ((CaloricContent < 1000)),
    CONSTRAINT dishes_pkey PRIMARY KEY (DishID),
    CONSTRAINT dishes_dishtypeid_fkey FOREIGN KEY (DishTypeID) REFERENCES TypesOfDish(DishTypeID)
);
