/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.mobicents.protocols.ss7.m3ua.message.rkm;

import org.mobicents.protocols.ss7.m3ua.message.M3UAMessage;
import org.mobicents.protocols.ss7.m3ua.parameter.RegistrationResult;

/**
 * The Registration Response (REG RSP) message is used as a response to the REG
 * REQ message from a remote M3UA peer. It contains indications of
 * success/failure for registration requests and returns a unique Routing
 * Context value for successful registration requests, to be used in subsequent
 * M3UA Traffic Management protocol.
 * 
 * @author amit bhayani
 * 
 */
public interface RegistrationResponse extends M3UAMessage {
    public RegistrationResult getRegistrationResult();

    public void setRegistrationResult(RegistrationResult rslt);
}
