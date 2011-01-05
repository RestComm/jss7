package org.mobicents.protocols.ss7.mtp;

public interface Mtp2Listener {
    
    public void linkFailed();
    public void linkInService();
    public void linkUp();

}
