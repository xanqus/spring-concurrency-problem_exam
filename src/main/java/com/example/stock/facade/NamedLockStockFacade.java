package com.example.stock.facade;

import com.example.stock.repository.LockRepository;
import com.example.stock.service.StockService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class NamedLockStockFacade {

    private final LockRepository lockRepository;

    private final StockService stockService;

    public NamedLockStockFacade(LockRepository lockRepository, StockService stockService) {
        this.lockRepository = lockRepository;
        this.stockService = stockService;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void decrease(Long productId, Long quantity) {
        try {
            lockRepository.getLock(productId.toString());
            stockService.decrease(productId, quantity);
        } finally {
            lockRepository.releaseLock(productId.toString());
        }
    }
}