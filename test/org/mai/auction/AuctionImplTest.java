package org.mai.auction;

import org.junit.jupiter.api.*;
import org.mai.auction.exceptions.InvalidBidPriceException;

import java.math.BigDecimal;

public class AuctionImplTest {
    Auction auction;

    String product3rdEdition = "Thinking Java 3rd edition";
    String product4thEdition = "Thinking Java 4th edition";
    String unknownProduct = "WTF";

    String firstUserName = "1st User";
    String secondUserName = "2nd User";

    BigDecimal underInitialPrice = BigDecimal.ONE;
    BigDecimal initialPrice = BigDecimal.TEN;
    BigDecimal priceLow = new BigDecimal("20.23");
    BigDecimal priceMedium = new BigDecimal("55.55");
    BigDecimal priceBig = new BigDecimal("1226.01");

    /*Вызыватется при инициализации класса AuctionImplTest*/
    @BeforeAll
    public static void setupClass(){

    }

    /*Вызыватется перед вызовом каждого метода помеченного аннотацией @Test*/
    @BeforeEach
    public void setup(){
        auction = new AuctionImpl();
        auction.placeProduct(product3rdEdition, initialPrice);
        auction.placeProduct(product4thEdition, initialPrice);
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
        Assertions.assertFalse(products.contains(unknownProduct));

        Assertions.assertEquals(initialPrice, auction.getProductPrice(product3rdEdition));

        Assertions.assertThrows(ProductNotFoundException.class, () -> {
            var notExistingProductPrice = auction.getProductPrice(unknownProduct);
        });
    }

    @Test
    public void addBid() {
        auction.addBid(firstUserName, product3rdEdition, priceMedium);
        auction.addBid(secondUserName, product3rdEdition, priceLow);
        Assertions.assertEquals(priceMedium, auction.getProductPrice(product3rdEdition));

        Assertions.assertEquals(initialPrice, auction.getProductPrice(product4thEdition));
        auction.addBid(firstUserName, product4thEdition, priceBig);
        Assertions.assertEquals(initialPrice, auction.getProductPrice(product4thEdition));

        Assertions.assertThrows(InvalidBidPriceException.class, () ->
                auction.addBid(firstUserName, product3rdEdition, underInitialPrice));
    }

    @Test
    public void removeBid() {
        // последующий тест покрывает логику текущего?
        Assertions.assertEquals(initialPrice, auction.getProductPrice(product3rdEdition));
        auction.addBid(firstUserName, product3rdEdition, priceLow);
        Assertions.assertEquals(priceLow, auction.getProductPrice(product3rdEdition));
        auction.removeBid(firstUserName, product3rdEdition);
        Assertions.assertEquals(initialPrice, auction.getProductPrice(product3rdEdition));

        Assertions.assertEquals(initialPrice, auction.getProductPrice(product4thEdition));
        auction.addBid(secondUserName, product4thEdition, priceMedium);
        Assertions.assertEquals(priceMedium, auction.getProductPrice(product4thEdition));
        auction.addBid(secondUserName, product4thEdition, priceBig);
        Assertions.assertEquals(priceBig, auction.getProductPrice(product4thEdition));
        auction.removeBid(secondUserName, product4thEdition);
        Assertions.assertEquals(initialPrice, auction.getProductPrice(product4thEdition));
    }

    @Test
    public void sellProduct() {
        var noBidsSellRes = auction.sellProduct(product3rdEdition);
        Assertions.assertFalse(noBidsSellRes);

        auction.addBid(firstUserName, product3rdEdition, priceLow);

        Assertions.assertTrue(auction.getProducts().contains(product3rdEdition));
        var validSellRes = auction.sellProduct(product3rdEdition);
        Assertions.assertTrue(validSellRes);

        var invalidSellRes = auction.sellProduct(product3rdEdition);
        Assertions.assertFalse(invalidSellRes);
        Assertions.assertFalse(auction.getProducts().contains(product3rdEdition));
    }
}
