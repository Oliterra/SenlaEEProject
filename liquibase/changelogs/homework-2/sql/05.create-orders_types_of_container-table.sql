CREATE TABLE orders_types_of_container (
    order_id INTEGER NOT NULL,
    number_of_calories INTEGER NOT NULL,
    number_of_containers INTEGER NOT NULL DEFAULT 1,
    CONSTRAINT orders_typesofcontainer_pkey PRIMARY KEY (order_id, number_of_calories, number_of_containers),
    CONSTRAINT orders_typesofcontainer_numberofcalories_fkey FOREIGN KEY (number_of_calories) REFERENCES types_of_container(number_of_calories),
    CONSTRAINT orders_typesofcontainer_orderid_fkey FOREIGN KEY (order_id) REFERENCES orders(id)
);