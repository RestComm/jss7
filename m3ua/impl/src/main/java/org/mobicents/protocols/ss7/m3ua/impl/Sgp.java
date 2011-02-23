package org.mobicents.protocols.ss7.m3ua.impl;

import java.io.IOException;

import org.mobicents.protocols.ss7.m3ua.message.transfer.PayloadData;

/**
 * A process instance of a Signalling Gateway (SGP). It serves as an active,
 * backup, load-sharing, or broadcast process of a Signalling Gateway.
 * 
 * @author amit bhayani
 * 
 */
public interface Sgp {

    /**
     * Start this Signalling Gateway Process
     * 
     * @throws IOException
     */
    public void start() throws IOException;

    /**
     * Stop this Signalling Gateway Process
     */
    public void stop();

    /**
     * Send the payload data to peer. The SGP process should find appropriate
     * {@link As} depending on the DPC / OPC / SI included in this payload
     * 
     * @param msu
     * @throws Exception
     */
    public void send(byte[] msu) throws Exception;

    /**
     * Poll this SGP and retrieve the {@link PayloadData} from all the
     * {@link As} that this SGP represents
     * 
     * @return
     */
    public PayloadData poll();

    /**
     * Create new {@link As}
     * 
     * @param args
     * @return
     * @throws Exception
     */
    public As createAppServer(String args[]) throws Exception;

    /**
     * Create new {@link AspFactory}
     * 
     * @param args
     * @return
     * @throws Exception
     */
    public AspFactory createAspFactory(String[] args) throws Exception;

    /**
     * Associate {@link Asp} to {@link As}
     * 
     * @param asName
     * @param aspName
     * @return
     * @throws Exception
     */
    public Asp assignAspToAs(String asName, String aspName) throws Exception;

}
