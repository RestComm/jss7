/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SpecificCSIWithdraw;
import org.restcomm.protocols.ss7.map.primitives.BitStringBase;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class SpecificCSIWithdrawImpl extends BitStringBase implements SpecificCSIWithdraw {

    private static final int _INDEX_o_csi = 0;
    private static final int _INDEX_ss_csi = 1;
    private static final int _INDEX_tif_csi = 2;
    private static final int _INDEX_d_csi = 3;
    private static final int _INDEX_vt_csi = 4;
    private static final int _INDEX_mo_sms_csi = 5;
    private static final int _INDEX_m_csi = 6;
    private static final int _INDEX_gprs_csi = 7;
    private static final int _INDEX_t_csi = 8;
    private static final int _INDEX_mt_sms_csi = 9;
    private static final int _INDEX_mg_csi = 10;
    private static final int _INDEX_o_IM_CSI = 11;
    private static final int _INDEX_d_IM_CSI = 12;
    private static final int _INDEX_vt_IM_CSI = 13;

    public SpecificCSIWithdrawImpl() {
        super(8, 32, 14, "SpecificCSIWithdraw");
    }

    public SpecificCSIWithdrawImpl(boolean OCsi, boolean SsCsi, boolean TifCsi, boolean DCsi, boolean VtCsi, boolean MoSmsCsi, boolean MCsi, boolean GprsCsi,
            boolean TCsi, boolean MtSmsCsi, boolean MgCsi, boolean OImCsi, boolean DImCsi, boolean VtImCsi) {
        super(8, 32, 14, "SpecificCSIWithdraw");

        if (OCsi)
            this.bitString.set(_INDEX_o_csi);
        if (SsCsi)
            this.bitString.set(_INDEX_ss_csi);
        if (TifCsi)
            this.bitString.set(_INDEX_tif_csi);
        if (DCsi)
            this.bitString.set(_INDEX_d_csi);
        if (VtCsi)
            this.bitString.set(_INDEX_vt_csi);
        if (MoSmsCsi)
            this.bitString.set(_INDEX_mo_sms_csi);
        if (MCsi)
            this.bitString.set(_INDEX_m_csi);
        if (GprsCsi)
            this.bitString.set(_INDEX_gprs_csi);
        if (TCsi)
            this.bitString.set(_INDEX_t_csi);
        if (MtSmsCsi)
            this.bitString.set(_INDEX_mt_sms_csi);
        if (MgCsi)
            this.bitString.set(_INDEX_mg_csi);
        if (OImCsi)
            this.bitString.set(_INDEX_o_IM_CSI);
        if (DImCsi)
            this.bitString.set(_INDEX_d_IM_CSI);
        if (VtImCsi)
            this.bitString.set(_INDEX_vt_IM_CSI);
    }

    @Override
    public boolean getOCsi() {
        return this.bitString.get(_INDEX_o_csi);
    }

    @Override
    public boolean getSsCsi() {
        return this.bitString.get(_INDEX_ss_csi);
    }

    @Override
    public boolean getTifCsi() {
        return this.bitString.get(_INDEX_tif_csi);
    }

    @Override
    public boolean getDCsi() {
        return this.bitString.get(_INDEX_d_csi);
    }

    @Override
    public boolean getVtCsi() {
        return this.bitString.get(_INDEX_vt_csi);
    }

    @Override
    public boolean getMoSmsCsi() {
        return this.bitString.get(_INDEX_mo_sms_csi);
    }

    @Override
    public boolean getMCsi() {
        return this.bitString.get(_INDEX_m_csi);
    }

    @Override
    public boolean getGprsCsi() {
        return this.bitString.get(_INDEX_gprs_csi);
    }

    @Override
    public boolean getTCsi() {
        return this.bitString.get(_INDEX_t_csi);
    }

    @Override
    public boolean getMtSmsCsi() {
        return this.bitString.get(_INDEX_mt_sms_csi);
    }

    @Override
    public boolean getMgCsi() {
        return this.bitString.get(_INDEX_mg_csi);
    }

    @Override
    public boolean getOImCsi() {
        return this.bitString.get(_INDEX_o_IM_CSI);
    }

    @Override
    public boolean getDImCsi() {
        return this.bitString.get(_INDEX_d_IM_CSI);
    }

    @Override
    public boolean getVtImCsi() {
        return this.bitString.get(_INDEX_vt_IM_CSI);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this._PrimitiveName);
        sb.append(" [");

        if (getOCsi())
            sb.append("OCsi, ");
        if (getSsCsi())
            sb.append("SsCsi, ");
        if (getTifCsi())
            sb.append("TifCsi, ");
        if (getDCsi())
            sb.append("DCsi, ");
        if (getVtCsi())
            sb.append("VtCsi, ");
        if (getMoSmsCsi())
            sb.append("MoSmsCsi, ");
        if (getMCsi())
            sb.append("MCsi, ");
        if (getGprsCsi())
            sb.append("GprsCsi, ");
        if (getTCsi())
            sb.append("TCsi, ");
        if (getMtSmsCsi())
            sb.append("MtSmsCsi, ");
        if (getMgCsi())
            sb.append("MgCsi, ");
        if (getOImCsi())
            sb.append("OImCsi, ");
        if (getDImCsi())
            sb.append("DImCsi, ");
        if (getVtImCsi())
            sb.append("VtImCsi, ");

        sb.append("]");

        return sb.toString();
    }

}

