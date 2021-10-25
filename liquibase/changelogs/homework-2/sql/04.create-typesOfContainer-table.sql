CREATE TABLE TypesOfContainer (
    NumberOfCalories INTEGER NOT NULL,
    Name VARCHAR(30) NOT NULL,
    Price INTEGER NOT NULL,
    CONSTRAINT typesofcontainer_pkey PRIMARY KEY (NumberOfCalories),
    CONSTRAINT typesofcontainer_numberofcalories_key UNIQUE (NumberOfCalories),
    CONSTRAINT typesofcontainer_price_key UNIQUE (Price)
);

