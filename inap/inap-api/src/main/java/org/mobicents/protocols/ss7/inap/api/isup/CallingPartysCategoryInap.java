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

package org.mobicents.protocols.ss7.inap.api.isup;

import java.io.Serializable;

import org.mobicents.protocols.ss7.inap.api.INAPException;
import org.mobicents.protocols.ss7.isup.message.parameter.CallingPartyCategory;

/**
 *
 ISUP CallingPartyCategory wrapper
 *
 * CallingPartysCategory::= OCTET STRING (SIZE (1)) -- Indicates the type of calling party (e.g. operator, payphone, ordinary
 * subscriber). Refer to -- ETS 300 356-1 [7] for encoding.
 *
 *
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface CallingPartysCategoryInap extends Serializable {

    byte[] getData();

    CallingPartyCategory getCallingPartyCategory() throws INAPException;

}
