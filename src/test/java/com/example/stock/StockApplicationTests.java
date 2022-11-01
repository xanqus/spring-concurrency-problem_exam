package com.example.stock;


import com.example.stock.domain.Stock;
import com.example.stock.facade.OptimisticLockStockFacade;
import com.example.stock.repository.StockRepository;
import com.example.stock.service.PessimisticLockStockService;
import com.example.stock.service.StockService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class StockApplicationTests {


	@Autowired
	private StockService stockService;

	@Autowired
	private StockRepository stockRepository;

	@Autowired
	private PessimisticLockStockService pessimisticLockStockService;

	@Autowired
	private OptimisticLockStockFacade optimisticLockStockFacade;


	@BeforeEach
	public void before() {

		Stock stock = new Stock(1L, 100L);

		stockRepository.saveAndFlush(stock);


	}

	@AfterEach
	public void after() {
		stockRepository.deleteAll();
	}


	@Test
	void test1() {
		for(int i = 0; i < 100; i++) {
			stockService.decrease(1L, 1L);
		}
		Stock stock = stockRepository.findByProductId(1L).orElseThrow();
		assertThat(stock.getQuantity())
				.isEqualTo(0L);

	}

	@Test
	void test2() throws InterruptedException {

		int threadCount = 100;
		ExecutorService executorService = Executors.newFixedThreadPool(32);
		CountDownLatch latch = new CountDownLatch(threadCount);

		for(int i = 0; i < threadCount; i++) {
			executorService.submit(() -> {
				try {
					stockService.decrease(1L, 1L);
				} finally {
					latch.countDown();
				}
			});
		}
		latch.await();

		Stock stock = stockRepository.findByProductId(1L).orElseThrow();

		assertThat(stock.getQuantity())
				.isEqualTo(0L);
	}

	@Test
	void test3() throws InterruptedException {

		int threadCount = 100;
		ExecutorService executorService = Executors.newFixedThreadPool(32);
		CountDownLatch latch = new CountDownLatch(threadCount);

		for(int i = 0; i < threadCount; i++) {
			executorService.submit(() -> {
				try {
					pessimisticLockStockService.decrease(1L, 1L);
				} finally {
					latch.countDown();
				}
			});
		}
		latch.await();

		Stock stock = stockRepository.findByProductId(1L).orElseThrow();

		assertThat(stock.getQuantity())
				.isEqualTo(0L);
	}

	@Test
	void test4() throws InterruptedException {

		int threadCount = 100;
		ExecutorService executorService = Executors.newFixedThreadPool(32);
		CountDownLatch latch = new CountDownLatch(threadCount);

		for(int i = 0; i < threadCount; i++) {
			executorService.submit(() -> {
				try {
					optimisticLockStockFacade.decrease(1L, 1L);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				} finally {
					latch.countDown();
				}
			});
		}
		latch.await();

		Stock stock = stockRepository.findByProductId(1L).orElseThrow();

		assertThat(stock.getQuantity())
				.isEqualTo(0L);
	}

}
