package edu.senla.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ContainerComponentsDTO {

    private String typeOfContainer;

    private long meat;

    private long garnish;

    private long salad;

    private long sauce;

}
