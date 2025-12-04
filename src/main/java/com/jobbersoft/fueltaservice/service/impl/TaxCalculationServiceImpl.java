package com.jobbersoft.fueltaservice.service.impl;

import com.jobbersoft.fueltaservice.dto.CalculateTaxRequest;
import com.jobbersoft.fueltaservice.dto.CalculateTaxResponse;
import com.jobbersoft.fueltaservice.dto.FuelTaxHistoryDto;
import com.jobbersoft.fueltaservice.entity.FuelTaxCalculation;
import com.jobbersoft.fueltaservice.entity.TaxJurisdiction;
import com.jobbersoft.fueltaservice.exception.JurisdictionNotFoundException;
import com.jobbersoft.fueltaservice.repository.FuelTaxCalculationRepository;
import com.jobbersoft.fueltaservice.repository.TaxJurisdictionRepository;
import com.jobbersoft.fueltaservice.service.TaxCalculationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.validation.ValidationException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaxCalculationServiceImpl implements TaxCalculationService {

    private final TaxJurisdictionRepository jurisdictionRepository;
    private final FuelTaxCalculationRepository calculationRepository;
    
    @Override
    public CalculateTaxResponse calculateTax(CalculateTaxRequest req) {
    	log.info("Calculating tax for request: {}", req);
        validate(req);
        TaxJurisdiction jurisdiction = jurisdictionRepository
                .findActiveByCode(req.getJurisdictionCode(), LocalDate.now())
                .orElseThrow(() -> new JurisdictionNotFoundException(
                        "Jurisdiction not found for code: " + req.getJurisdictionCode()
                ));
        
        BigDecimal calculatedTax = req.getQuantity()
                .multiply(req.getPricePerGallon())
                .multiply(jurisdiction.getBaseRate())
                .setScale(6, BigDecimal.ROUND_HALF_EVEN);
        
        FuelTaxCalculation entity = toEntity(req, jurisdiction, calculatedTax);
        calculationRepository.save(entity);

        return toDto(entity);

    }
    
   

    @Override
    @Transactional(readOnly = true)
    public List<FuelTaxHistoryDto> getTaxHistory(String shipmentId) {
        if (shipmentId == null || shipmentId.isBlank()) {
            throw new ValidationException("shipmentId must be provided for history lookup");
        }

        return calculationRepository.findByShipmentIdOrderByCalculatedOnDesc(shipmentId)
                .stream()
                .map(f -> FuelTaxHistoryDto.builder()
                        .id(f.getId())
                        .shipmentId(f.getShipmentId())
                        .jurisdictionCode(f.getJurisdiction().getCode())
                        .jurisdictionName(f.getJurisdiction().getName())
                        .jurisdictionBaseRate(f.getJurisdiction().getBaseRate())
                        .fuelQuantity(f.getFuelQuantity())
                        .pricePerGallon(f.getPricePerGallon())
                        .calculatedTax(f.getCalculatedTax())
                        .calculatedOn(f.getCalculatedOn())
                        .build()
                ).collect(Collectors.toList());
    }

// For Validate CalculateTaxRequest
    private void validate(CalculateTaxRequest req) {
        if (req.getShipmentId() == null || req.getShipmentId().isBlank()) {
            throw new ValidationException("shipmentId is required");
        }
        if (req.getJurisdictionCode() == null || req.getJurisdictionCode().isBlank()) {
            throw new ValidationException("jurisdictionCode is required");
        }
        if (req.getQuantity() == null || req.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("quantity must be greater than zero");
        }
        if (req.getPricePerGallon() == null || req.getPricePerGallon().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("pricePerGallon must be greater than zero");
        }
    }
    
    private FuelTaxCalculation toEntity(CalculateTaxRequest req, TaxJurisdiction jurisdiction, BigDecimal tax) {
        return FuelTaxCalculation.builder()
                .shipmentId(req.getShipmentId())
                .jurisdiction(jurisdiction)
                .fuelQuantity(req.getQuantity())
                .pricePerGallon(req.getPricePerGallon())
                .calculatedTax(tax)
                .calculatedOn(OffsetDateTime.now())
                .build();
    }

    private CalculateTaxResponse toDto(FuelTaxCalculation entity) {
        return CalculateTaxResponse.builder()
                .shipmentId(entity.getShipmentId())
                .jurisdictionCode(entity.getJurisdiction().getCode())
                .calculatedTax(entity.getCalculatedTax())
                .calculatedOn(entity.getCalculatedOn())
                .build();
    }

    private FuelTaxHistoryDto toHistoryDto(FuelTaxCalculation entity) {
        return FuelTaxHistoryDto.builder()
                .id(entity.getId())
                .shipmentId(entity.getShipmentId())
                .jurisdictionCode(entity.getJurisdiction().getCode())
                .jurisdictionName(entity.getJurisdiction().getName())
                .jurisdictionBaseRate(entity.getJurisdiction().getBaseRate())
                .fuelQuantity(entity.getFuelQuantity())
                .pricePerGallon(entity.getPricePerGallon())
                .calculatedTax(entity.getCalculatedTax())
                .calculatedOn(entity.getCalculatedOn())
                .build();
    }
}
