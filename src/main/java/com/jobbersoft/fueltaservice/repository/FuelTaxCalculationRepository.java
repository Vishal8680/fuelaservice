package com.jobbersoft.fueltaservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jobbersoft.fueltaservice.entity.FuelTaxCalculation;

import java.util.List;

@Repository
public interface FuelTaxCalculationRepository extends JpaRepository<FuelTaxCalculation, Long> {
    List<FuelTaxCalculation> findByShipmentIdOrderByCalculatedOnDesc(String shipmentId);
}
