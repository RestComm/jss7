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

package org.mobicents.protocols.ss7.map.smstpdu;

import java.nio.charset.Charset;

import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.smstpdu.SmsTpdu;
import org.mobicents.protocols.ss7.map.api.smstpdu.SmsTpduType;

/**
 *
 * @author sergey vetyutnev
 *
 */
public abstract class SmsTpduImpl implements SmsTpdu {

    protected static int _MASK_TP_MTI = 0x03;
    protected static int _MASK_TP_MMS = 0x04;
    protected static int _MASK_TP_RD = 0x04;
    protected static int _MASK_TP_LP = 0x08;
    protected static int _MASK_TP_SRI = 0x20;
    protected static int _MASK_TP_SRR = 0x20;
    protected static int _MASK_TP_SRQ = 0x20;
    protected static int _MASK_TP_UDHI = 0x40;
    protected static int _MASK_TP_RP = 0x80;
    protected static int _MASK_TP_VPF = 0x18;

    protected static int _UserDataLimit = 140;
    protected static int _UserDataStatusReportLimit = 131;
    protected static int _UserDataSubmitReportLimit = 152;
    protected static int _UserDataDeliverReportLimit = 159;
    protected static int _CommandDataLimit = 146;

    protected boolean mobileOriginatedMessage;
    protected SmsTpduType tpduType;

    protected SmsTpduImpl() {
    }

    public static SmsTpduImpl createInstance(byte[] data, boolean mobileOriginatedMessage, Charset gsm8Charset)
            throws MAPException {

        if (data == null)
            throw new MAPException("Error creating a new SmsTpduImpl instance: data is empty");
        if (data.length < 1)
            throw new MAPException("Error creating a new SmsTpduImpl instance: data length is equal zero");

        int tpMti = data[0] & _MASK_TP_MTI;
        if (mobileOriginatedMessage) {
            SmsTpduType type = SmsTpduType.getMobileOriginatedInstance(tpMti);
            switch (type) {
                case SMS_DELIVER_REPORT:
                    return new SmsDeliverReportTpduImpl(data, gsm8Charset);
                case SMS_SUBMIT:
                    return new SmsSubmitTpduImpl(data, gsm8Charset);
                case SMS_COMMAND:
                    return new SmsCommandTpduImpl(data);
            }
        } else {
            SmsTpduType type = SmsTpduType.getMobileTerminatedInstance(tpMti);
            switch (type) {
                case SMS_DELIVER:
                    return new SmsDeliverTpduImpl(data, gsm8Charset);
                case SMS_SUBMIT_REPORT:
                    return new SmsSubmitReportTpduImpl(data, gsm8Charset);
                case SMS_STATUS_REPORT:
                    return new SmsStatusReportTpduImpl(data, gsm8Charset);
            }
        }

        throw new MAPException("Error creating a new SmsTpduImpl instance: unsupported Sms Tpdu type");
    }

    public SmsTpduType getSmsTpduType() {
        return tpduType;
    }
}
