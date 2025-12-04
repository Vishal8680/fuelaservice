package com.jobbersoft.fueltaservice.repository;


import com.jobbersoft.fueltaservice.entity.FuelTaxCalculation;
import com.jobbersoft.fueltaservice.entity.TaxJurisdiction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class FuelTaxCalculationRepositoryTest {

    @Autowired
    private FuelTaxCalculationRepository calculationRepository;

    @Autowired
    private TaxJurisdictionRepository jurisdictionRepository;

    @Test
    void findByShipmentIdOrderByCalculatedOnDesc_success() {
        TaxJurisdiction j = jurisdictionRepository.save(TaxJurisdiction.builder()
                .code("CA")
                .name("California")
                .baseRate(BigDecimal.valueOf(0.1))
                .effectiveDate(LocalDate.now())
                .build());

        FuelTaxCalculation c1 = FuelTaxCalculation.builder()
                .shipmentId("S1")
                .jurisdiction(j)
                .fuelQuantity(BigDecimal.valueOf(100))
                .pricePerGallon(BigDecimal.valueOf(3.5))
                .calculatedTax(BigDecimal.valueOf(35))
                .calculatedOn(OffsetDateTime.now())
                .build();

        FuelTaxCalculation c2 = FuelTaxCalculation.builder()
                .shipmentId("S1")
                .jurisdiction(j)
                .fuelQuantity(BigDecimal.valueOf(50))
                .pricePerGallon(BigDecimal.valueOf(4))
                .calculatedTax(BigDecimal.valueOf(20))
                .calculatedOn(OffsetDateTime.now().plusMinutes(1))
                .build();

        calculationRepository.save(c1);
        calculationRepository.save(c2);

        List<FuelTaxCalculation> history = calculationRepository.findByShipmentIdOrderByCalculatedOnDesc("S1");

        assertEquals(2, history.size());
        assertEquals(c2.getFuelQuantity(), history.get(0).getFuelQuantity()); // Most recent first
        assertEquals(c1.getFuelQuantity(), history.get(1).getFuelQuantity());
    }

    @Test
    void findByShipmentIdOrderByCalculatedOnDesc_empty() {
        List<FuelTaxCalculation> history = calculationRepository.findByShipmentIdOrderByCalculatedOnDesc("S2");
        assertNotNull(history);
        assertTrue(history.isEmpty());
    }
}

