package com.example.stock.repository;

import com.example.stock.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from Stock s where s.productId = :productId")
    Stock findByIdWithPessimisticLock(@Param("productId") Long productId);

    @Lock(value = LockModeType.OPTIMISTIC)
    @Query("select s from Stock s where s.productId = :productId")
    Stock findByIdWithOptimisticLock(@Param("productId") Long productId);

    Optional<Stock> findByProductId(Long productId);
}
