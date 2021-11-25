CREATE TABLE orders (
    id SERIAL,
    client_id INTEGER NOT NULL,
    courier_id INTEGER NOT NULL,
    date DATE NOT NULL,
    time TIME NOT NULL,
    payment_type VARCHAR(20) NOT NULL DEFAULT 'By cash',
    status VARCHAR(20) NOT NULL DEFAULT 'New',
    CONSTRAINT orders_pkey PRIMARY KEY (id),
    CONSTRAINT orders_clientid_fkey FOREIGN KEY (client_id) REFERENCES clients(id),
    CONSTRAINT orders_courierid_fkey FOREIGN KEY (courier_id) REFERENCES couriers(id)
);
