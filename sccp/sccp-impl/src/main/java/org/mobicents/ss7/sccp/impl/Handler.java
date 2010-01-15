/*
 * The Java Call Control API for CAMEL 2
 *
 * The source code contained in this file is in in the public domain.
 * It can be used in any project or product without prior permission,
 * license or royalty payments. There is  NO WARRANTY OF ANY KIND,
 * EXPRESS, IMPLIED OR STATUTORY, INCLUDING, WITHOUT LIMITATION,
 * THE IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE,
 * AND DATA ACCURACY.  We do not warrant or make any representations
 * regarding the use of the software or the  results thereof, including
 * but not limited to the correctness, accuracy, reliability or
 * usefulness of the software.
 */

package org.mobicents.ss7.sccp.impl;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import org.apache.log4j.Logger;
import org.mobicents.ss7.sccp.SccpListener;
import org.mobicents.ss7.sccp.SccpProvider;
import org.mobicents.ss7.sccp.impl.ud.UnitDataImpl;
import org.mobicents.ss7.sccp.impl.ud.XUnitDataImpl;

/**
 *
 * @author Oleg Kulikov
 */
public class Handler implements Runnable {
    
    private SccpProvider provider = null;
    private byte[] packet;
    
    private Logger logger = Logger.getLogger(Handler.class);
    
    /** Creates a new instance of Handler */
    public Handler(SccpProvider provider, byte[] packet) {
        this.provider = provider;
        this.packet = packet;
    }
    
    public void run() {
        ByteArrayInputStream bin = new ByteArrayInputStream(packet);
        DataInputStream in = new DataInputStream(bin);
        
        try {
            int o = in.readUnsignedByte();
            
            int ssf = o & 0xf0;
            int si = o & 0x0f;
            
            int b1 = in.readUnsignedByte();
            int b2 = in.readUnsignedByte();
            int b3 = in.readUnsignedByte();
            int b4 = in.readUnsignedByte();
            
            int dpc = ((b2 & 0x3f) << 8) | b1;
            int opc = ((b4 & 0x0f) << 10) | (b3 << 2) | (b1 & 0xc0);
            int sls = (b4 & 0xf0);
            
            int mt = in.readUnsignedByte();
            switch (mt) {
            case UnitDataImpl._MT:
            	 UnitDataImpl unitData = new UnitDataImpl();
                 unitData.decode(in);
                 
                 synchronized(this) {
                     SccpListener listener = provider.getListener();
                     if (listener != null) {
                         listener.onMessage(unitData.getCalledParty(),
                                 unitData.getCallingParty(), unitData.getData());
                     }
                 }
				//0x11
			case XUnitDataImpl._MT:
				XUnitDataImpl xunitData = new XUnitDataImpl();
				xunitData.decode(in);
				
				synchronized(this) {
                    SccpListener listener = provider.getListener();
                    if (listener != null) {
                        listener.onMessage(xunitData.getCalledParty(),
                        		xunitData.getCallingParty(), xunitData.getData());
                    }
                }
				break;
                default :
                    logger.warn("Unsupported message type: " + mt);
            }
        } catch (IOException e) {
            System.err.println(e);
        }
        
    }
    
}
