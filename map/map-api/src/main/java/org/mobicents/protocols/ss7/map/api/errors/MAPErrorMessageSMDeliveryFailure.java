/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.mobicents.protocols.ss7.map.api.errors;

import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.smstpdu.SmsDeliverReportTpdu;


/**
 *
The MAP ReturnError message: MessageSMDeliveryFailure with parameters

sm-DeliveryFailure ERROR ::= {
  PARAMETER SM-DeliveryFailureCause
  CODE local:32
}

MAP V 2-3
SM-DeliveryFailureCause ::= SEQUENCE {
  sm-EnumeratedDeliveryFailureCause   SM-EnumeratedDeliveryFailureCause,
  diagnosticInfo                      SignalInfo OPTIONAL,
  extensionContainer                  ExtensionContainer OPTIONAL,
  ...
}

MAP V1
sm-EnumeratedDeliveryFailureCause     SM-EnumeratedDeliveryFailureCause

 *
 * @author sergey vetyutnev
 *
 */
public interface MAPErrorMessageSMDeliveryFailure extends MAPErrorMessage {

    SMEnumeratedDeliveryFailureCause getSMEnumeratedDeliveryFailureCause();

    byte[] getSignalInfo();

    MAPExtensionContainer getExtensionContainer();

    long getMapProtocolVersion();

    void setSMEnumeratedDeliveryFailureCause(SMEnumeratedDeliveryFailureCause sMEnumeratedDeliveryFailureCause);

    void setSignalInfo(byte[] signalInfo);

    void setExtensionContainer(MAPExtensionContainer extensionContainer);

    void setMapProtocolVersion(long mapProtocolVersion);

    SmsDeliverReportTpdu getSmsDeliverReportTpdu() throws MAPException;

    void setSmsDeliverReportTpdu(SmsDeliverReportTpdu tpdu) throws MAPException;

}
