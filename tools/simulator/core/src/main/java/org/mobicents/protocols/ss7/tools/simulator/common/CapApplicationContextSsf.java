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

package org.mobicents.protocols.ss7.tools.simulator.common;

import java.util.Hashtable;

import org.mobicents.protocols.ss7.cap.api.CAPApplicationContext;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class CapApplicationContextSsf extends EnumeratedBase {

    private static final long serialVersionUID = -5721221865156852024L;

    public static final int VAL_CAP_V1_gsmSSF_to_gsmSCF = 1;
    public static final int VAL_CAP_V2_gsmSSF_to_gsmSCF = 2;
    public static final int VAL_CAP_V3_scfGeneric = 3;
    public static final int VAL_CAP_V4_scfGeneric = 4;

    public static final int VAL_CAP_V2_assist_gsmSSF_to_gsmSCF = 12;
    public static final int VAL_CAP_V3_scfAssistHandoffAC = 13;
    public static final int VAL_CAP_V4_scfAssistHandoffAC = 14;

    public static final int VAL_CAP_V2_gsmSRF_to_gsmSCF = 22;
    public static final int VAL_CAP_V3_gsmSRF_gsmSCF = 23;
    public static final int VAL_CAP_V4_gsmSRF_gsmSCF = 24;

    private static Hashtable<String, Integer> stringMap = new Hashtable<String, Integer>();
    private static Hashtable<Integer, String> intMap = new Hashtable<Integer, String>();

    static {
        intMap.put(VAL_CAP_V1_gsmSSF_to_gsmSCF, "CAP_V1_gsmSSF_to_gsmSCF 0.50.0");
        intMap.put(VAL_CAP_V2_gsmSSF_to_gsmSCF, "CAP_V2_gsmSSF_to_gsmSCF 0.50.1");
        intMap.put(VAL_CAP_V3_scfGeneric, "CAP_V3_scfGeneric 21.3.4");
        intMap.put(VAL_CAP_V4_scfGeneric, "CAP_V4_scfGeneric 23.3.4");

        intMap.put(VAL_CAP_V2_assist_gsmSSF_to_gsmSCF, "CAP_V2_assist_gsmSSF_to_gsmSCF 0.51.1");
        intMap.put(VAL_CAP_V3_scfAssistHandoffAC, "CAP_V3_scfAssistHandoffAC 21.3.6");
        intMap.put(VAL_CAP_V4_scfAssistHandoffAC, "CAP_V4_scfAssistHandoffAC 23.3.6");

        intMap.put(VAL_CAP_V2_gsmSRF_to_gsmSCF, "CAP_V2_gsmSRF_to_gsmSCF 0.52.1");
        intMap.put(VAL_CAP_V3_gsmSRF_gsmSCF, "CAP_V3_gsmSRF_gsmSCF 20.3.14");
        intMap.put(VAL_CAP_V4_gsmSRF_gsmSCF, "CAP_V4_gsmSRF_gsmSCF 22.3.14");

        stringMap.put("CAP_V1_gsmSSF_to_gsmSCF 0.50.0", VAL_CAP_V1_gsmSSF_to_gsmSCF);
        stringMap.put("CAP_V2_gsmSSF_to_gsmSCF 0.50.1", VAL_CAP_V2_gsmSSF_to_gsmSCF);
        stringMap.put("CAP_V3_scfGeneric 21.3.4", VAL_CAP_V3_scfGeneric);
        stringMap.put("CAP_V4_scfGeneric 23.3.4", VAL_CAP_V4_scfGeneric);

        stringMap.put("CAP_V2_assist_gsmSSF_to_gsmSCF 0.51.1", VAL_CAP_V2_assist_gsmSSF_to_gsmSCF);
        stringMap.put("CAP_V3_scfAssistHandoffAC 21.3.6", VAL_CAP_V3_scfAssistHandoffAC);
        stringMap.put("CAP_V4_scfAssistHandoffAC 23.3.6", VAL_CAP_V4_scfAssistHandoffAC);

        stringMap.put("CAP_V2_gsmSRF_to_gsmSCF 0.52.1", VAL_CAP_V2_gsmSRF_to_gsmSCF);
        stringMap.put("CAP_V3_gsmSRF_gsmSCF 20.3.14", VAL_CAP_V3_gsmSRF_gsmSCF);
        stringMap.put("CAP_V4_gsmSRF_gsmSCF 22.3.14", VAL_CAP_V4_gsmSRF_gsmSCF);
    }

    public CapApplicationContextSsf() {
    }

    public CapApplicationContextSsf(int val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public CapApplicationContextSsf(Integer val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public CapApplicationContextSsf(String val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public static CapApplicationContextSsf createInstance(String s) {
        Integer i1 = doCreateInstance(s, stringMap, intMap);
        if (i1 == null)
            return new CapApplicationContextSsf(VAL_CAP_V1_gsmSSF_to_gsmSCF);
        else
            return new CapApplicationContextSsf(i1);
    }

    @Override
    protected Hashtable<Integer, String> getIntTable() {
        return intMap;
    }

    @Override
    protected Hashtable<String, Integer> getStringTable() {
        return stringMap;
    }

    public CAPApplicationContext getCAPApplicationContext() {
        switch (this.intValue()) {
            case VAL_CAP_V1_gsmSSF_to_gsmSCF:
                return CAPApplicationContext.CapV1_gsmSSF_to_gsmSCF;
            case VAL_CAP_V2_gsmSSF_to_gsmSCF:
                return CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF;
            case VAL_CAP_V3_scfGeneric:
                return CAPApplicationContext.CapV3_gsmSSF_scfGeneric;
            case VAL_CAP_V4_scfGeneric:
                return CAPApplicationContext.CapV4_gsmSSF_scfGeneric;

            case VAL_CAP_V2_assist_gsmSSF_to_gsmSCF:
                return CAPApplicationContext.CapV2_assistGsmSSF_to_gsmSCF;
            case VAL_CAP_V3_scfAssistHandoffAC:
                return CAPApplicationContext.CapV3_gsmSSF_scfAssistHandoff;
            case VAL_CAP_V4_scfAssistHandoffAC:
                return CAPApplicationContext.CapV4_gsmSSF_scfAssistHandoff;

            case VAL_CAP_V2_gsmSRF_to_gsmSCF:
                return CAPApplicationContext.CapV2_gsmSRF_to_gsmSCF;
            case VAL_CAP_V3_gsmSRF_gsmSCF:
                return CAPApplicationContext.CapV3_gsmSRF_gsmSCF;
            case VAL_CAP_V4_gsmSRF_gsmSCF:
                return CAPApplicationContext.CapV4_gsmSRF_gsmSCF;
        }
        return CAPApplicationContext.CapV1_gsmSSF_to_gsmSCF;
    }

}
