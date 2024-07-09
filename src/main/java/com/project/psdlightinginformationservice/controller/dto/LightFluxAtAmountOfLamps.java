package com.project.psdlightinginformationservice.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LightFluxAtAmountOfLamps {
    @JsonProperty("lightingInformationId")
    short lightingInformationId;
    @JsonProperty("amountOfLampsInOneLuminaire")
    short amountOfLampsInOneLuminaire;
    @JsonProperty("minLightFluxForLuminaireSelection")
    float minLightFluxForLuminaireSelection;
    @JsonProperty("maxLightFluxForLuminaireSelection")
    float maxLightFluxForLuminaireSelection;
}
