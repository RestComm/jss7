/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.mobicents.protocols.ss7.mtp.util;

/**
 * Small class to define methods of use to o
 *
 * @author baranowb
 *
 */
public class MTPUtility {

    /**
     * Extract routing information from source[], it expects that source is properly encoded routing label atleast(5bytes long,
     * same as dest). It copies data to <b>dest</b> and swamp <i>DPC</i> with <i>OPC</i>.
     *
     * @param source
     * @param dest
     */
    public static void copyBackRouteHeader(byte[] source, byte[] dest) {
        int thisPointCode = getFromSif_DPC(source, 1);
        int remotePointCode = getFromSif_OPC(source, 1);
        int sls = getFromSif_SLS(source, 1);
        int si = getFromSif_SI(source);
        int ssi = getFromSif_SSI(source);
        writeRoutingLabel(dest, si, ssi, sls, remotePointCode, thisPointCode);
    }

    /**
     * Retrieves DPC from SIF. SIF starts from byte 1 in MSU. For MSU [SIO,DPC,OPC,SLS,Data], call would look as following: int
     * dpc = getFromSif_DPC(MSU,1);
     *
     * @param sif - byte[] - either SIF or MSU
     * @param shift - shift in passed byte[]. For MSU its 1. For SIF part of MSU it will be 0;
     * @return
     */
    public static final int getFromSif_DPC(byte[] sif, int shift) {
        int dpc = (sif[0 + shift] & 0xff | ((sif[1 + shift] & 0x3f) << 8));
        return dpc;
    }

    /**
     * Retrieves OPC from SIF. SIF starts from byte 1 in MSU. For MSU [SIO,DPC,OPC,SLS,Data], call would look as following: int
     * opc = getFromSif_OPC(MSU,1);
     *
     * @param sif - byte[] - either SIF or MSU
     * @param shift - shift in passed byte[]. For MSU its 1. For SIF part of MSU it will be 0;
     * @return
     */
    public static final int getFromSif_OPC(byte[] sif, int shift) {
        int opc = ((sif[1 + shift] & 0xC0) >> 6) | ((sif[2 + shift] & 0xff) << 2) | ((sif[3 + shift] & 0x0f) << 10);
        return opc;
    }

    /**
     * Retrieves SLS from SIF. SIF starts from byte 1 in MSU. For MSU [SIO,DPC,OPC,SLS,Data], call would look as following: int
     * opc = getFromSif_SLS(MSU,1);
     *
     * @param sif - byte[] - either SIF or MSU
     * @param shift - shift in passed byte[]. For MSU its 1. For SIF part of MSU it will be 0;
     * @return
     */
    public static final int getFromSif_SLS(byte[] sif, int shift) {
        int sls = (sif[3 + shift] & 0xf0) >>> 4;
        return sls;
    }

    public static final int getFromSif_SI(byte[] data) {

        int serviceIndicator = data[0] & 0x0f;
        return serviceIndicator;
    }

    public static final int getFromSif_SSI(byte[] data) {
        // see Q.704.14.2
        int subserviceIndicator = (data[0] >> 4) & 0x0F;
        return subserviceIndicator;
    }

    /**
     * Encodes routing label into passed byte[]. It has to be at least 5 bytes long!
     *
     * @param destination
     * @param si
     * @param ssi
     * @param sls
     * @param dpc
     * @param opc
     */
    public static void writeRoutingLabel(byte[] destination, int si, int ssi, int sls, int dpc, int opc) {
        // see Q.704.14.2
        destination[0] = (byte) (((ssi & 0x0F) << 4) | (si & 0x0F));
        destination[1] = (byte) dpc;
        destination[2] = (byte) (((dpc >> 8) & 0x3F) | ((opc & 0x03) << 6));
        destination[3] = (byte) (opc >> 2);
        destination[4] = (byte) (((opc >> 10) & 0x0F) | ((sls & 0x0F) << 4));
        // sif[4] = (byte) (((opc>> 10) & 0x0F) | ((0 & 0x0F) << 4));
    }

}
