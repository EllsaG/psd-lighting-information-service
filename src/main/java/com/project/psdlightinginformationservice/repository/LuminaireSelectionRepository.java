package com.project.psdlightinginformationservice.repository;

import com.project.psdlightinginformationservice.entity.LuminaireSelection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LuminaireSelectionRepository extends JpaRepository<LuminaireSelection,Short> {
}
