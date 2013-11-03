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

package org.mobicents.protocols.ss7.sccp.message;

import org.mobicents.protocols.ss7.sccp.parameter.Importance;
import org.mobicents.protocols.ss7.sccp.parameter.ReturnCause;
import org.mobicents.protocols.ss7.sccp.parameter.Segmentation;

/**
 *
 * This interface represents SCCP a connectionless notice message (UDTS, XUDTS and LUDTS)
 *
 * @author sergey vetyutnev
 *
 */
public interface SccpNoticeMessage extends SccpAddressedMessage {

    ReturnCause getReturnCause();

    byte[] getData();

    Segmentation getSegmentation();

    Importance getImportance();

    void setReturnCause(ReturnCause rc);

    void setData(byte[] data);

    void setImportance(Importance p);

}
