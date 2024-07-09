package com.project.psdlightinginformationservice.entity;

import lombok.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "luminaire_selection")
public class LuminaireSelection {

    @Id
    @Column(name = "luminaire_selection_id")
    private short luminaireSelectionId;
    @Column(name = "distance_between_lamp_rows")
    private float distanceBetweenLampRows;
    @Column(name = "distance_between_wall_and_first_lamp_row")
    private float distanceBetweenWallAndFirstLampRow;
    @Column(name = "amount_luminaires_per_length")
    private short amountLuminairesPerLength;
    @Column(name = "amount_luminaires_per_width")
    private short amountLuminairesPerWidth;
    @Column(name = "light_flux")
    private float lightFlux;
    @Column(name = "production_hall_height")
    private float productionHallHeight;
    @Column(name = "production_hall_width")
    private float productionHallWidth;
    @Column(name = "production_hall_length")
    private float productionHallLength;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LuminaireSelection that = (LuminaireSelection) o;
        return luminaireSelectionId == that.luminaireSelectionId && Float.compare(distanceBetweenLampRows, that.distanceBetweenLampRows) == 0 && Float.compare(distanceBetweenWallAndFirstLampRow, that.distanceBetweenWallAndFirstLampRow) == 0 && amountLuminairesPerLength == that.amountLuminairesPerLength && amountLuminairesPerWidth == that.amountLuminairesPerWidth && Float.compare(lightFlux, that.lightFlux) == 0 && Float.compare(productionHallHeight, that.productionHallHeight) == 0 && Float.compare(productionHallWidth, that.productionHallWidth) == 0 && Float.compare(productionHallLength, that.productionHallLength) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(luminaireSelectionId, distanceBetweenLampRows, distanceBetweenWallAndFirstLampRow, amountLuminairesPerLength, amountLuminairesPerWidth, lightFlux, productionHallHeight, productionHallWidth, productionHallLength);
    }
}
