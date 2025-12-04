package com.jobbersoft.fueltaservice.entity;

import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "tax_jurisdiction", uniqueConstraints = @UniqueConstraint(columnNames = {"code","effective_date"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaxJurisdiction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 32)
    private String code;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "base_rate", nullable = false, precision = 10, scale = 6)
    private BigDecimal baseRate;

    @NotNull
    @Column(name = "effective_date", nullable = false)
    private LocalDate effectiveDate;
}
