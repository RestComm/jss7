package org.mobicents.ss7;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.m3ua.impl.as.RemSgpImpl;

public class RemSignalingGateway implements Runnable {

    Logger logger = Logger.getLogger(RemSignalingGateway.class);
    private RemSgpImpl remSgpImpl = null;
    private volatile boolean started = false;

    public RemSignalingGateway() {
        // TODO Auto-generated constructor stub
    }

    public RemSgpImpl getRemSgpImpl() {
        return remSgpImpl;
    }

    public void setRemSgpImpl(RemSgpImpl remSgpImpl) {
        this.remSgpImpl = remSgpImpl;
    }

    public void start() {

    }

    public void stop() {
    }

    public void startService() {
        this.started = true;
        (new Thread(this)).start();
    }

    public void stopService() {
        this.started = false;
    }

    public void run() {
        while (this.started) {
            try {
                this.remSgpImpl.perform();
            } catch (IOException e) {
                logger.error("IOException when performing RemSgpImpl", e);
            }
            
            //TODO : Sleep for 1ms? Doesn't make sense
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
            }
        }
    }

}
