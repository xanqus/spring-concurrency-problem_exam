package com.example.stock.service;

import com.example.stock.domain.Stock;
import com.example.stock.repository.StockRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class StockService {

    private StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }



    public synchronized void decrease(Long productId, Long quantity) {

        Stock stock = stockRepository.findByProductId(productId).orElseThrow();
        System.out.println("stock: " + stock);

        stock.decrease(quantity);

        stockRepository.saveAndFlush(stock);
    }
}
