package com.jobbersoft.fueltaservice.repository;

import com.jobbersoft.fueltaservice.entity.TaxJurisdiction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TaxJurisdictionRepositoryTest {

    @Autowired
    private TaxJurisdictionRepository jurisdictionRepository;

    @Test
    void findActiveByCode_success() {
        TaxJurisdiction j = TaxJurisdiction.builder()
                .code("CA")
                .name("California")
                .baseRate(BigDecimal.valueOf(0.1))
                .effectiveDate(LocalDate.now())
                .build();
        jurisdictionRepository.save(j);

        Optional<TaxJurisdiction> result = jurisdictionRepository.findActiveByCode("CA", LocalDate.now());
        assertTrue(result.isPresent());
        assertEquals("CA", result.get().getCode());
        assertEquals("California", result.get().getName());
    }

    @Test
    void findActiveByCode_notFound() {
        Optional<TaxJurisdiction> result = jurisdictionRepository.findActiveByCode("XX", LocalDate.now());
        assertTrue(result.isEmpty());
    }
}

