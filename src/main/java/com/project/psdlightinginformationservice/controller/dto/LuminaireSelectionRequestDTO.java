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
public class LuminaireSelectionRequestDTO {
    @JsonProperty("lightingInformationId")
    private short lightingInformationId;
    @JsonProperty("heightProductionHall")
    private float heightProductionHall;
    @JsonProperty("widthProductionHall")
    private float widthProductionHall;
    @JsonProperty("lengthProductionHall")
    private float lengthProductionHall;
}
