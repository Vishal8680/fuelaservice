package com.jobbersoft.fueltaservice.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobbersoft.fueltaservice.dto.TaxJurisdictionDto;
import com.jobbersoft.fueltaservice.entity.TaxJurisdiction;
import com.jobbersoft.fueltaservice.repository.TaxJurisdictionRepository;
import com.jobbersoft.fueltaservice.service.TaxJurisdictionService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TaxJurisdictionServiceImpl implements TaxJurisdictionService {
	
	@Autowired
	TaxJurisdictionRepository jurisdictionRepository;

	@Override
	public List<TaxJurisdictionDto> getAll() {
		
		return jurisdictionRepository.findAll().stream().map(this::toDto).toList();
	}
	
	private TaxJurisdictionDto toDto(TaxJurisdiction j) {
        return TaxJurisdictionDto.builder()
                .code(j.getCode())
                .name(j.getName())
                .baseRate(j.getBaseRate())
                .effectiveDate(j.getEffectiveDate())
                .build();
    }

}
