package org.mobicents.protocols.ss7.m3ua.impl;

/**
 * @author OAfanasiev
 */
public interface AspTrafficListener {
    void onAspMessage(String aspName, byte[] data);
}
