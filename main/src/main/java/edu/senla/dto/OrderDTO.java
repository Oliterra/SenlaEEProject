package edu.senla.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDTO {

    private int id;

    private String paymentType;

    private String status;

    private LocalDate date;

    private Timestamp time;

    private int client_id;

    private int courier_id;

}
