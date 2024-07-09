package com.project.psdlightinginformationservice.service;


import com.project.psdlightinginformationservice.calculation.LightingCalculation;
import com.project.psdlightinginformationservice.controller.dto.LightingInformationResponseDTO;
import com.project.psdlightinginformationservice.controller.dto.LuminaireSelectionResponseDTO;
import com.project.psdlightinginformationservice.entity.LightInformation;
import com.project.psdlightinginformationservice.entity.LuminaireSelection;
import com.project.psdlightinginformationservice.repository.LightingInformationRepository;
import com.project.psdlightinginformationservice.repository.LuminaireSelectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;


@Service
public class LightingInformationService {

    private final LightingInformationRepository lightingInformationRepository;
    private final LuminaireSelectionRepository luminaireSelectionRepository;

    @Autowired
    public LightingInformationService(LightingInformationRepository lightingInformationRepository,
                                      LuminaireSelectionRepository luminaireSelectionRepository) {
        this.lightingInformationRepository = lightingInformationRepository;
        this.luminaireSelectionRepository = luminaireSelectionRepository;
    }

    public LuminaireSelectionResponseDTO saveLuminaireSelectionInformation(short lightingInformationId, float heightProductionHall,
                                                                           float widthProductionHall, float lengthProductionHall) {
        LightingCalculation lightingCalculation = new LightingCalculation();
        LuminaireSelection luminaireSelection = lightingCalculation.lightingCalculation(lightingInformationId,
                heightProductionHall, widthProductionHall, lengthProductionHall, luminaireSelectionRepository);

        luminaireSelectionRepository.save(luminaireSelection);

        return lightingCalculation.createLuminaireSelectionResponse(luminaireSelectionRepository);
    }

    public LightingInformationResponseDTO saveLightingInformation(short lightingInformationId, String modelOfLuminaire, String modelOfLamp, float lightFluxOneLamp,
                                                                  short amountOfLampsInOneLuminaire, float activePowerOneLamp) {
        LightingCalculation lightingCalculation = new LightingCalculation();
        LuminaireSelection luminaireSelection = luminaireSelectionRepository.findById(lightingInformationId)
                .orElseThrow(() -> new NoSuchElementException("No value present"));

        LightInformation lightInformation = lightingCalculation.electricCalculation(luminaireSelection,
                lightingInformationRepository, lightingInformationId, modelOfLuminaire, modelOfLamp,
                lightFluxOneLamp, amountOfLampsInOneLuminaire, activePowerOneLamp);

        lightingInformationRepository.save(lightInformation);

        return getAllLightingInformation();
    }

    public LightingInformationResponseDTO updateLightingInformation(short lightingInformationId, String modelOfLuminaire, String modelOfLamp, float lightFluxOneLamp,
                                                                    short amountOfLampsInOneLuminaire, float activePowerOneLamp) {
        deleteLightingInformationById(lightingInformationId);
        return saveLightingInformation(lightingInformationId, modelOfLuminaire, modelOfLamp,
                lightFluxOneLamp, amountOfLampsInOneLuminaire, activePowerOneLamp);
    }

    public LuminaireSelectionResponseDTO updateLuminaireSelectionInformation(short lightingInformationId, float heightProductionHall,
                                                                             float widthProductionHall, float lengthProductionHall) {
        deleteLuminaireById(lightingInformationId);
        return saveLuminaireSelectionInformation(lightingInformationId, heightProductionHall, widthProductionHall, lengthProductionHall);
    }

    public LightingInformationResponseDTO deleteLightingInformationById(short lightingInformationId) {
        lightingInformationRepository.deleteById(lightingInformationId);
        return getAllLightingInformation();

    }

    public LuminaireSelectionResponseDTO deleteLuminaireById(short lightingInformationId) {
        luminaireSelectionRepository.deleteById(lightingInformationId);
        return getAllLuminaireSelectionInformation();

    }

    public LightingInformationResponseDTO getAllLightingInformation() {
        return new LightingInformationResponseDTO(lightingInformationRepository.findAll());
    }

    public LuminaireSelectionResponseDTO getAllLuminaireSelectionInformation() {
        LightingCalculation lightingCalculation = new LightingCalculation();
        return lightingCalculation.createLuminaireSelectionResponse(luminaireSelectionRepository);
    }







}
