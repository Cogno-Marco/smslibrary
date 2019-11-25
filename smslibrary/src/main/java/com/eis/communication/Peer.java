package com.eis.communication;

import com.eis.communication.exception.InvalidPeerException;

/**
 * @author Mattia Fanan
 * abstarcton of peer class
 * @param T type of address
 */
public abstract class Peer<T> {
    protected T address;

    /**
     * build the peer
     * @param address
     * @throws InvalidPeerException if a non valid address is passed
     */
    public Peer(T address)throws InvalidPeerException {
        if(address==null || !isValidAddress(address))
            throw new InvalidPeerException();
        this.address=address;
    }

    /**
     * return the peer's address
     * @return peer's address
     */
    public T getAddress(){
        return address;
    }

    /**
     * set the peer's address if a valid one is passed
     * @param address
     * @return true if address setted correctly
     */
    public boolean setAddress(T address){
        if(address==null || !isValidAddress(address))
            return false;
        this.address=address;
        return true;
    }

    /**
     * method that decides what is a valid address for the peer
     * @param address to validate
     * @return true if is valid
     */
    protected abstract boolean isValidAddress(T address);

    /**
     * check if peer is valid
     * @return true if is valid
     */
    public boolean isValid(){
        return isValidAddress(address);
    }
}
