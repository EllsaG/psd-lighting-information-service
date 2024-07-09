package com.project.psdlightinginformationservice.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LightingInformationRequestDTO {
    @JsonProperty("lightingInformationId")
    private short lightingInformationId;
    @JsonProperty("modelOfLuminaire")
    private String modelOfLuminaire;
    @JsonProperty("modelOfLamp")
    private String modelOfLamp;
    @JsonProperty("amountOfLampsInOneLuminaire")
    private short amountOfLampsInOneLuminaire;
    @JsonProperty("lightFluxOneLamp")
    private float lightFluxOneLamp;
    @JsonProperty("activePowerOneLamp")
    private float activePowerOneLamp;
}
