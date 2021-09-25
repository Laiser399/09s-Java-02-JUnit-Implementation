package org.mai.auction;

import org.junit.jupiter.api.*;
import org.mai.auction.exceptions.DuplicateBidPriceException;
import org.mai.auction.exceptions.InvalidBidPriceException;
import org.mai.auction.exceptions.ProductNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

public class AuctionImplTest {
    Auction auction;

    String product3rdEdition = "Thinking Java 3rd edition";
    String product4thEdition = "Thinking Java 4th edition";
    String product5th = "5th product";
    String unknownProduct = "WTF";

    String firstUserName = "1st User";
    String secondUserName = "2nd User";

    BigDecimal underInitialPrice = BigDecimal.ONE;
    BigDecimal initialPrice = BigDecimal.TEN;
    BigDecimal priceLow = new BigDecimal("20.23");
    BigDecimal priceMedium = new BigDecimal("55.55");
    BigDecimal priceBig = new BigDecimal("1226.01");

    @BeforeEach
    public void setup() {
        auction = new AuctionImpl();

        placeCommonProducts();
    }

    @AfterEach
    public void clear() {
        auction = null;
    }

    @Test
    public void placeProduct() {
        var products = auction.getProducts();

        Assertions.assertTrue(products.contains(product3rdEdition));
        Assertions.assertTrue(products.contains(product4thEdition));
        Assertions.assertFalse(products.contains(unknownProduct));

        Assertions.assertEquals(initialPrice, auction.getProductPrice(product3rdEdition));

        Assertions.assertThrows(ProductNotFoundException.class, () -> {
            var notExistingProductPrice = auction.getProductPrice(unknownProduct);
        });

        auction.placeProduct(product3rdEdition, priceLow);
        Assertions.assertEquals(priceLow, auction.getProductPrice(product3rdEdition));
    }

    @Test
    public void addBid() throws DuplicateBidPriceException {
        auction.addBid(firstUserName, product3rdEdition, priceMedium);
        auction.addBid(secondUserName, product3rdEdition, priceLow);
        Assertions.assertEquals(priceMedium, auction.getProductPrice(product3rdEdition));

        Assertions.assertEquals(initialPrice, auction.getProductPrice(product4thEdition));
        auction.addBid(firstUserName, product4thEdition, priceBig);
        Assertions.assertEquals(priceBig, auction.getProductPrice(product4thEdition));

        Assertions.assertThrows(InvalidBidPriceException.class, () ->
                auction.addBid(firstUserName, product3rdEdition, underInitialPrice));

        Assertions.assertThrows(ProductNotFoundException.class, () ->
                auction.addBid(firstUserName, unknownProduct, priceLow));

        auction.addBid(firstUserName, product4thEdition, priceBig);
        Assertions.assertThrows(DuplicateBidPriceException.class, () ->
                auction.addBid(secondUserName, product4thEdition, priceBig));
    }

    @Test
    public void removeBid() throws DuplicateBidPriceException {
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
    public void sellProduct() throws DuplicateBidPriceException {
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

    @Test
    public void getProducts() {
        var products = auction.getProducts();
        assertEquals(2, products.size());
        assertTrue(products.contains(product3rdEdition));
        assertTrue(products.contains(product4thEdition));

        auction.placeProduct(product5th, initialPrice);

        products = auction.getProducts();
        assertEquals(3, products.size());
        assertTrue(products.contains(product3rdEdition));
        assertTrue(products.contains(product4thEdition));
        assertTrue(products.contains(product5th));
    }

    @Test
    public void getProductPrice() throws DuplicateBidPriceException {
        assertEquals(initialPrice, auction.getProductPrice(product3rdEdition));

        assertThrows(ProductNotFoundException.class, () ->
                auction.getProductPrice(unknownProduct));

        auction.addBid(firstUserName, product3rdEdition, priceLow);
        assertEquals(priceLow, auction.getProductPrice(product3rdEdition));
        auction.addBid(secondUserName, product3rdEdition, priceMedium);
        assertEquals(priceMedium, auction.getProductPrice(product3rdEdition));
        auction.removeBid(firstUserName, product3rdEdition);
        auction.removeBid(secondUserName, product3rdEdition);
        assertEquals(initialPrice, auction.getProductPrice(product3rdEdition));
    }

    private void placeCommonProducts() {
        auction.placeProduct(product3rdEdition, initialPrice);
        auction.placeProduct(product4thEdition, initialPrice);
    }
}
