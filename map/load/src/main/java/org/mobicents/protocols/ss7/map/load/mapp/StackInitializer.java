package org.mobicents.protocols.ss7.map.load.mapp;


public interface StackInitializer  {
    String getStackProtocol();
    void init(MAPpContext ctx) throws Exception;
}
