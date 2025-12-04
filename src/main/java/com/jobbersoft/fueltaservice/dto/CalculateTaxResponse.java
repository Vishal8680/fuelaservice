package com.jobbersoft.fueltaservice.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@Builder
public class CalculateTaxResponse {
    private String shipmentId;
    private String jurisdictionCode;
    private BigDecimal calculatedTax;
    private OffsetDateTime calculatedOn;
}
