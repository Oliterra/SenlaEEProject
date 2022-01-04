package edu.senla.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CourierPerformanceIndicatorDTO {

    private int totalNumberOfOrdersDelivered;

    private int numberOfOrdersDeliveredOnTime;

    private int numberOfOrdersDeliveredLate;

    private double percentageOfOrdersDeliveredOnTime;

    private double numberOfOrdersDeliveredPerDay;

    private boolean isSpeedOfWorkSatisfactory;

    private boolean isDailyAmountOfWorkSatisfactory;

}
