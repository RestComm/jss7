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

package org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation;

import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.MobilityMessage;

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
 * ProvideSubscriberInfoRes ::= SEQUENCE { subscriberInfo SubscriberInfo, extensionContainer ExtensionContainer OPTIONAL, ...}
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
