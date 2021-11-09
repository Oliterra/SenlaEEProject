package edu.senla.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Order {

    private int id;

    private int clientId;

    private int courierId;

    private Date date;

    private Date time;

    private String paymentType;

    private String status;

}
