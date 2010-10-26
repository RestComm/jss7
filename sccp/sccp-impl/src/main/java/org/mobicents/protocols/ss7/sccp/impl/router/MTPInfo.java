/*
 * JBoss, Home of Professional Open Source
 * Copyright XXXX, Red Hat Middleware LLC, and individual contributors as indicated
 * by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */

package org.mobicents.protocols.ss7.sccp.impl.router;

/**
 * Carries the MTP information.
 * 
 * @author kulikov
 */
public class MTPInfo {
    private final static String SEPARATOR = "#";
    
    /** The identifier of MTP-SAP */
    private String name;    
    /** destination point code */
    private int dpc;
    /** originated point code */
    private int opc;
    /** signaling link selection */
    private int sls;
    
    /**
     * Creates new MTPInfo object.
     * 
     * @param name the name of MTP-SAP
     * @param opc the originated point code
     * @param dpc the destination point code
     * @param sls the signaling link selection.
     */
    public MTPInfo(String name, int opc, int dpc, int sls) {
        //name can not be null
        if (name == null) {
            throw new IllegalArgumentException("MTP-SAP can not be NULL");
        }
        
        //remove any extra white spaces
        this.name = name.trim();
        
        //name can not be empty string
        if (this.name.length() == 0) {
            throw new IllegalArgumentException("MTP-SAP name not specified");
        }

        //check and assign originated point code
        if (opc <= 0) {
            throw new IllegalArgumentException("Originated point code less of equal to zero");
        }
        this.opc = opc;
        
        //check and assign destination point code
        if (dpc <= 0) {
            throw new IllegalArgumentException("Destination point code less of equal to zero");
        }
        this.dpc = dpc;
        
        //check and assign signaling link selection
        if (sls < 0) {
            throw new IllegalArgumentException("Signaling link code can not be less then zero");
        }
        this.sls = sls;
    }
    
    /**
     * Gets the name of the MTP-SAP
     * @return
     */
    public String getName() {
        return name;
    }
    
    /**
     * Gets the originated point code used for routing
     * 
     * @return point code in decimal format
     */
    public int getOpc() {
        return opc;
    }
    
    /**
     * Gets the destination point code which will be used for MTP_TRANSFER
     * 
     * @return the point code in decimal format.
     */
    public int getDpc() {
        return dpc;
    }
    
    /**
     * Gets the signaling link selection.
     * 
     * @return
     */
    public int getSls() {
        return sls;
    }
    
    public static MTPInfo getInstance(String s) {
        //use white space as separator
        String[] tokens = s.split(SEPARATOR);
        
        //we are expecting 4 params
        if (tokens.length != 4) {
            throw new IllegalArgumentException("Incorrect format: " + s);
        }
        
        //parse params
        String name = tokens[0];
        int opc = Integer.parseInt(tokens[1]);
        int dpc = Integer.parseInt(tokens[2]);
        int sls = Integer.parseInt(tokens[3]);
        
        //generate new object
        return new MTPInfo(name, opc, dpc, sls);
    }
    
    @Override
    public String toString() {
        StringBuffer buff = new StringBuffer();
        buff.append(name);
        buff.append(SEPARATOR);
        buff.append(opc);
        buff.append(SEPARATOR);
        buff.append(dpc);
        buff.append(SEPARATOR);
        buff.append(sls);
        return buff.toString();
    }
}
