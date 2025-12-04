package com.jobbersoft.fueltaservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jobbersoft.fueltaservice.dto.CalculateTaxRequest;
import com.jobbersoft.fueltaservice.dto.CalculateTaxResponse;
import com.jobbersoft.fueltaservice.dto.FuelTaxHistoryDto;
import com.jobbersoft.fueltaservice.dto.TaxJurisdictionDto;
import com.jobbersoft.fueltaservice.service.TaxCalculationService;
import com.jobbersoft.fueltaservice.service.TaxJurisdictionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@RestController
@RequestMapping("/v1/tax/fuel")
@RequiredArgsConstructor
@Validated
public class TaxController {
	
	@Autowired
	TaxCalculationService taxCalculationService;
	
	@Autowired
	TaxJurisdictionService jurisdictionService;

	@PostMapping("/calculate")
	public ResponseEntity<CalculateTaxResponse> calculate(@Validated @RequestBody CalculateTaxRequest request) {
	    log.info("Tax calculation request received: {}", request);
	    return ResponseEntity.ok(taxCalculationService.calculateTax(request));
	}


	@GetMapping("/history/{shipmentId}")
	public List<FuelTaxHistoryDto> history(@PathVariable String shipmentId) {
	    return taxCalculationService.getTaxHistory(shipmentId);
	}

	@GetMapping("/jurisdictions")
	public ResponseEntity<List<TaxJurisdictionDto>> getAll() {
	    return ResponseEntity.ok(jurisdictionService.getAll());
	}

}
