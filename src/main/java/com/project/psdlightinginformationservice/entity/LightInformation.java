package com.project.psdlightinginformationservice.entity;

import lombok.*;

import jakarta.persistence.*;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name="light_information")
public class LightInformation {
    @Id
    @Column(name="light_information_id")
    private short lightInformationId;

    @Column(name="luminaire_model")
    private String luminaireModel;
    @Column(name="lamp_model")
    private String lampModel;
    @Column(name="amount_luminaires")
    private short amountLuminaires;
    @Column(name="amount_lamps_in_one_luminaire")
    private short amountLampsInOneLuminaire;
    @Column(name="one_lamp_power")
    private float oneLampPower;
    @Column(name="one_lamp_light_flux")
    private float oneLampLightFlux;
    @Column(name="distance_between_lamp_rows")
    private float distanceBetweenLampRows;
    @Column(name="distance_between_wall_and_first_lamp_row")
    private float distanceBetweenWallAndFirstLampRow;
    @Column(name="amount_luminaires_per_length")
    private short amountLuminairesPerLength;
    @Column(name="amount_luminaires_per_width")
    private short amountLuminairesPerWidth;
    @Column(name="total_active_power")
    private float totalActivePower;
    @Column(name="total_reactive_power")
    private float totalReactivePower;
    @Column(name="total_full_power")
    private float totalFullPower;
    @Column(name="electric_current")
    private float electricCurrent;
    @Column(name="electric_current_of_one_row_of_luminaires")
    private float electricCurrentOfOneRowOfLuminaires;
    @Column(name = "cos_f")
    private float cosF;
    @Column(name = "tg_f")
    private float tgF;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LightInformation that = (LightInformation) o;
        return lightInformationId == that.lightInformationId && amountLuminaires == that.amountLuminaires && amountLampsInOneLuminaire == that.amountLampsInOneLuminaire && Float.compare(oneLampPower, that.oneLampPower) == 0 && Float.compare(oneLampLightFlux, that.oneLampLightFlux) == 0 && Float.compare(distanceBetweenLampRows, that.distanceBetweenLampRows) == 0 && Float.compare(distanceBetweenWallAndFirstLampRow, that.distanceBetweenWallAndFirstLampRow) == 0 && amountLuminairesPerLength == that.amountLuminairesPerLength && amountLuminairesPerWidth == that.amountLuminairesPerWidth && Float.compare(totalActivePower, that.totalActivePower) == 0 && Float.compare(totalReactivePower, that.totalReactivePower) == 0 && Float.compare(totalFullPower, that.totalFullPower) == 0 && Float.compare(electricCurrent, that.electricCurrent) == 0 && Float.compare(electricCurrentOfOneRowOfLuminaires, that.electricCurrentOfOneRowOfLuminaires) == 0 && Float.compare(cosF, that.cosF) == 0 && Float.compare(tgF, that.tgF) == 0 && Objects.equals(luminaireModel, that.luminaireModel) && Objects.equals(lampModel, that.lampModel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lightInformationId, luminaireModel, lampModel, amountLuminaires, amountLampsInOneLuminaire, oneLampPower, oneLampLightFlux, distanceBetweenLampRows, distanceBetweenWallAndFirstLampRow, amountLuminairesPerLength, amountLuminairesPerWidth, totalActivePower, totalReactivePower, totalFullPower, electricCurrent, electricCurrentOfOneRowOfLuminaires, cosF, tgF);
    }
}
