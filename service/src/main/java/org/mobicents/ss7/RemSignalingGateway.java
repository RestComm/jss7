/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

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
