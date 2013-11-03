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

package org.mobicents.protocols.ss7.tcap.asn.comp;

import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.tcap.asn.ParseException;

/**
 * @author baranowb
 *
 */
public enum ProblemType {

    General(0), Invoke(1), ReturnResult(2), ReturnError(3);

    private long typeTag = -1;

    public static final int _TAG_CLASS = Tag.CLASS_APPLICATION;
    public static final boolean _TAG_PC_PRITIMITIVE = true;

    ProblemType(long l) {
        this.typeTag = l;
    }

    /**
     * @return the typeTag
     */
    public long getTypeTag() {
        return typeTag;
    }

    public static ProblemType getFromInt(long t) throws ParseException {
        if (t == 0) {
            return General;
        } else if (t == 1) {
            return Invoke;
        } else if (t == 2) {
            return ReturnResult;
        } else if (t == 3) {
            return ReturnError;
        }

        throw new ParseException(null, null, "Wrong value of type: " + t);
    }
}
