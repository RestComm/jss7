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

package org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement;

/**
 *
 MT-SMS-TPDU-Type ::= ENUMERATED { sms-DELIVER (0), sms-SUBMIT-REPORT (1), sms-STATUS-REPORT (2), ... }
 *
 * -- exception handling: -- For TPDU-TypeCriterion sequences containing this parameter with any -- other value than the ones
 * listed above the receiver shall ignore -- the whole TPDU-TypeCriterion sequence. -- In CAMEL phase 4, sms-SUBMIT-REPORT shall
 * not be used and a received TPDU-TypeCriterion -- sequence containing sms-SUBMIT-REPORT shall be wholly ignored.
 *
 *
 *
 * @author sergey vetyutnev
 *
 */
public enum MTSMSTPDUType {
    smsDELIVER(0), smsSUBMITREPORT(1), smsSTATUSREPORT(2);

    private int code;

    private MTSMSTPDUType(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static MTSMSTPDUType getInstance(int code) {
        switch (code) {
            case 0:
                return MTSMSTPDUType.smsDELIVER;
            case 1:
                return MTSMSTPDUType.smsSUBMITREPORT;
            case 2:
                return MTSMSTPDUType.smsSTATUSREPORT;
            default:
                return null;
        }
    }
}
