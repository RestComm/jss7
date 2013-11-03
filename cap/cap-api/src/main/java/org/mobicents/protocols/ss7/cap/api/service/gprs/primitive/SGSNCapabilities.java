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

package org.mobicents.protocols.ss7.cap.api.service.gprs.primitive;

import java.io.Serializable;

/**
 *
 SGSNCapabilities ::= OCTET STRING (SIZE (1)) -- Indicates the SGSN capabilities. The coding of the parameter is as follows:
 * -- Bit Value Meaning -- 0 0 AoC not supported by SGSN -- 1 AoC supported by SGSN -- 1 - This bit is reserved in CAP V.3 -- 2
 * - This bit is reserved in CAP V.3 -- 3 - This bit is reserved in CAP V.3 -- 4 - This bit is reserved in CAP V.3 -- 5 - This
 * bit is reserved in CAP V.3 -- 6 - This bit is reserved in CAP V.3 -- 7 - This bit is reserved in CAP V.3
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface SGSNCapabilities extends Serializable {

    int getData();

    boolean getAoCSupportedBySGSN();

}