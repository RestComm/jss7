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

package org.mobicents.protocols.ss7.m3ua.parameter;

/**
 * The Deregistration Result parameter contains the deregistration status for a single Routing Context in a DEREG REQ message.
 * The number of results in a single DEREG RSP message MAY be anywhere from one to the total number of number of Routing Context
 * values found in the corresponding DEREG REQ message.
 *
 * Where multiple DEREG RSP messages are used in reply to DEREG REQ message, a specific result SHOULD be in only one DEREG RSP
 * message.
 *
 * @author amit bhayani
 *
 */
public interface DeregistrationResult extends Parameter {

    RoutingContext getRoutingContext();

    DeregistrationStatus getDeregistrationStatus();
}
