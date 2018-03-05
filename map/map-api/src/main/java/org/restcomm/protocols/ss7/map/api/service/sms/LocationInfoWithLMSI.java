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

package org.restcomm.protocols.ss7.map.api.service.sms;

import java.io.Serializable;

import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.LMSI;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.service.lsm.AdditionalNumber;

/**
 *
<code>
LocationInfoWithLMSI ::= SEQUENCE {
  networkNode-Number    [1] ISDN-AddressString,
  lmsi                  LMSI OPTIONAL,
  extensionContainer    ExtensionContainer OPTIONAL,
  ...,
  gprsNodeIndicator     [5] NULL OPTIONAL,
  -- gprsNodeIndicator is set only if the SGSN number is sent as the
  -- Network Node Number
  additional-Number     [6] Additional-Number OPTIONAL
  -- NetworkNode-number can be either msc-number or sgsn-number

Additional-Number ::= CHOICE {
    msc-Number          [0] ISDN-AddressString,
    sgsn-Number         [1] ISDN-AddressString
}
-- additional-number can be either msc-number or sgsn-number
-- if received networkNode-number is msc-number then the
-- additional number is sgsn-number
-- if received networkNode-number is sgsn-number then the
-- additional number is msc-number
}
</code>
 *
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface LocationInfoWithLMSI extends Serializable {

    ISDNAddressString getNetworkNodeNumber();

    LMSI getLMSI();

    MAPExtensionContainer getExtensionContainer();

    boolean getGprsNodeIndicator();

    AdditionalNumber getAdditionalNumber();

}
