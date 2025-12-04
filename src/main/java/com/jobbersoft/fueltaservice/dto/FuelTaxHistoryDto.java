package com.jobbersoft.fueltaservice.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@Builder
public class FuelTaxHistoryDto {

    private Long id;

    private String shipmentId;

    private String jurisdictionCode;  
    
    private String jurisdictionName; 
    
    private BigDecimal jurisdictionBaseRate; 

    private BigDecimal fuelQuantity;

    private BigDecimal pricePerGallon;

    private BigDecimal calculatedTax;

    private OffsetDateTime calculatedOn;
}
