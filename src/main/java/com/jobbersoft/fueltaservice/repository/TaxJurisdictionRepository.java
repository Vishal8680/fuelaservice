package com.jobbersoft.fueltaservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jobbersoft.fueltaservice.entity.TaxJurisdiction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaxJurisdictionRepository extends JpaRepository<TaxJurisdiction, Long> {
    Optional<TaxJurisdiction> findTopByCodeAndEffectiveDateLessThanEqualOrderByEffectiveDateDesc(String code, LocalDate asOfDate);
    List<TaxJurisdiction> findByBaseRateBetween(BigDecimal minRate, BigDecimal maxRate);
    

    @Query("""
            SELECT t FROM TaxJurisdiction t
            WHERE t.code = :code AND t.effectiveDate <= :asOfDate
            ORDER BY t.effectiveDate DESC
            """)
    List<TaxJurisdiction> findActiveListByCode(@Param("code") String code,
                                               @Param("asOfDate") LocalDate asOfDate);

    default Optional<TaxJurisdiction> findActiveByCode(String code, LocalDate asOfDate) {
        List<TaxJurisdiction> list = findActiveListByCode(code, asOfDate);
        return list.stream().findFirst();
    }

    @Query("""
            SELECT t FROM TaxJurisdiction t
            WHERE t.baseRate BETWEEN :min AND :max
            ORDER BY t.baseRate
            """)
    List<TaxJurisdiction> findByRateRange(@Param("min") BigDecimal minRate,
                                          @Param("max") BigDecimal maxRate);
}
    
