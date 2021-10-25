CREATE TABLE dishes (
    id SERIAL,
    dish_type VARCHAR(30) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    caloric_content INTEGER NOT NULL,
    CONSTRAINT dishes_pkey PRIMARY KEY (id),
    CONSTRAINT dishes_dish_type_key UNIQUE (dish_type),
    CONSTRAINT dishes_caloric_content_check CHECK ((caloric_content < 1000))
);
