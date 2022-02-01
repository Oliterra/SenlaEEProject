CREATE TABLE dish_information (
    id SERIAL,
    dish_id INTEGER NOT NULL,
    description VARCHAR(1000) NOT NULL,
    proteins DECIMAL NOT NULL,
    fats DECIMAL NOT NULL,
    carbohydrates DECIMAL NOT NULL,
    caloric_content DECIMAL NOT NULL,
    CONSTRAINT dishinformation_caloric_content_check CHECK (caloric_content < 1000),
    CONSTRAINT dishinformation_pkey PRIMARY KEY (id),
    CONSTRAINT dishinformation_id_fkey FOREIGN KEY (dish_id) REFERENCES public.dishes(id) ON DELETE CASCADE
);



