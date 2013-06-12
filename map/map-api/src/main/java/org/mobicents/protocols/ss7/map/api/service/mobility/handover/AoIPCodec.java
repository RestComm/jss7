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

package org.mobicents.protocols.ss7.map.api.service.mobility.handover;

/**
 *
 AoIPCodec ::= OCTET STRING (SIZE (1..3))
 *
 * -- The internal structure is defined as follows: -- octet 1 Coded as Speech Codec Elements in 3GPP TS 48.008 -- with the
 * exception that FI, PI, PT and TF bits shall -- be set to 0 -- octets 2,3 Optional; in case of AMR codec types it defines --
 * the supported codec configurations as defined in -- 3GPP TS 48.008
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface AoIPCodec {

    byte[] getData();

}
