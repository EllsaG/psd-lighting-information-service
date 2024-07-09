package psdlightinginformationservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.psdlightinginformationservice.PsdLightingInformationServiceApplication;
import com.project.psdlightinginformationservice.controller.dto.*;
import com.project.psdlightinginformationservice.entity.LightInformation;
import com.project.psdlightinginformationservice.entity.LuminaireSelection;
import com.project.psdlightinginformationservice.repository.LightingInformationRepository;
import com.project.psdlightinginformationservice.repository.LuminaireSelectionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import psdlightinginformationservice.config.SpringH2DatabaseConfig;

import org.junit.jupiter.api.Assertions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.NoSuchElementException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {PsdLightingInformationServiceApplication.class, SpringH2DatabaseConfig.class})
public class LightingInformationControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private LuminaireSelectionRepository luminaireSelectionRepository;

    @Autowired
    private LightingInformationRepository lightingInformationRepository;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }


    @Test
    @Sql(scripts = {"/sql/clearLuminaireSelectionDB.sql"})
    public void createLuminariesSelection() throws Exception {

        String REQUEST = createLuminaireSelectionRequestAsString();

        MvcResult mvcResult = mockMvc.perform(put("/create/selectionInformation")
                        .content(REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isOk())
                .andReturn();

        LuminaireSelection luminaireSelection = luminaireSelectionRepository.findById((short) 1)
                .orElseThrow(() -> new NoSuchElementException("No value present"));

        String body = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        ObjectMapper objectMapper = new ObjectMapper();
        LuminaireSelectionResponseDTO luminaireSelectionResponseDTO = objectMapper
                .readValue(body, LuminaireSelectionResponseDTO.class);

        Assertions.assertEquals(4, luminaireSelection.getDistanceBetweenLampRows());
        Assertions.assertEquals(1, luminaireSelection.getDistanceBetweenWallAndFirstLampRow());
        Assertions.assertEquals(18, luminaireSelection.getAmountLuminairesPerLength());
        Assertions.assertEquals(9, luminaireSelection.getAmountLuminairesPerWidth());
        Assertions.assertEquals(10350, luminaireSelection.getLightFlux());
        Assertions.assertEquals(6, luminaireSelection.getProductionHallHeight());
        Assertions.assertEquals(36, luminaireSelection.getProductionHallWidth());
        Assertions.assertEquals(72, luminaireSelection.getProductionHallLength());


        LightFluxAtAmountOfLamps lightFluxWithOneLampInLuminaire = luminaireSelectionResponseDTO.getLightFluxAtAmountOfLampsList().get(0);
        LightFluxAtAmountOfLamps lightFluxWithTwoLampsInLuminaire = luminaireSelectionResponseDTO.getLightFluxAtAmountOfLampsList().get(1);
        LightFluxAtAmountOfLamps lightFluxWithThreeLampsInLuminaire = luminaireSelectionResponseDTO.getLightFluxAtAmountOfLampsList().get(2);
        LightFluxAtAmountOfLamps lightFluxWithFourLampsInLuminaire = luminaireSelectionResponseDTO.getLightFluxAtAmountOfLampsList().get(3);

        Assertions.assertEquals(1, lightFluxWithOneLampInLuminaire.getAmountOfLampsInOneLuminaire());
        Assertions.assertEquals(14490.0F, lightFluxWithOneLampInLuminaire.getMinLightFluxForLuminaireSelection());
        Assertions.assertEquals(16560.0F, lightFluxWithOneLampInLuminaire.getMaxLightFluxForLuminaireSelection());

        Assertions.assertEquals(2, lightFluxWithTwoLampsInLuminaire.getAmountOfLampsInOneLuminaire());
        Assertions.assertEquals(7245.0F, lightFluxWithTwoLampsInLuminaire.getMinLightFluxForLuminaireSelection());
        Assertions.assertEquals(8280.0F, lightFluxWithTwoLampsInLuminaire.getMaxLightFluxForLuminaireSelection());

        Assertions.assertEquals(3, lightFluxWithThreeLampsInLuminaire.getAmountOfLampsInOneLuminaire());
        Assertions.assertEquals(4830.0F, lightFluxWithThreeLampsInLuminaire.getMinLightFluxForLuminaireSelection());
        Assertions.assertEquals(5520.0F, lightFluxWithThreeLampsInLuminaire.getMaxLightFluxForLuminaireSelection());

        Assertions.assertEquals(4, lightFluxWithFourLampsInLuminaire.getAmountOfLampsInOneLuminaire());
        Assertions.assertEquals(3622.5F, lightFluxWithFourLampsInLuminaire.getMinLightFluxForLuminaireSelection());
        Assertions.assertEquals(4140.0F, lightFluxWithFourLampsInLuminaire.getMaxLightFluxForLuminaireSelection());

    }

    @Test
    @Sql(scripts = {"/sql/clearLuminaireSelectionDB.sql","/sql/clearLightInformationDB.sql",
            "/sql/addLuminaireSelectionInformation.sql"})
    public void createLighting() throws Exception {

        String REQUEST = createLightingInformationRequestAsString();

        MvcResult mvcResult = mockMvc.perform(put("/create/lighting")
                        .content(REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isOk())
                .andReturn();

        LightInformation lightInformationById = lightingInformationRepository.findById((short) 1)
                .orElseThrow(() -> new NoSuchElementException("No value present"));

        String body = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        ObjectMapper objectMapper = new ObjectMapper();
        LightingInformationResponseDTO lightingInformationResponseDTO = objectMapper
                .readValue(body, LightingInformationResponseDTO.class);
        LightInformation lightInformation = lightingInformationResponseDTO.getLightInformationList().get(0);

        Assertions.assertEquals(lightInformationById.getLampModel(), lightInformation.getLampModel());
        Assertions.assertEquals(lightInformationById.getAmountLuminaires(), lightInformation.getAmountLuminaires());
        Assertions.assertEquals(lightInformationById.getAmountLampsInOneLuminaire(), lightInformation.getAmountLampsInOneLuminaire());
        Assertions.assertEquals(lightInformationById.getOneLampPower(), lightInformation.getOneLampPower());
        Assertions.assertEquals(lightInformationById.getOneLampLightFlux(), lightInformation.getOneLampLightFlux());
        Assertions.assertEquals(lightInformationById.getDistanceBetweenLampRows(), lightInformation.getDistanceBetweenLampRows());
        Assertions.assertEquals(lightInformationById.getDistanceBetweenWallAndFirstLampRow(), lightInformation.getDistanceBetweenWallAndFirstLampRow());
        Assertions.assertEquals(lightInformationById.getAmountLuminairesPerLength(), lightInformation.getAmountLuminairesPerLength());
        Assertions.assertEquals(lightInformationById.getAmountLuminairesPerWidth(), lightInformation.getAmountLuminairesPerWidth());
        Assertions.assertEquals(lightInformationById.getTotalActivePower(), lightInformation.getTotalActivePower());
        Assertions.assertEquals(lightInformationById.getTotalReactivePower(), lightInformation.getTotalReactivePower());
        Assertions.assertEquals(lightInformationById.getTotalFullPower(), lightInformation.getTotalFullPower());
        Assertions.assertEquals(lightInformationById.getElectricCurrent(), lightInformation.getElectricCurrent());
        Assertions.assertEquals(lightInformationById.getElectricCurrentOfOneRowOfLuminaires(), lightInformation.getElectricCurrentOfOneRowOfLuminaires());
        Assertions.assertEquals(lightInformationById.getCosF(), lightInformation.getCosF());
        Assertions.assertEquals(lightInformationById.getTgF(), lightInformation.getTgF());
    }

    @Test
    @Sql(scripts = {"/sql/clearLightInformationDB.sql","/sql/clearLuminaireSelectionDB.sql",
            "/sql/addLuminaireSelectionInformation.sql","/sql/addLightInformation.sql"})
    public void updateLighting() throws Exception {

        String REQUEST = createLightingInformationUpdateRequestAsString();

        MvcResult mvcResult = mockMvc.perform(put("/update/lighting")
                        .content(REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isOk())
                .andReturn();

        LightInformation lightInformationById = lightingInformationRepository.findById((short) 1)
                .orElseThrow(() -> new NoSuchElementException("No value present"));

        String body = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        ObjectMapper objectMapper = new ObjectMapper();
        LightingInformationResponseDTO lightingInformationResponseDTO = objectMapper
                .readValue(body, LightingInformationResponseDTO.class);
        LightInformation lightInformation = lightingInformationResponseDTO.getLightInformationList().get(0);

        Assertions.assertEquals(lightInformationById.getLampModel(), lightInformation.getLampModel());
        Assertions.assertEquals(lightInformationById.getAmountLuminaires(), lightInformation.getAmountLuminaires());
        Assertions.assertEquals(lightInformationById.getAmountLampsInOneLuminaire(), lightInformation.getAmountLampsInOneLuminaire());
        Assertions.assertEquals(lightInformationById.getOneLampPower(), lightInformation.getOneLampPower());
        Assertions.assertEquals(lightInformationById.getOneLampLightFlux(), lightInformation.getOneLampLightFlux());
        Assertions.assertEquals(lightInformationById.getDistanceBetweenLampRows(), lightInformation.getDistanceBetweenLampRows());
        Assertions.assertEquals(lightInformationById.getDistanceBetweenWallAndFirstLampRow(), lightInformation.getDistanceBetweenWallAndFirstLampRow());
        Assertions.assertEquals(lightInformationById.getAmountLuminairesPerLength(), lightInformation.getAmountLuminairesPerLength());
        Assertions.assertEquals(lightInformationById.getAmountLuminairesPerWidth(), lightInformation.getAmountLuminairesPerWidth());
        Assertions.assertEquals(lightInformationById.getTotalActivePower(), lightInformation.getTotalActivePower());
        Assertions.assertEquals(lightInformationById.getTotalReactivePower(), lightInformation.getTotalReactivePower());
        Assertions.assertEquals(lightInformationById.getTotalFullPower(), lightInformation.getTotalFullPower());
        Assertions.assertEquals(lightInformationById.getElectricCurrent(), lightInformation.getElectricCurrent());
        Assertions.assertEquals(lightInformationById.getElectricCurrentOfOneRowOfLuminaires(), lightInformation.getElectricCurrentOfOneRowOfLuminaires());
        Assertions.assertEquals(lightInformationById.getCosF(), lightInformation.getCosF());
        Assertions.assertEquals(lightInformationById.getTgF(), lightInformation.getTgF());
    }



    @Test
    @Sql(scripts = {"/sql/clearLuminaireSelectionDB.sql", "/sql/addLuminaireSelectionInformation.sql"})
    public void updateLuminariesSelectionInformation() throws Exception {

        String REQUEST = createLuminaireSelectionUpdateRequestAsString();

        MvcResult mvcResult = mockMvc.perform(put("/update/selectionInformation")
                        .content(REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isOk())
                .andReturn();

        LuminaireSelection luminaireSelection = luminaireSelectionRepository.findById((short) 1)
                .orElseThrow(() -> new NoSuchElementException("No value present"));

        String body = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        ObjectMapper objectMapper = new ObjectMapper();
        LuminaireSelectionResponseDTO luminaireSelectionResponseDTO = objectMapper
                .readValue(body, LuminaireSelectionResponseDTO.class);

        Assertions.assertEquals(5, luminaireSelection.getDistanceBetweenLampRows());
        Assertions.assertEquals(1.3F, luminaireSelection.getDistanceBetweenWallAndFirstLampRow());
        Assertions.assertEquals(12, luminaireSelection.getAmountLuminairesPerLength());
        Assertions.assertEquals(6, luminaireSelection.getAmountLuminairesPerWidth());
        Assertions.assertEquals(16172, luminaireSelection.getLightFlux());
        Assertions.assertEquals(7, luminaireSelection.getProductionHallHeight());
        Assertions.assertEquals(30, luminaireSelection.getProductionHallWidth());
        Assertions.assertEquals(60, luminaireSelection.getProductionHallLength());


        LightFluxAtAmountOfLamps lightFluxWithOneLampInLuminaire = luminaireSelectionResponseDTO.getLightFluxAtAmountOfLampsList().get(0);
        LightFluxAtAmountOfLamps lightFluxWithTwoLampsInLuminaire = luminaireSelectionResponseDTO.getLightFluxAtAmountOfLampsList().get(1);
        LightFluxAtAmountOfLamps lightFluxWithThreeLampsInLuminaire = luminaireSelectionResponseDTO.getLightFluxAtAmountOfLampsList().get(2);
        LightFluxAtAmountOfLamps lightFluxWithFourLampsInLuminaire = luminaireSelectionResponseDTO.getLightFluxAtAmountOfLampsList().get(3);

        Assertions.assertEquals(1, lightFluxWithOneLampInLuminaire.getAmountOfLampsInOneLuminaire());
        Assertions.assertEquals(22641.0F, lightFluxWithOneLampInLuminaire.getMinLightFluxForLuminaireSelection());
        Assertions.assertEquals(25876.0F, lightFluxWithOneLampInLuminaire.getMaxLightFluxForLuminaireSelection());

        Assertions.assertEquals(2, lightFluxWithTwoLampsInLuminaire.getAmountOfLampsInOneLuminaire());
        Assertions.assertEquals(11320.5F, lightFluxWithTwoLampsInLuminaire.getMinLightFluxForLuminaireSelection());
        Assertions.assertEquals(12938.0F, lightFluxWithTwoLampsInLuminaire.getMaxLightFluxForLuminaireSelection());

        Assertions.assertEquals(3, lightFluxWithThreeLampsInLuminaire.getAmountOfLampsInOneLuminaire());
        Assertions.assertEquals(7547.0F, lightFluxWithThreeLampsInLuminaire.getMinLightFluxForLuminaireSelection());
        Assertions.assertEquals(8625.3F, lightFluxWithThreeLampsInLuminaire.getMaxLightFluxForLuminaireSelection());

        Assertions.assertEquals(4, lightFluxWithFourLampsInLuminaire.getAmountOfLampsInOneLuminaire());
        Assertions.assertEquals(5660.3F, lightFluxWithFourLampsInLuminaire.getMinLightFluxForLuminaireSelection());
        Assertions.assertEquals(6469.0F, lightFluxWithFourLampsInLuminaire.getMaxLightFluxForLuminaireSelection());

    }





    @Test
    @Sql(scripts = {"/sql/clearLuminaireSelectionDB.sql", "/sql/addLuminaireSelectionInformation.sql"})
    public void deleteLuminaireById() throws Exception {

        MvcResult mvcResult = mockMvc.perform(delete("/delete/selectionInformation/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isOk())
                .andReturn();

        List<LuminaireSelection> luminaireSelectionRepositoryAll = luminaireSelectionRepository.findAll();
        LuminaireSelection luminaireSelection = luminaireSelectionRepositoryAll.get(0);

        String body = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        ObjectMapper objectMapper = new ObjectMapper();
        LuminaireSelectionResponseDTO luminaireSelectionResponseDTO = objectMapper
                .readValue(body, LuminaireSelectionResponseDTO.class);
        LightFluxAtAmountOfLamps lightFluxAtAmountOfLamps = luminaireSelectionResponseDTO.getLightFluxAtAmountOfLampsList().get(0);

        Assertions.assertEquals(luminaireSelection.getLuminaireSelectionId(), lightFluxAtAmountOfLamps.getLightingInformationId());
    }


    @Test
    @Sql(scripts = {"/sql/clearLightInformationDB.sql","/sql/clearLuminaireSelectionDB.sql",
            "/sql/addLuminaireSelectionInformation.sql","/sql/addLightInformation.sql"})
    public void deleteLightingById() throws Exception {

        MvcResult mvcResult = mockMvc.perform(delete("/delete/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isOk())
                .andReturn();

        List<LightInformation> lightInformationRepositoryAll = lightingInformationRepository.findAll();
        LightInformation lightInformation = lightInformationRepositoryAll.get(0);

        String body = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        ObjectMapper objectMapper = new ObjectMapper();
        LightingInformationResponseDTO lightingInformationResponseDTO = objectMapper
                .readValue(body, LightingInformationResponseDTO.class);
        LightInformation lightInformationResponse = lightingInformationResponseDTO.getLightInformationList().get(0);

        Assertions.assertEquals(lightInformation.getLightInformationId(), lightInformationResponse.getLightInformationId());

    }


    private String createLuminaireSelectionRequestAsString() throws JsonProcessingException {
        LuminaireSelectionRequestDTO luminaireSelectionRequestDTO =
                new LuminaireSelectionRequestDTO((short) 1, 6, 36, 72);
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.writeValueAsString(luminaireSelectionRequestDTO);
    }


    private String createLightingInformationRequestAsString() throws JsonProcessingException {
        LightingInformationRequestDTO lightingInformationRequestDTO =
                new LightingInformationRequestDTO((short) 1, "ЛСП68-2х80-011 HF IP65",
                        "FT5/80W/830/24000HRS/GE/SL1/30", (short) 2, 7000, 80);
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.writeValueAsString(lightingInformationRequestDTO);
    }

    private String createLuminaireSelectionUpdateRequestAsString() throws JsonProcessingException {
        LuminaireSelectionRequestDTO luminaireSelectionRequestDTO =
                new LuminaireSelectionRequestDTO((short) 1, 7, 30, 60);
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.writeValueAsString(luminaireSelectionRequestDTO);
    }

    private String createLightingInformationUpdateRequestAsString() throws JsonProcessingException {
        LightingInformationRequestDTO lightingInformationRequestDTO =
                new LightingInformationRequestDTO((short) 1, "ЛСП68-4х40-011 HF IP65",
                        "FT5/40W/830/12000HRS/GE/SL1/30", (short) 4, 3500, 40);
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.writeValueAsString(lightingInformationRequestDTO);
    }

}