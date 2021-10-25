CREATE TABLE Orders_TypesOfContainer (
    OrderId INTEGER NOT NULL,
    NumberOfCalories INTEGER NOT NULL,
    NumberOfContainers INTEGER NOT NULL DEFAULT 1,
    CONSTRAINT orders_typesofcontainer_pkey PRIMARY KEY (OrderId, NumberOfCalories, NumberOfContainers),
    CONSTRAINT orders_typesofcontainer_numberofcalories_fkey FOREIGN KEY (NumberOfCalories) REFERENCES TypesOfContainer(NumberOfCalories),
    CONSTRAINT orders_typesofcontainer_orderid_fkey FOREIGN KEY (OrderId) REFERENCES Orders(OrderId)
);
