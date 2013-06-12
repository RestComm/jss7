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

package org.mobicents.protocols.ss7.cap.api.service.gprs.primitive;

/**
 *
 AccessPointName {PARAMETERS-BOUND : bound} ::= OCTET STRING (SIZE( bound.&minAccessPointNameLength ..
 * bound.&maxAccessPointNameLength)) -- Indicates the AccessPointName, refer to 3GPP TS 24.008 [9] for the encoding. -- It shall
 * be coded as in the value part defined in 3GPP TS 24.008, -- i.e. the 3GPP TS 24.008 IEI and 3GPP TS 24.008 octet length
 * indicator -- shall not be included.
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface AccessPointName {

    byte[] getData();

}
