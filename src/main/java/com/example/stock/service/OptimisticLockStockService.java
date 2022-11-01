package com.example.stock.service;

import com.example.stock.domain.Stock;
import com.example.stock.repository.StockRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class OptimisticLockStockService {

    private StockRepository stockRepository;

    public OptimisticLockStockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Transactional
    public void decrease(Long productId, Long quantity) {
        Stock stock = stockRepository.findByIdWithOptimisticLock(productId);
        System.out.println("stock: " + stock);

        stock.decrease(quantity);

        stockRepository.saveAndFlush(stock);
    }
}
