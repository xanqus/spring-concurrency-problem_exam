package com.example.stock.facade;

import com.example.stock.service.OptimisticLockStockService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class OptimisticLockStockFacade {

    private OptimisticLockStockService optimisticLockStockService;

    public OptimisticLockStockFacade(OptimisticLockStockService optimisticLockStockService) {
        this.optimisticLockStockService = optimisticLockStockService;
    }


    public void decrease(Long productId, Long quantity) throws InterruptedException {
        while(true) {
            try {
                optimisticLockStockService.decrease(productId, quantity);

                break;
            } catch( Exception e ) {
                Thread.sleep(50);
            }
        }
    }
}
