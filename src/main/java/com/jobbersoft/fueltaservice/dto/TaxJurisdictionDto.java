package com.jobbersoft.fueltaservice.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaxJurisdictionDto {
    private String code;
    private String name;
    private BigDecimal baseRate;
    private LocalDate effectiveDate;
}
