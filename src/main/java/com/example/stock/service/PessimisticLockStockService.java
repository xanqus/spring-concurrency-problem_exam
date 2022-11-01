package com.example.stock.service;

import com.example.stock.domain.Stock;
import com.example.stock.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;

import javax.transaction.Transactional;

@Service
public class PessimisticLockStockService {

    private StockRepository stockRepository;

    public PessimisticLockStockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }


    @Transactional
    public void decrease(Long productId, Long quantity) {
        Stock stock = stockRepository.findByIdWithPessimisticLock(productId);


        stock.decrease(quantity);

        stockRepository.saveAndFlush(stock);
    }
}
