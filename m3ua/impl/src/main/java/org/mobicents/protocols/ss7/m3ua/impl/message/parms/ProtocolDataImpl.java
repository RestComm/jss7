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


package org.mobicents.protocols.ss7.m3ua.impl.message.parms;

import org.mobicents.protocols.ss7.m3ua.message.parm.*;
import org.mobicents.protocols.ss7.m3ua.impl.message.ParameterImpl;

/**
 * Implements Protocol Data parameter.
 * 
 * @author kulikov
 */
public class ProtocolDataImpl extends ParameterImpl implements ProtocolData {
    private int opc;
    private int dpc;
    private int si;
    private int ni;
    private int mp;
    private int sls;
    private byte[] data;
    
    private byte[] value;

    protected ProtocolDataImpl() {
        this.tag = ParameterImpl.Protocol_Data;
    }    
    protected ProtocolDataImpl(int opc, int dpc, int si, int ni, int mp, int sls, byte[] data) {
        this.tag = ParameterImpl.Protocol_Data;
        this.opc = opc;
        this.dpc = dpc;
        this.si = si;
        this.ni = ni;
        this.mp = mp;
        this.sls = sls;
        this.data = data;
        encode();
    }
    
    protected void load(byte[] msu) {
        this.data = new byte[msu.length - 5];
        this.ni = (msu[0] & 0xc0) >> 6;
        this.mp = (msu[0] & 0x30) >> 4;
        this.si = msu[0] & 0x0f;
        this.opc = ((msu[2] & 0xC0) >> 6) | ((msu[3] & 0xff) << 2) | ((msu[4] & 0x0f) << 10);
        this.dpc = (msu[1] & 0xff | ((msu[2] & 0x3f) << 8));
        System.arraycopy(msu, 5, data, 0, data.length);
        encode();
    }   
    
    private void encode() {
        //create byte array taking into account data, point codes and indicators;
        this.value = new byte[data.length + 12];
        //insert data 
        System.arraycopy(data, 0, value, 12, data.length);
        
        //encode originated point codes
        value[0] = (byte)(opc >> 24);
        value[1] = (byte)(opc >> 16);
        value[2] = (byte)(opc >> 8);
        value[3] = (byte)(opc);
        
        //encode destination point code
        value[4] = (byte)(dpc >> 24);
        value[5] = (byte)(dpc >> 16);
        value[6] = (byte)(dpc >> 8);
        value[7] = (byte)(dpc);
        
        //encode indicators
        value[8] = (byte)(si);
        value[9] = (byte)(ni);
        value[10] = (byte)(mp);
        value[11] = (byte)(sls);
    }
    /**
     * Creates new parameter with specified value.
     * 
     * @param value the value of this parameter
     */
    protected ProtocolDataImpl(byte[] value) {
        this.tag = ParameterImpl.Protocol_Data;
        opc = ((value[0] & 0xff) << 24) | ((value[1] & 0xff) << 16) | ((value[2] & 0xff) << 8) | (value[3] & 0xff);
        dpc = ((value[4] & 0xff) << 24) | ((value[5] & 0xff) << 16) | ((value[6] & 0xff) << 8) | (value[7] & 0xff);
        
        si = value[8] & 0xff;
        ni = value[9] & 0xff;
        mp = value[10] & 0xff;
        sls = value[11] & 0xff;
        
        data = new byte[value.length - 12];
        System.arraycopy(value, 12, data, 0, value.length - 12);
    }   
    
    public int getOpc() {
        return opc;
    }
    
    public int getDpc() {
        return dpc;
    }
    
    public int getSI() {
        return si;
    }
    
    public int getNI() {
        return ni;
    }
    
    public int getMP() {
        return mp;
    }
    
    public int getSLS() {
        return sls;
    }

    public byte[] getData() {
        return data;
    }
    
    @Override
    protected byte[] getValue() {
        return value;
    }

    public byte[] getMsu() {
        //create extended byte array
        byte[] msu = new byte[data.length + 5];
        
        //encode service information octet and routing label
        int ssi = ni << 2;
	msu[0] = (byte) (((ssi & 0x0F) << 4) | (si & 0x0F));
	msu[1] = (byte) dpc;
	msu[2] = (byte) (((dpc >> 8) & 0x3F) | ((opc & 0x03) << 6));
	msu[3] = (byte) (opc >> 2);
	msu[4] = (byte) (((opc >> 10) & 0x0F) | ((sls & 0x0F) << 4));
        
        //copy data
        System.arraycopy(data, 0, msu, 5, data.length);
        return msu;
    }
    
    @Override
    public String toString() {
        return String.format("Protocol data: opc=%d, dpc=%d, si=%d, ni=%d, sls=%d", opc, dpc, si, ni, sls);
    }

}
