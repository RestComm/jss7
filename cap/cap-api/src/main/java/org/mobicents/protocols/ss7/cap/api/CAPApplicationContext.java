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

package org.mobicents.protocols.ss7.cap.api;

import java.util.Arrays;

/**
 *
 * @author sergey vetyutnev
 *
 */
public enum CAPApplicationContext {
    CapV1_gsmSSF_to_gsmSCF(11),

    CapV2_gsmSSF_to_gsmSCF(21), CapV2_assistGsmSSF_to_gsmSCF(22), CapV2_gsmSRF_to_gsmSCF(24),

    CapV3_gsmSSF_scfGeneric(31), CapV3_gsmSSF_scfAssistHandoff(32), CapV3_gsmSRF_gsmSCF(34), CapV3_gprsSSF_gsmSCF(35), CapV3_gsmSCF_gprsSSF(
            36), CapV3_cap3_sms(37),

    CapV4_gsmSSF_scfGeneric(41), CapV4_gsmSSF_scfAssistHandoff(42), CapV4_scf_gsmSSFGeneric(43), CapV4_gsmSRF_gsmSCF(44), CapV4_cap4_sms(
            48);

    private static long[] oidTemplate = new long[] { 0, 4, 0, 0, 1, 0, 0, 0 };

    private int code;
    private CAPApplicationContextVersion applicationContextVersion;

    private CAPApplicationContext(int code) {
        this.code = code;

        if (code == 11) {
            this.applicationContextVersion = CAPApplicationContextVersion.version1;
        } else if (code < 30) {
            this.applicationContextVersion = CAPApplicationContextVersion.version2;
        } else if (code < 40) {
            this.applicationContextVersion = CAPApplicationContextVersion.version3;
        } else {
            this.applicationContextVersion = CAPApplicationContextVersion.version4;
        }
    }

    public static CAPApplicationContext getInstance(long[] oid) {

        if (oid == null || oid.length != oidTemplate.length)
            return null;
        for (int i1 = 0; i1 < oidTemplate.length - 3; i1++) {
            if (oid[i1] != oidTemplate[i1])
                return null;
        }

        if (oid[5] == 0 && oid[6] == 50 && oid[7] == 0) {
            return CAPApplicationContext.CapV1_gsmSSF_to_gsmSCF;

        }
        if (oid[5] == 0 && oid[6] == 50 && oid[7] == 1) {
            return CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF;
        }
        if (oid[5] == 0 && oid[6] == 51 && oid[7] == 1) {
            return CAPApplicationContext.CapV2_assistGsmSSF_to_gsmSCF;
        }
        if (oid[5] == 0 && oid[6] == 52 && oid[7] == 1) {
            return CAPApplicationContext.CapV2_gsmSRF_to_gsmSCF;

        }
        if (oid[5] == 21 && oid[6] == 3 && oid[7] == 4) {
            return CAPApplicationContext.CapV3_gsmSSF_scfGeneric;
        }
        if (oid[5] == 21 && oid[6] == 3 && oid[7] == 6) {
            return CAPApplicationContext.CapV3_gsmSSF_scfAssistHandoff;
        }
        if (oid[5] == 20 && oid[6] == 3 && oid[7] == 14) {
            return CAPApplicationContext.CapV3_gsmSRF_gsmSCF;
        }
        if (oid[5] == 21 && oid[6] == 3 && oid[7] == 50) {
            return CAPApplicationContext.CapV3_gprsSSF_gsmSCF;
        }
        if (oid[5] == 21 && oid[6] == 3 && oid[7] == 51) {
            return CAPApplicationContext.CapV3_gsmSCF_gprsSSF;
        }
        if (oid[5] == 21 && oid[6] == 3 && oid[7] == 61) {
            return CAPApplicationContext.CapV3_cap3_sms;

        }
        if (oid[5] == 23 && oid[6] == 3 && oid[7] == 4) {
            return CAPApplicationContext.CapV4_gsmSSF_scfGeneric;
        }
        if (oid[5] == 23 && oid[6] == 3 && oid[7] == 6) {
            return CAPApplicationContext.CapV4_gsmSSF_scfAssistHandoff;
        }
        if (oid[5] == 23 && oid[6] == 3 && oid[7] == 8) {
            return CAPApplicationContext.CapV4_scf_gsmSSFGeneric;
        }
        if (oid[5] == 22 && oid[6] == 3 && oid[7] == 14) {
            return CAPApplicationContext.CapV4_gsmSRF_gsmSCF;
        }
        if (oid[5] == 23 && oid[6] == 3 && oid[7] == 61) {
            return CAPApplicationContext.CapV4_cap4_sms;
        }

        return null;
    }

    public int getCode() {
        return this.code;
    }

    public CAPApplicationContextVersion getVersion() {
        return this.applicationContextVersion;
    }

    public long[] getOID() {
        long[] res = Arrays.copyOf(oidTemplate, oidTemplate.length);

        switch (this) {
            case CapV1_gsmSSF_to_gsmSCF:
                res[5] = 0;
                res[6] = 50;
                res[7] = 0;
                break;

            case CapV2_gsmSSF_to_gsmSCF:
                res[5] = 0;
                res[6] = 50;
                res[7] = 1;
                break;
            case CapV2_assistGsmSSF_to_gsmSCF:
                res[5] = 0;
                res[6] = 51;
                res[7] = 1;
                break;
            case CapV2_gsmSRF_to_gsmSCF:
                res[5] = 0;
                res[6] = 52;
                res[7] = 1;
                break;

            case CapV3_gsmSSF_scfGeneric:
                res[5] = 21;
                res[6] = 3;
                res[7] = 4;
                break;
            case CapV3_gsmSSF_scfAssistHandoff:
                res[5] = 21;
                res[6] = 3;
                res[7] = 6;
                break;
            case CapV3_gsmSRF_gsmSCF:
                res[5] = 20;
                res[6] = 3;
                res[7] = 14;
                break;
            case CapV3_gprsSSF_gsmSCF:
                res[5] = 21;
                res[6] = 3;
                res[7] = 50;
                break;
            case CapV3_gsmSCF_gprsSSF:
                res[5] = 21;
                res[6] = 3;
                res[7] = 51;
                break;
            case CapV3_cap3_sms:
                res[5] = 21;
                res[6] = 3;
                res[7] = 61;
                break;

            case CapV4_gsmSSF_scfGeneric:
                res[5] = 23;
                res[6] = 3;
                res[7] = 4;
                break;
            case CapV4_gsmSSF_scfAssistHandoff:
                res[5] = 23;
                res[6] = 3;
                res[7] = 6;
                break;
            case CapV4_scf_gsmSSFGeneric:
                res[5] = 23;
                res[6] = 3;
                res[7] = 8;
                break;
            case CapV4_gsmSRF_gsmSCF:
                res[5] = 22;
                res[6] = 3;
                res[7] = 14;
                break;
            case CapV4_cap4_sms:
                res[5] = 23;
                res[6] = 3;
                res[7] = 61;
                break;
        }

        return res;
    }

    @Override
    public String toString() {
        StringBuffer s = new StringBuffer();

        s.append("CAPApplicationContext [Name=");
        s.append(super.toString());
        s.append(", Version=");
        s.append(this.applicationContextVersion.toString());
        s.append(", Oid=");
        for (long l : this.getOID()) {
            s.append(l).append(", ");
        }
        s.append("]");

        return s.toString();
    }
}
