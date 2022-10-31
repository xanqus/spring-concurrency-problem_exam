package com.example.stock.domain;

import javax.persistence.*;
import javax.transaction.Transactional;

@Entity
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;

    private Long quantity;

    @Version
    private Long version;

    public Stock() {

    }

    public Stock(Long productId, Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getQuantity() {
        return quantity;
    }


    public void decrease(Long quantity) {
        if (this.quantity - quantity < 0) {
            throw new RuntimeException("stock이 0 이하입니다.");
        }

        this.quantity -= quantity;
    }
}
