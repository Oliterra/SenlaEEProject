CREATE TABLE containers (
    id SERIAL,
    order_id INTEGER NOT NULL,
    number_of_calories INTEGER NOT NULL,
    meat_id INTEGER NOT NULL,
    garnish_id INTEGER NOT NULL,
    salad_id INTEGER NOT NULL,
    sauce_id INTEGER NOT NULL,
    CONSTRAINT containers_pkey PRIMARY KEY (id),
    CONSTRAINT containers_numberofcalories_fkey FOREIGN KEY (number_of_calories) REFERENCES types_of_container(number_of_calories),
    CONSTRAINT containers_orderid_fkey FOREIGN KEY (order_id) REFERENCES orders(id)
);