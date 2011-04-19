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

package org.mobicents.protocols.ss7.mtp.dialogic;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ConfigurationException;
import org.mobicents.protocols.StartFailedException;
//import org.mobicents.protocols.ss7.hardware.dialogic.InterProcessCommunicator;
import org.mobicents.protocols.ss7.mtp.Utils;
import org.mobicents.protocols.ss7.mtp.provider.MtpListener;
import org.mobicents.protocols.ss7.mtp.provider.MtpProvider;

/**
 * Implements Mtp level in case of direct access Dialogic signaling hardware.
 * 
 * @author kulikov
 */
public class MtpImpl implements MtpProvider, Runnable {

    private String linksetName;
    
    //communicator with hardware
   // private InterProcessCommunicator ipc; 
    
    //Dialogic modules
    private int src = 61;
    private int dst = 34;
    
    private volatile boolean stopped = false;
    
    //originated and adjacent point codes
    private int opc;
    private int dpc;
    private int ni;
    //Message listener
    private MtpListener listener;
    
    //working thread instance
    private Thread thread;
    
    //Logger instance
    private Logger logger = Logger.getLogger(MtpImpl.class);

    public MtpImpl() {
    }
    
    public String getName() {
        return linksetName;
    }
    
    public void setName(String linksetName) {
        this.linksetName = linksetName;
    }
    
    /**
     * Modify dialogic source module.
     * 
     * @param src the source module identifier.
     */
    public void setSourceModule(int src) {
        this.src = src;
    }
    
    /**
     * Current dialogic source module.
     * 
     * @return the source module identifier.
     */
    public int getSourceModule() {
        return src;
    }
    
    /**
     * Modify dialogic destination module.
     * 
     * @param dst the destination module identifier.
     */
    public void setDestinationModule(int dst) {
        this.dst = dst;
    }
    
    /**
     * Current dialogic destination module.
     * 
     * @return the destination module identifier.
     */
    public int getDestinationModule() {
        return dst;
    }
    
    /**
     * (Non Java-doc.)
     * 
     * @see org.mobicents.protocols.ss7.mtp.provider.MtpProvider#setOriginalPointCode(int) 
     */
    public void setOriginalPointCode(int opc) {
        this.opc = opc;
    }

    /**
     * (Non Java-doc.)
     * 
     * @see org.mobicents.protocols.ss7.mtp.provider.MtpProvider#setAdjacentPointCode(int) 
     */
    public void setAdjacentPointCode(int dpc) {
        this.dpc = dpc;
    }

    /**
     * (Non Java-doc.)
     * 
     * @see org.mobicents.protocols.ss7.mtp.provider.MtpProvider#getAdjacentPointCode() 
     */
    public int getAdjacentPointCode() {
        return dpc;
    }
    
    /**
     * (Non Java-doc.)
     * 
     * @see org.mobicents.protocols.ss7.mtp.provider.MtpProvider#getOriginalPointCode()  
     */
    public int getOriginalPointCode() {
        return opc;
    }

	public void setNetworkIndicator(int ni) {
		this.ni = ni;
	}

	public int getNetworkIndicator() {
		return this.ni;
	}

	/**
     * (Non Java-doc.)
     * 
     * @see org.mobicents.protocols.ss7.mtp.provider.MtpProvider#setMtpListener(org.mobicents.protocols.ss7.mtp.provider.MtpListener) 
     */
    public void setMtpListener(MtpListener lst) {
        this.listener = lst;
    }

    
    /**
     * (Non Java-doc.)
     * 
     * @see org.mobicents.protocols.ss7.mtp.provider.MtpProvider#send(byte[]) 
     */
    public void send(byte[] msu) throws IOException {
        //write message to the hardware
       // ipc.send(msu);
    }

    /**
     * (Non Java-doc.)
     * 
     * @see org.mobicents.protocols.ss7.mtp.provider.MtpProvider#start() 
     */
    public void start() throws StartFailedException {
        logger.info(String.format("Starting linkset :[opc=%d, adjacent=%d]", opc, dpc));
        
       // ipc = new InterProcessCommunicator(src, dst);
        logger.info(String.format("Succesfully accessed hardware:[opc=%d, adjacent=%d]", opc, dpc));
        
        this.stopped = false;
        
        thread = new Thread(this);
        thread.start();
    }

    /**
     * (Non Java-doc.)
     * 
     * @see org.mobicents.protocols.ss7.mtp.provider.MtpProvider#stop() 
     */
    public void stop() {
        logger.info("Stopping linkset: ");
        stopped = true;
        thread.interrupt();
    }

    /**
     * (Non Java-doc.)
     * 
     * @see org.mobicents.protocols.ss7.mtp.provider.MtpProvider#configure(java.util.Properties) 
     */
    public void configure(Properties p) throws ConfigurationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * (Non Java-doc.)
     * 
     * @see org.mobicents.protocols.ss7.mtp.provider.MtpProvider#isLinkUp() 
     */
    public boolean isLinkUp() {
        return true;
    }

    /**
     * Receiver loop
     */
    public void run() {
        while (!stopped) {
            try {
                if (logger.isTraceEnabled()) {
                    logger.trace("Waiting for packet delivery");
                }

                //receive message signal unit from harware
               byte[] msu = null; //ipc.receive();
                logger.info("Packet received!!!!" + msu.length );
                if (logger.isTraceEnabled()) {
                    logger.trace(Utils.hexDump("Packet received\n", msu));
                }
                
                //deliver message to the listener
                if (listener != null) {
                    listener.receive(msu);
                }
            } catch (Exception e) {
                logger.error("I/O error occured while reading data from MTP3 driver", e);
            }
        }
    }
}
