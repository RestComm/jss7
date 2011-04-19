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

package org.mobicents.protocols.ss7.m3ua.impl.parameter;

import org.mobicents.protocols.ss7.m3ua.parameter.ProtocolData;

/**
 * Implements Protocol Data parameter.
 * 
 * @author amit bhayani
 * @author kulikov
 */
public class ProtocolDataImpl extends ParameterImpl implements ProtocolData {

    // FIXME: Oleg why the hell this class does not reuse mtp.util class to
    // manipulate RL ?
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

    protected ProtocolDataImpl(int opc, int dpc, int si, int ni, int mp,
            int sls, byte[] data) {
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
    
    /**
     * Creates new parameter with specified value.
     * 
     * @param value
     *            the value of this parameter
     */
    protected ProtocolDataImpl(byte[] value) {
        this.tag = ParameterImpl.Protocol_Data;
        opc = ((value[0] & 0xff) << 24) | ((value[1] & 0xff) << 16)
                | ((value[2] & 0xff) << 8) | (value[3] & 0xff);
        dpc = ((value[4] & 0xff) << 24) | ((value[5] & 0xff) << 16)
                | ((value[6] & 0xff) << 8) | (value[7] & 0xff);

        si = value[8] & 0xff;
        ni = value[9] & 0xff;
        mp = value[10] & 0xff;
        sls = value[11] & 0xff;

        data = new byte[value.length - 12];
        System.arraycopy(value, 12, data, 0, value.length - 12);
    }    

    protected void load(byte[] msu) {
        this.data = new byte[msu.length - 5];
        this.ni = (msu[0] & 0xc0) >> 6;
        this.mp = (msu[0] & 0x30) >> 4;
        this.si = msu[0] & 0x0f;
        this.dpc = (msu[1] & 0xff | ((msu[2] & 0x3f) << 8));
        this.opc = ((msu[2] & 0xC0) >> 6) | ((msu[3] & 0xff) << 2)
                | ((msu[4] & 0x0f) << 10);
        this.sls = (msu[4] & 0xf0) >> 4;
        System.arraycopy(msu, 5, data, 0, data.length);
        encode();
    }

    private void encode() {
        // create byte array taking into account data, point codes and
        // indicators;
        this.value = new byte[data.length + 12];
        // insert data
        System.arraycopy(data, 0, value, 12, data.length);

        // encode originated point codes
        value[0] = (byte) (opc >> 24);
        value[1] = (byte) (opc >> 16);
        value[2] = (byte) (opc >> 8);
        value[3] = (byte) (opc);

        // encode destination point code
        value[4] = (byte) (dpc >> 24);
        value[5] = (byte) (dpc >> 16);
        value[6] = (byte) (dpc >> 8);
        value[7] = (byte) (dpc);

        // encode indicators
        value[8] = (byte) (si);
        value[9] = (byte) (ni);
        value[10] = (byte) (mp);
        value[11] = (byte) (sls);
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
        // create extended byte array
        byte[] msu = new byte[data.length + 5];

        // encode service information octet and routing label
        int ssi = ni << 2;
        msu[0] = (byte) (((ssi & 0x0F) << 4) | (si & 0x0F));
        msu[1] = (byte) dpc;
        msu[2] = (byte) (((dpc >> 8) & 0x3F) | ((opc & 0x03) << 6));
        msu[3] = (byte) (opc >> 2);
        msu[4] = (byte) (((opc >> 10) & 0x0F) | ((sls & 0x0F) << 4));

        // copy data
        System.arraycopy(data, 0, msu, 5, data.length);
        return msu;
    }

    @Override
    public String toString() {
        return String.format(
                "Protocol opc=%d dpc=%d si=%d ni=%d sls=%d", opc,
                dpc, si, ni, sls);
    }

}
