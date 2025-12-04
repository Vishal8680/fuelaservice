package com.jobbersoft.fueltaservice.entity;

import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "fuel_tax_calculation", indexes = {@Index(name = "idx_shipment", columnList = "shipment_id")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FuelTaxCalculation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "shipment_id", nullable = false, length = 64)
    private String shipmentId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "jurisdiction_id", nullable = false)
    private TaxJurisdiction jurisdiction;

    @NotNull
    @DecimalMin("0.0")
    @Column(name = "fuel_quantity", nullable = false, precision = 18, scale = 6)
    private BigDecimal fuelQuantity;

    @NotNull
    @DecimalMin("0.0")
    @Column(name = "price_per_gallon", nullable = false, precision = 18, scale = 6)
    private BigDecimal pricePerGallon;

    @NotNull
    @Column(name = "calculated_tax", nullable = false, precision = 18, scale = 6)
    private BigDecimal calculatedTax;

    @NotNull
    @Column(name = "calculated_on", nullable = false)
    private OffsetDateTime calculatedOn;
}
