package org.mai.auction;

import org.junit.jupiter.api.*;

import java.math.BigDecimal;

public class AuctionImplTest {

    Auction auction;
    String product4thEdition = "Thinking Java 4th edition";
    String product3rdEdition = "Thinking Java 3rd edition";
    BigDecimal defaultPrice = BigDecimal.TEN;

    /*Вызыватется при инициализации класса AuctionImplTest*/
    @BeforeAll
    public static void setupClass(){

    }

    /*Вызыватется перед вызовом каждого метода помеченного аннотацией @Test*/
    @BeforeEach
    public void setup(){
        auction = new AuctionImpl();
        auction.placeProduct(product3rdEdition, defaultPrice);
        auction.placeProduct(product4thEdition, defaultPrice);
    }

    /*Вызыватется после вызова каждого метода помеченного аннотацией @Test*/
    @AfterEach
    public void clear() {
        auction = null;
    }

    /*Вызывается после вызова всех тестовых методов*/
    @AfterAll
    public static void releaseResources() {

    }

    @Test()
    public void placeProduct() {
        var products = auction.getProducts();

        Assertions.assertTrue(products.contains(product3rdEdition));
        Assertions.assertTrue(products.contains(product4thEdition));

        var product5thEdition = "Thinking Java 5th edition";
        Assertions.assertFalse(products.contains(product5thEdition));

        Assertions.assertEquals(defaultPrice, auction.getProductPrice(product3rdEdition));

        Assertions.assertThrows(ProductNotFoundException.class, () -> {
            var notExistingProductPrice = auction.getProductPrice(product5thEdition);
        });
    }

    @Test
    public void addBid() {
        Assertions.fail();
    }

    @Test
    public void removeBid() {
        Assertions.fail();
    }

    @Test
    public void sellProduct() {
        Assertions.fail();
    }
}
