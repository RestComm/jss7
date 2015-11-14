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
 * The optional Concerned Destination parameter is only used if the SCON message is sent from an ASP to the SGP. It contains the
 * point code of the originator of the message that triggered the SCON message. Any resulting Transfer Controlled (TFC) message
 * from the SG is sent to the Concerned Point Code using the single Affected DPC contained in the SCON message to populate the
 * (affected) Destination field of the TFC message
 *
 * @author abhayani
 *
 */
public interface ConcernedDPC extends Parameter {

    int getPointCode();

}
