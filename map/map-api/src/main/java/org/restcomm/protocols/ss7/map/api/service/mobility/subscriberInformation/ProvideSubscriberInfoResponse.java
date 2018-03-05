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

package org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation;

import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.service.mobility.MobilityMessage;

/**
 * <p>
 * The HLR may use MAP PSI to obtain subscriber data from VLR; the subscriber data that may be obtained with MAP PSI includes
 * location information and subscriber state. In CAMEL phase 3 and CAMEL phase 4, additional information may be obtained from
 * the VLR
 * </p>
 * <p>
 * Within the context of CAMEL, the HLR may use MAP PSI for the following procedures: (1) MT call handling (2) ATI
 * </p>
 * <p>
 * The HLR may also use MAP PSI for optimal routing (OR); refer to GSM TS 03.79 [39] for a description of OR
 * </p>
 *
<code>
ProvideSubscriberInfoRes ::= SEQUENCE {
  subscriberInfo       SubscriberInfo,
  extensionContainer   ExtensionContainer OPTIONAL,
  ...
}
</code>
 *
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface ProvideSubscriberInfoResponse extends MobilityMessage {

    SubscriberInfo getSubscriberInfo();

    MAPExtensionContainer getExtensionContainer();

}
