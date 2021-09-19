package org.mai.auction;

import org.mai.auction.exceptions.DuplicateBidPriceException;
import org.mai.auction.exceptions.InvalidBidPriceException;
import org.mai.auction.exceptions.ProductNotFoundException;

import java.math.BigDecimal;
import java.util.*;


public class AuctionImpl implements Auction {
    private final Map<String, BigDecimal> initialPrices;// product -> initialPrice
    private final Map<String, Map<String, BigDecimal>> bids;// product -> user -> bidPrice

    public AuctionImpl() {
        initialPrices = new HashMap<>();
        bids = new HashMap<>();
    }

    @Override
    public void placeProduct(String product, BigDecimal initialPrice) {
        initialPrices.put(product, initialPrice);
    }

    @Override
    public void addBid(String user, String product, BigDecimal price) throws DuplicateBidPriceException {
        var initialPrice = initialPrices.get(product);

        if (initialPrice == null) {
            throw new ProductNotFoundException("Product with name %s not found in auction".formatted(product));
        }
        if (price.compareTo(initialPrice) < 0) {
            throw new InvalidBidPriceException("Bid price is lower than initial price.");
        }

        var currentBids = bids.get(product);

        if (currentBids == null) {
            currentBids = new TreeMap<>();
            currentBids.put(user, price);
            bids.put(product, currentBids);
        }
        else {
            var currentUserPrice = currentBids.get(user);
            if (currentUserPrice != null && currentUserPrice.equals(price)) {
                return;
            }
            if (currentBids.containsValue(price)) {
                throw new DuplicateBidPriceException();
            }

            currentBids.put(user, price);
        }
    }

    @Override
    public void removeBid(String user, String product) {
        var productBids = bids.get(product);
        if (productBids == null) {
            return;
        }

        productBids.remove(user);
    }

    @Override
    public boolean sellProduct(String product) {
        if (!initialPrices.containsKey(product)) {
            return false;
        }

        if (!bids.containsKey(product) || bids.get(product).isEmpty()) {
            return false;
        }

        initialPrices.remove(product);
        bids.remove(product);
        return true;
    }

    @Override
    public List<String> getProducts() {
        return new ArrayList<>(initialPrices.keySet());
    }

    @Override
    public BigDecimal getProductPrice(String product) {
        var initialPrice = initialPrices.get(product);

        if (initialPrice == null) {
            throw new ProductNotFoundException("Product with name %s not found in auction".formatted(product));
        }

        var productBids = bids.get(product);
        if (productBids != null && !productBids.isEmpty()) {
            return productBids.values().stream().max(BigDecimal::compareTo).get();
        }

        return initialPrice;
    }
}
