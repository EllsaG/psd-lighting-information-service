package com.project.psdlightinginformationservice.controller.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LuminaireSelectionResponseDTO {

    List<LightFluxAtAmountOfLamps> lightFluxAtAmountOfLampsList;

}
