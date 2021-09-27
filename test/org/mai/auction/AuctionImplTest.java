package org.mai.auction;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mai.auction.exceptions.DuplicateBidPriceException;
import org.mai.auction.exceptions.InvalidBidPriceException;
import org.mai.auction.exceptions.ProductNotFoundException;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

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

        assertThat(products, containsInAnyOrder(product3rdEdition, product4thEdition));

        assertThat(auction.getProductPrice(product3rdEdition), equalTo(initialPrice));

        Assertions.assertThrows(ProductNotFoundException.class, () -> {
            var notExistingProductPrice = auction.getProductPrice(unknownProduct);
        });

        auction.placeProduct(product3rdEdition, priceLow);
        assertThat(auction.getProductPrice(product3rdEdition), equalTo(priceLow));
    }

    @Test
    public void addBid() throws DuplicateBidPriceException {
        auction.addBid(firstUserName, product3rdEdition, priceMedium);
        auction.addBid(secondUserName, product3rdEdition, priceLow);
        assertThat(auction.getProductPrice(product3rdEdition), equalTo(priceMedium));

        assertThat(auction.getProductPrice(product4thEdition), equalTo(initialPrice));
        auction.addBid(firstUserName, product4thEdition, priceBig);
        assertThat(auction.getProductPrice(product4thEdition), equalTo(priceBig));

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
        assertThat(auction.getProductPrice(product3rdEdition), equalTo(initialPrice));
        auction.addBid(firstUserName, product3rdEdition, priceLow);
        assertThat(auction.getProductPrice(product3rdEdition), equalTo(priceLow));
        auction.removeBid(firstUserName, product3rdEdition);
        assertThat(auction.getProductPrice(product3rdEdition), equalTo(initialPrice));

        assertThat(auction.getProductPrice(product4thEdition), equalTo(initialPrice));
        auction.addBid(secondUserName, product4thEdition, priceMedium);
        assertThat(auction.getProductPrice(product4thEdition), equalTo(priceMedium));
        auction.addBid(secondUserName, product4thEdition, priceBig);
        assertThat(auction.getProductPrice(product4thEdition), equalTo(priceBig));
        auction.removeBid(secondUserName, product4thEdition);
        assertThat(auction.getProductPrice(product4thEdition), equalTo(initialPrice));
    }

    @Test
    public void sellProduct() throws DuplicateBidPriceException {
        var noBidsSellRes = auction.sellProduct(product3rdEdition);
        assertThat(noBidsSellRes, equalTo(false));

        auction.addBid(firstUserName, product3rdEdition, priceLow);

        assertThat(auction.getProducts(), hasItem(product3rdEdition));
        var validSellRes = auction.sellProduct(product3rdEdition);
        assertThat(validSellRes, equalTo(true));

        var invalidSellRes = auction.sellProduct(product3rdEdition);
        assertThat(invalidSellRes, equalTo(false));
        assertThat(auction.getProducts(), not(hasItem(product3rdEdition)));
    }

    @Test
    public void getProducts() {
        var products = auction.getProducts();
        assertThat(products, containsInAnyOrder(product3rdEdition, product4thEdition));

        auction.placeProduct(product5th, initialPrice);

        products = auction.getProducts();
        assertThat(products, containsInAnyOrder(product3rdEdition, product4thEdition, product5th));
    }

    @Test
    public void getProductPrice() throws DuplicateBidPriceException {
        assertThat(auction.getProductPrice(product3rdEdition), equalTo(initialPrice));

        Assertions.assertThrows(ProductNotFoundException.class, () ->
                auction.getProductPrice(unknownProduct));

        auction.addBid(firstUserName, product3rdEdition, priceLow);
        assertThat(auction.getProductPrice(product3rdEdition), equalTo(priceLow));
        auction.addBid(secondUserName, product3rdEdition, priceMedium);
        assertThat(auction.getProductPrice(product3rdEdition), equalTo(priceMedium));
        auction.removeBid(firstUserName, product3rdEdition);
        auction.removeBid(secondUserName, product3rdEdition);
        assertThat(auction.getProductPrice(product3rdEdition), equalTo(initialPrice));
    }

    private void placeCommonProducts() {
        auction.placeProduct(product3rdEdition, initialPrice);
        auction.placeProduct(product4thEdition, initialPrice);
    }
}
