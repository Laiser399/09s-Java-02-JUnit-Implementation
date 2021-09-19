package org.mai.auction.exceptions;

public class InvalidBidPriceException extends RuntimeException {
    public InvalidBidPriceException(String message) {
        super(message);
    }
}
