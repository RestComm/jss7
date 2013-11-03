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

package org.mobicents.protocols.ss7.cap.api.dialog;

import java.io.Serializable;

/**
 *
 * @author sergey vetyutnev
 *
 *         CAP-GPRS-ReferenceNumber ::= SEQUENCE { destinationReference [0] Integer4 OPTIONAL, originationReference [1] Integer4
 *         OPTIONAL } -- This parameter is used to identify the relationship between SGSN and the gsmSCF.
 *
 *         Integer4::= INTEGER (0..2147483647)
 */
public interface CAPGprsReferenceNumber extends Serializable {

    Integer getDestinationReference();

    Integer getOriginationReference();

    void setDestinationReference(Integer destinationReference);

    void setOriginationReference(Integer originationReference);
}