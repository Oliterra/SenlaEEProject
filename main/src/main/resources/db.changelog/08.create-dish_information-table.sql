CREATE TABLE dish_information (
    id INTEGER NOT NULL UNIQUE,
    description VARCHAR(1000) NOT NULL,
    proteins DECIMAL NOT NULL,
    fats DECIMAL NOT NULL,
    carbohydrates DECIMAL NOT NULL,
    caloric_content DECIMAL NOT NULL,
    cooking_date DATE NOT NULL,
    expiration_date TIME NOT NULL,
    CONSTRAINT dishinformation_caloric_content_check CHECK (caloric_content < 1000),
    CONSTRAINT dishinformation_pkey PRIMARY KEY (id),
    CONSTRAINT dishinformation_id_fkey FOREIGN KEY (id) REFERENCES public.dishes(id)
);



