package org.mai.basket;

import org.mai.basket.exceptions.ProductNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BasketImpl implements Basket {
    private final Map<String, Integer> products = new HashMap<>();

    @Override
    public void addProduct(String product, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity lower or equal 0.");
        }

        if (products.containsKey(product)) {
            var currentQuantity = products.get(product);
            products.put(product, currentQuantity + quantity);
        }
        else {
            products.put(product, quantity);
        }
    }

    @Override
    public void removeProduct(String product) {
        products.remove(product);
    }

    @Override
    public void updateProductQuantity(String product, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity lower or equal 0.");
        }
        if (!products.containsKey(product)) {
            throw new ProductNotFoundException("Product \"%s\" not found.".formatted(product));
        }

        products.put(product, quantity);
    }

    @Override
    public List<String> getProducts() {
        return new ArrayList<>(products.keySet());
    }

    @Override
    public int getQuantity(String product) {
        var quantity = products.get(product);

        if (quantity == null) {
            throw new ProductNotFoundException("Product \"%s\" not found.".formatted(product));
        }

        return quantity;
    }
}
