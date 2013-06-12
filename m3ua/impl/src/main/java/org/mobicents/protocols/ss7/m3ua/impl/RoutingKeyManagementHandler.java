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
