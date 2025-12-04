package com.jobbersoft.fueltaservice.service;

import java.math.BigDecimal;
import java.util.List;

import com.jobbersoft.fueltaservice.dto.CalculateTaxRequest;
import com.jobbersoft.fueltaservice.dto.CalculateTaxResponse;
import com.jobbersoft.fueltaservice.dto.FuelTaxHistoryDto;

public interface TaxCalculationService {

    List<FuelTaxHistoryDto> getTaxHistory(String shipmentId);
    
    CalculateTaxResponse calculateTax(CalculateTaxRequest request);
}
