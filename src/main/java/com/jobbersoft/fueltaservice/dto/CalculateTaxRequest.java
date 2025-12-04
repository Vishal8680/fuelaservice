package com.jobbersoft.fueltaservice.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
@Builder
public class CalculateTaxRequest {
    @NotBlank
    private String shipmentId;

    @NotBlank
    private String jurisdictionCode;
    
    @NotNull
    @DecimalMin("0.0")
    private BigDecimal quantity;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal pricePerGallon;
}
