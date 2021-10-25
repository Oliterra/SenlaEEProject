CREATE TABLE Orders (
    OrderId SERIAL,
    ClientId INTEGER NOT NULL,
    CourierId INTEGER NOT NULL,
    Date DATE NOT NULL,
    Time TIME NOT NULL,
    PaymentType VARCHAR(20) NOT NULL DEFAULT 'By cash',
    Status VARCHAR(20) NOT NULL DEFAULT 'New',
    CONSTRAINT orders_pkey PRIMARY KEY (OrderId),
    CONSTRAINT orders_clientid_fkey FOREIGN KEY (ClientId) REFERENCES Clients(ClientId),
    CONSTRAINT orders_courierid_fkey FOREIGN KEY (CourierId) REFERENCES Couriers(CourierId)
);

