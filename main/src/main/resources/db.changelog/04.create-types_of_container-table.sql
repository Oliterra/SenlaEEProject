CREATE TABLE types_of_container (
    number_of_calories INTEGER NOT NULL,
    name VARCHAR(30) NOT NULL,
    price DECIMAL NOT NULL,
    CONSTRAINT typesofcontainer_pkey PRIMARY KEY (number_of_calories),
    CONSTRAINT typesofcontainer_numberofcalories_key UNIQUE (number_of_calories),
    CONSTRAINT typesofcontainer_price_key UNIQUE (price)
);
