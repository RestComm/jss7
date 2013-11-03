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
package org.mobicents.protocols.ss7.m3ua.impl;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.m3ua.message.rkm.DeregistrationRequest;
import org.mobicents.protocols.ss7.m3ua.message.rkm.DeregistrationResponse;
import org.mobicents.protocols.ss7.m3ua.message.rkm.RegistrationRequest;
import org.mobicents.protocols.ss7.m3ua.message.rkm.RegistrationResponse;

/**
 *
 * @author amit bhayani
 *
 */
public class RoutingKeyManagementHandler extends MessageHandler {
    private static final Logger logger = Logger.getLogger(RoutingKeyManagementHandler.class);

    public RoutingKeyManagementHandler(AspFactoryImpl aspFactoryImpl) {
        super(aspFactoryImpl);
    }

    public void handleRegistrationRequest(RegistrationRequest registrationRequest) {
        logger.error(String.format("Received REGREQ=%s. Handling of RKM message is not supported", registrationRequest));

    }

    public void handleRegistrationResponse(RegistrationResponse registrationResponse) {
        logger.error(String.format("Received REGRES=%s. Handling of RKM message is not supported", registrationResponse));
    }

    public void handleDeregistrationRequest(DeregistrationRequest deregistrationRequest) {
        logger.error(String.format("Received DEREGREQ=%s. Handling of RKM message is not supported", deregistrationRequest));
    }

    public void handleDeregistrationResponse(DeregistrationResponse deregistrationResponse) {
        logger.error(String.format("Received DEREGRES=%s. Handling of RKM message is not supported", deregistrationResponse));
    }

}
