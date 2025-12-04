//package com.jobbersoft.fueltaservice.service;
//
//import com.jobbersoft.fueltaservice.entity.FuelTaxCalculation;
//import com.jobbersoft.fueltaservice.entity.TaxJurisdiction;
//import com.jobbersoft.fueltaservice.exception.JurisdictionNotFoundException;
//import com.jobbersoft.fueltaservice.repository.FuelTaxCalculationRepository;
//import com.jobbersoft.fueltaservice.repository.TaxJurisdictionRepository;
//import com.jobbersoft.fueltaservice.service.impl.TaxCalculationServiceImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.*;
//
//import javax.validation.ValidationException;
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class TaxCalculationServiceTest {
//
//    @Mock
//    private TaxJurisdictionRepository jurisdictionRepository;
//    @Mock
//    private FuelTaxCalculationRepository calculationRepository;
//
//    private TaxCalculationServiceImpl service;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        service = new TaxCalculationServiceImpl(jurisdictionRepository, calculationRepository);
//    }
//
//    @Test
//    void calculateTax_success() {
//        TaxJurisdiction j = TaxJurisdiction.builder().id(1L).code("CA").name("California").baseRate(new BigDecimal("0.1")).effectiveDate(LocalDate.now()).build();
//        when(jurisdictionRepository.findActiveByCode(eq("CA"), any())).thenReturn(Optional.of(j));
//
//        var resp = service.calculateTax("S1","CA", new BigDecimal("100"), new BigDecimal("3.5"));
//        assertNotNull(resp);
//        assertEquals("S1", resp.getShipmentId());
//        verify(calculationRepository, times(1)).save(any(FuelTaxCalculation.class));
//    }
//
//    @Test
//    void calculateTax_noJurisdiction() {
//        when(jurisdictionRepository.findActiveByCode(eq("XX"), any())).thenReturn(Optional.empty());
//        assertThrows(JurisdictionNotFoundException.class, () -> service.calculateTax("S2","XX", new BigDecimal("10"), new BigDecimal("2")));
//    }
//
//    @Test
//    void calculateTax_invalidInput() {
//        assertThrows(ValidationException.class, () -> service.calculateTax(null, "CA", new BigDecimal("1"), new BigDecimal("1")));
//    }
//}

package com.jobbersoft.fueltaservice.service;

import com.jobbersoft.fueltaservice.dto.CalculateTaxRequest;
import com.jobbersoft.fueltaservice.dto.CalculateTaxResponse;
import com.jobbersoft.fueltaservice.dto.FuelTaxHistoryDto;
import com.jobbersoft.fueltaservice.entity.FuelTaxCalculation;
import com.jobbersoft.fueltaservice.entity.TaxJurisdiction;
import com.jobbersoft.fueltaservice.exception.JurisdictionNotFoundException;
import com.jobbersoft.fueltaservice.repository.FuelTaxCalculationRepository;
import com.jobbersoft.fueltaservice.repository.TaxJurisdictionRepository;
import com.jobbersoft.fueltaservice.service.impl.TaxCalculationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import javax.validation.ValidationException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaxCalculationServiceTest {

    @Mock
    private TaxJurisdictionRepository jurisdictionRepository;

    @Mock
    private FuelTaxCalculationRepository calculationRepository;

    private TaxCalculationServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new TaxCalculationServiceImpl(jurisdictionRepository, calculationRepository);
    }

    // ====== calculateTax tests ======
    @Test
    void calculateTax_success() {
        TaxJurisdiction j = TaxJurisdiction.builder()
                .id(1L)
                .code("CA")
                .name("California")
                .baseRate(new BigDecimal("0.1"))
                .effectiveDate(LocalDate.now())
                .build();
        when(jurisdictionRepository.findActiveByCode(eq("CA"), any())).thenReturn(Optional.of(j));

        CalculateTaxRequest req = CalculateTaxRequest.builder()
                .shipmentId("S1")
                .jurisdictionCode("CA")
                .quantity(new BigDecimal("100"))
                .pricePerGallon(new BigDecimal("3.5"))
                .build();

        CalculateTaxResponse resp = service.calculateTax(req);

        assertNotNull(resp);
        assertEquals("S1", resp.getShipmentId());
        assertEquals("CA", resp.getJurisdictionCode());
        verify(calculationRepository, times(1)).save(any(FuelTaxCalculation.class));
    }

    @Test
    void calculateTax_noJurisdiction() {
        when(jurisdictionRepository.findActiveByCode(eq("XX"), any())).thenReturn(Optional.empty());

        CalculateTaxRequest req = CalculateTaxRequest.builder()
                .shipmentId("S2")
                .jurisdictionCode("XX")
                .quantity(new BigDecimal("10"))
                .pricePerGallon(new BigDecimal("2"))
                .build();

        assertThrows(JurisdictionNotFoundException.class, () -> service.calculateTax(req));
    }

    @Test
    void calculateTax_invalidInput() {
        // Null shipmentId
        CalculateTaxRequest req1 = CalculateTaxRequest.builder()
                .shipmentId(null)
                .jurisdictionCode("CA")
                .quantity(new BigDecimal("10"))
                .pricePerGallon(new BigDecimal("2"))
                .build();
        assertThrows(ValidationException.class, () -> service.calculateTax(req1));

        // Negative quantity
        CalculateTaxRequest req2 = CalculateTaxRequest.builder()
                .shipmentId("S3")
                .jurisdictionCode("CA")
                .quantity(new BigDecimal("-5"))
                .pricePerGallon(new BigDecimal("2"))
                .build();
        assertThrows(ValidationException.class, () -> service.calculateTax(req2));

        // Negative price
        CalculateTaxRequest req3 = CalculateTaxRequest.builder()
                .shipmentId("S4")
                .jurisdictionCode("CA")
                .quantity(new BigDecimal("10"))
                .pricePerGallon(new BigDecimal("-2"))
                .build();
        assertThrows(ValidationException.class, () -> service.calculateTax(req3));
    }

    // ====== getTaxHistory tests ======
    @Test
    void getTaxHistory_success() {
        TaxJurisdiction jurisdiction = TaxJurisdiction.builder()
                .id(1L)
                .code("CA")
                .name("California")
                .baseRate(new BigDecimal("0.1"))
                .build();

        FuelTaxCalculation calc1 = FuelTaxCalculation.builder()
                .id(101L)
                .shipmentId("S1")
                .jurisdiction(jurisdiction)
                .fuelQuantity(new BigDecimal("100"))
                .pricePerGallon(new BigDecimal("3.5"))
                .calculatedTax(new BigDecimal("35.000000"))
                .calculatedOn(OffsetDateTime.now())
                .build();

        FuelTaxCalculation calc2 = FuelTaxCalculation.builder()
                .id(102L)
                .shipmentId("S1")
                .jurisdiction(jurisdiction)
                .fuelQuantity(new BigDecimal("50"))
                .pricePerGallon(new BigDecimal("4"))
                .calculatedTax(new BigDecimal("20.000000"))
                .calculatedOn(OffsetDateTime.now())
                .build();

        when(calculationRepository.findByShipmentIdOrderByCalculatedOnDesc("S1"))
                .thenReturn(Arrays.asList(calc2, calc1));

        List<FuelTaxHistoryDto> history = service.getTaxHistory("S1");

        assertNotNull(history);
        assertEquals(2, history.size());
        assertEquals(102L, history.get(0).getId());
        verify(calculationRepository, times(1)).findByShipmentIdOrderByCalculatedOnDesc("S1");
    }

    @Test
    void getTaxHistory_invalidInput() {
        assertThrows(ValidationException.class, () -> service.getTaxHistory(null));
        assertThrows(ValidationException.class, () -> service.getTaxHistory(" "));
    }

    @Test
    void getTaxHistory_emptyList() {
        when(calculationRepository.findByShipmentIdOrderByCalculatedOnDesc("S2"))
                .thenReturn(List.of());

        List<FuelTaxHistoryDto> history = service.getTaxHistory("S2");
        assertNotNull(history);
        assertTrue(history.isEmpty());
    }
}

