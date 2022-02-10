package edu.senla.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ContainerComponentsNamesDTO {

    private String typeOfContainer;

    private String meat;

    private String garnish;

    private String salad;

    private String sauce;

}
