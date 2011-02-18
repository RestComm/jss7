package org.mobicents.protocols.ss7.m3ua.impl;

import java.io.IOException;

import org.mobicents.protocols.ss7.m3ua.message.transfer.PayloadData;

public interface SigGateway {

    public void start() throws IOException;

    public void stop();

    public void send(byte[] msu) throws Exception;

    public PayloadData poll();

}
