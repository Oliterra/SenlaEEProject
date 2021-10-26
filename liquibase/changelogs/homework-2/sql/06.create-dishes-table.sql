CREATE TABLE dishes (
    id SERIAL,
    dish_type VARCHAR(30) NOT NULL,
    name VARCHAR(100) NOT NULL,
    CONSTRAINT dishes_dish_type_key UNIQUE (dish_type),
    CONSTRAINT dishes_pkey PRIMARY KEY (id)
);
