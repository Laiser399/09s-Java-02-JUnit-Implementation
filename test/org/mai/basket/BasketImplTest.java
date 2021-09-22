package org.mai.basket;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mai.basket.exceptions.ProductNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class BasketImplTest {

    private Basket basket;

    private final String[] products = new String[] {
            "pr1",
            "pr2",
            "pr3",
            "pr4",
            "pr5",
            "pr6",
            "pr7",
    };

    @BeforeEach
    void setUp() {
        basket = new BasketImpl();
    }

    @AfterEach
    void tearDown() {
        basket = null;
    }

    @Test
    void addProduct() {
        assertThrows(IllegalArgumentException.class, () ->
                basket.addProduct(products[0], 0));
        assertThrows(IllegalArgumentException.class, () ->
                basket.addProduct(products[0], -1));

        addSomeProducts();
    }

    @Test
    void removeProduct() {
        basket.removeProduct(products[0]);

        addSomeProducts();

        basket.removeProduct(products[1]);
        var basketProducts = basket.getProducts();
        assertEquals(2, basketProducts.size());
        assertTrue(basketProducts.contains(products[0]));
        assertTrue(basketProducts.contains(products[2]));
    }

    @Test
    void updateProductQuantity() {
        assertThrows(ProductNotFoundException.class, () ->
                basket.updateProductQuantity(products[0], 1));

        addSomeProducts();

        assertThrows(IllegalArgumentException.class, () ->
                basket.updateProductQuantity(products[0], 0));
        assertThrows(IllegalArgumentException.class, () ->
                basket.updateProductQuantity(products[0], -1));

        assertEquals(6, basket.getQuantity(products[0]));
        basket.updateProductQuantity(products[0], 2);
        assertEquals(2, basket.getQuantity(products[0]));

        assertEquals(10, basket.getQuantity(products[1]));
        basket.updateProductQuantity(products[1], 10);
        assertEquals(10, basket.getQuantity(products[1]));
    }

    @Test
    void getProducts() {
        var basketProducts = basket.getProducts();
        assertEquals(0, basketProducts.size());

        addSomeProducts();

        basketProducts = basket.getProducts();
        assertEquals(3, basketProducts.size());
        assertTrue(basketProducts.contains(products[0]));
        assertTrue(basketProducts.contains(products[1]));
        assertTrue(basketProducts.contains(products[2]));
    }

    @Test
    void getQuantity() {
        assertThrows(ProductNotFoundException.class, () ->
                basket.getQuantity(products[6]));

        addSomeProducts();

        assertEquals(6, basket.getQuantity(products[0]));
        assertEquals(10, basket.getQuantity(products[1]));
        assertEquals(1, basket.getQuantity(products[2]));
    }

    private void addSomeProducts() {
        basket.addProduct(products[0], 1);
        basket.addProduct(products[0], 3);
        basket.addProduct(products[0], 2);
        basket.addProduct(products[1], 10);
        basket.addProduct(products[2], 1);
    }
}