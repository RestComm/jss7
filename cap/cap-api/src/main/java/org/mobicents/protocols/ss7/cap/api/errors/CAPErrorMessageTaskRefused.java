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

package org.mobicents.protocols.ss7.cap.api.errors;

/**
 *
 taskRefused ERROR ::= { PARAMETER ENUMERATED { generic (0), unobtainable (1), congestion (2) } CODE errcode-taskRefused } --
 * An entity normally capable of the task requested cannot or chooses not to perform the task at -- this time. This includes
 * error situations like congestion and unobtainable address as used in -- e.g. the connect operation.)
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface CAPErrorMessageTaskRefused extends CAPErrorMessage {

    TaskRefusedParameter getTaskRefusedParameter();

}
