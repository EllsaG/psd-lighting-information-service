package com.project.psdlightinginformationservice.calculation;





import com.project.psdlightinginformationservice.controller.dto.LightFluxAtAmountOfLamps;
import com.project.psdlightinginformationservice.controller.dto.LuminaireSelectionResponseDTO;
import com.project.psdlightinginformationservice.entity.LightInformation;
import com.project.psdlightinginformationservice.entity.LuminaireSelection;
import com.project.psdlightinginformationservice.exceptions.InformationAlreadyExistsException;
import com.project.psdlightinginformationservice.repository.LightingInformationRepository;
import com.project.psdlightinginformationservice.repository.LuminaireSelectionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LightingCalculation {


    public LuminaireSelection lightingCalculation(short lightingId, float productionHallHeight,
                                                  float productionHallWidth, float productionHallLength,
                                                  LuminaireSelectionRepository luminaireSelectionRepository) {

        Optional<LuminaireSelection> byId = luminaireSelectionRepository.findById(lightingId);
        if (byId.isPresent()) {
            throw new InformationAlreadyExistsException("Information about production hall with id № " + lightingId + " is already exists");
        }

        List<LuminaireSelection> all = luminaireSelectionRepository.findAll();

        for (LuminaireSelection luminaireSelection : all) {
            if (luminaireSelection.getProductionHallHeight() == productionHallHeight &&
                    luminaireSelection.getProductionHallWidth() == productionHallWidth &&
                    luminaireSelection.getProductionHallLength() == productionHallLength) {
                throw new InformationAlreadyExistsException("Information about production hall with height: " + productionHallHeight +
                        ", width: " + productionHallWidth +
                        " and length: " + productionHallLength +
                        " is already exists");
            }
        }

        final float heightOfWorkSurface = 0.8F;

        final short ratedLight = 300;
        final float safetyFactor = 1.5F;
        final float coefOfLightingIrregularity = 1.15F;
        final float coefEfficiencyOfLuminaire = 0.8F;

        float heightLampUnderCeiling = 1.2F; // maybe between 0 and 2.5

        float coef = 1.0F; // coefficient of the relative ratio of suspension height and distance between luminaires (maybe between 0.8 and 1.5)

        float heightOverWorkSurface = Math.round(productionHallHeight - heightOfWorkSurface - heightLampUnderCeiling) / 100F;

        float distanceBetweenLampRows = heightOverWorkSurface * coef * 100;

        float distanceBetweenWallAndFirstRowOfLamps = Math.round(0.25 * distanceBetweenLampRows * 10) / 10F; // coef "0.25" maybe between 0.25 and 0.3

        short amountLuminairesPerLength = (short) (Math.floor((productionHallLength -
                        2 * distanceBetweenWallAndFirstRowOfLamps) / distanceBetweenLampRows) + 1);

        short amountLuminairesPerWidth = (short) (Math.floor((productionHallWidth -
                        2 * distanceBetweenWallAndFirstRowOfLamps) / distanceBetweenLampRows) + 1);

        float lightFlux = (float) Math.ceil((ratedLight * productionHallLength * productionHallWidth * safetyFactor * coefOfLightingIrregularity) /
                (1 * amountLuminairesPerLength * amountLuminairesPerWidth * coefEfficiencyOfLuminaire));

        return new LuminaireSelection(lightingId, distanceBetweenLampRows, distanceBetweenWallAndFirstRowOfLamps,
                amountLuminairesPerLength, amountLuminairesPerWidth, lightFlux, productionHallHeight, productionHallWidth, productionHallLength);

    }

    public LuminaireSelectionResponseDTO createLuminaireSelectionResponse(LuminaireSelectionRepository luminaireSelectionRepository) {

        List<LuminaireSelection> all = luminaireSelectionRepository.findAll();
        if (all.isEmpty()){
            return new LuminaireSelectionResponseDTO();
        }
        LuminaireSelectionResponseDTO luminaireSelectionResponseDTO =
                new LuminaireSelectionResponseDTO();// min and max light flux at 1, 2, 3 and 4 lamps in the Luminaire
        List<LightFluxAtAmountOfLamps> lightFluxAtAmountOfLamps = new ArrayList<>();

        for (LuminaireSelection f: all) {
            double lightFlux = f.getLightFlux();
            double minLightFluxForChooseLuminaire = Math.ceil(lightFlux * 1.4);
            double maxLightFluxForChooseLuminaire = Math.ceil(lightFlux * 1.6);

            for (short amountOfLamps = 1; amountOfLamps < 5; amountOfLamps++) {
                lightFluxAtAmountOfLamps.add(new LightFluxAtAmountOfLamps(f.getLuminaireSelectionId(),
                        amountOfLamps,
                        Math.round(minLightFluxForChooseLuminaire/amountOfLamps*10)/10.0F,
                        Math.round(maxLightFluxForChooseLuminaire/amountOfLamps*10)/10.0F));
            }
            luminaireSelectionResponseDTO.setLightFluxAtAmountOfLampsList(lightFluxAtAmountOfLamps);
        }

        return luminaireSelectionResponseDTO;
    }


    public LightInformation electricCalculation(LuminaireSelection luminaireSelection,
                                                LightingInformationRepository lightingInformationRepository, short lightingId, String modelOfLuminaire,
                                                String modelOfLamp, float lightFluxOneLamp, short amountOfLampsInOneLuminaire, float activePowerOneLamp) {

        Optional<LightInformation> byId = lightingInformationRepository.findById(lightingId);

        if (byId.isPresent()) {
            throw new InformationAlreadyExistsException("Information about lighting with id № " + lightingId + " is already exists");
        }


        final float coefDemand = 0.9F;// check http://electricalschool.info/main/lighting/296-kak-opredelit-raschetnuju-moshhnost.html
        final float coefPRA = 1.1F;// check http://electricalschool.info/main/lighting/296-kak-opredelit-raschetnuju-moshhnost.html
        final float coefP = 1.4F;// check http://electricalschool.info/main/lighting/296-kak-opredelit-raschetnuju-moshhnost.html
        final float cosf = 0.95F;
        final float tgf = 0.33F;



        float distanceBetweenRowsOfLamps = luminaireSelection.getDistanceBetweenLampRows();
        float distanceBetweenWallAndFirstRowOfLamps = luminaireSelection.getDistanceBetweenWallAndFirstLampRow();
        short amountLuminairesPerLength = luminaireSelection.getAmountLuminairesPerLength();
        short amountLuminairesPerWidth = luminaireSelection.getAmountLuminairesPerWidth();


        short amountOfLuminaires = (short) (amountLuminairesPerLength * amountLuminairesPerWidth);

        float activePower = (float) (Math.ceil(coefDemand * (amountOfLampsInOneLuminaire * amountOfLuminaires) * activePowerOneLamp * coefPRA * 100)/ 100.0);

        float reactivePower = (float) (Math.ceil((activePower * tgf)* 100) / 100.0);

        float fullPower = (float) (Math.ceil(Math.sqrt(Math.pow(activePower, 2) +
                                Math.pow(reactivePower, 2))* 100) / 100.0);

        float electricCurrent = (float) (Math.ceil(((coefP * fullPower) / (Math.sqrt(3) * 380)) * 100000) / 100.0); // max electric current of this busbar


        float electricCurrentOfOneRowOfLuminaire =  Math.round(((coefP * electricCurrent) /
                        (Math.sqrt(3) * 0.38 * amountLuminairesPerLength)) *100) / 100.0F;

        return new LightInformation(lightingId, modelOfLuminaire, modelOfLamp, amountOfLuminaires, amountOfLampsInOneLuminaire,
                activePowerOneLamp, lightFluxOneLamp, distanceBetweenRowsOfLamps,
                distanceBetweenWallAndFirstRowOfLamps, amountLuminairesPerLength, amountLuminairesPerWidth,
                activePower, reactivePower, fullPower, electricCurrent, electricCurrentOfOneRowOfLuminaire, cosf, tgf);
    }


}
