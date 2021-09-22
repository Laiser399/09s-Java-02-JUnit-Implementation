package org.mai.basket;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BasketImpl implements Basket {
    private final Map<String, Integer> products = new HashMap<>();

    @Override
    public void addProduct(String product, int quantity) {

    }

    @Override
    public void removeProduct(String product) {

    }

    @Override
    public void updateProductQuantity(String product, int quantity) {

    }

    @Override
    public List<String> getProducts() {
        return null;
    }

    @Override
    public int getQuantity(String product) {
        return -1;
    }
}
