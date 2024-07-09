package com.project.psdlightinginformationservice.repository;

import com.project.psdlightinginformationservice.entity.LightInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LightingInformationRepository extends JpaRepository<LightInformation,Short> {
}
