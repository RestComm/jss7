/*
 * Mobicents, Communications Middleware
 * 
 * Copyright (c) 2008, Red Hat Middleware LLC or third-party
 * contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Middleware LLC.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 *
 * Boston, MA  02110-1301  USA
 */
package org.mobicents.protocols.ss7.mtp;

import java.io.IOException;
import java.util.List;

/**
 * Represent the entire Message Transfer Part
 * 
 * @author kulikov
 */
public class MTP {

    /** The name of the link set */
    private String name;
    
    /** Originated point code */
    private int opc;
    
    /** Destination point code */
    private int dpc;
    
    /** physical channels */
    private List<Mtp1> channels;
    
    /** MTP layer 3 */
    private Mtp3 mtp3;
    
    /** Represent MTP user part  */
    private MtpUser mtpUser;
    
    /**
     * Gets the name of the linkset.
     * 
     * @return the name of the linkset
     */
    public String getName() {
        return name;
    }
    
    /**
     * Sets the name of the linkset.
     * 
     * @param name the alhanumeric name of the linkset.
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Assigns originated point code 
     * 
     * @param opc the value of the originated point code in decimal format
     */
    public void setOpc(int opc) {
        this.opc = opc;
    }

    /**
     * Assigns destination point code.
     * 
     * @param dpc the destination point code value in decimal format.
     */
    public void setDpc(int dpc) {
        this.dpc = dpc;
    }
    
    /**
     * Assigns user part.
     * 
     * @param mtpUser the MTP user part.
     */
    public void setUserPart(MtpUser mtpUser) {
        this.mtpUser = mtpUser;
    }
    
    /**
     * Assigns signalling channels.
     * 
     * @param channels the list of available physical channels.
     */
    public void setChannels(List<Mtp1> channels) {
        this.channels = channels;
    }
    
    /**
     * Activates link set.
     */
    public void activate() throws IOException {
        //create mtp layer 3 instance
        mtp3 = new Mtp3(name);
        
        //assigning physical channel
        mtp3.setChannels(channels);
        
        //assigning point codes
        mtp3.setOpc(opc);
        mtp3.setDpc(dpc);
        
        //set user part
        mtp3.setUserPart(mtpUser);
        
        //starting layer 3
        mtp3.start();
    }
    
    /**
     * Deactivates link set.
     */
    public void deactive() {
        mtp3.stop();
    }
}
