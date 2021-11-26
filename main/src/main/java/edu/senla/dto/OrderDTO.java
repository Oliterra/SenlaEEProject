package edu.senla.dto;

import edu.senla.entity.Client;
import edu.senla.entity.Courier;
import edu.senla.entity.TypeOfContainer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDTO {

    private int id;

    private int client_id;

    private String paymentType;

    private String status;

    private LocalDate date;

}
