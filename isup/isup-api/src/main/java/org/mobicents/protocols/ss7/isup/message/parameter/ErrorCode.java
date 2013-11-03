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

package org.mobicents.protocols.ss7.isup.message.parameter;

import java.io.Serializable;

import org.mobicents.protocols.asn.Tag;

/**
 * @author baranowb
 * @author sergey netyutnev
 *
 */
public interface ErrorCode extends Serializable {

    // it contains valid params for error....

    int _TAG_LOCAL = Tag.INTEGER;
    int _TAG_GLOBAL = Tag.OBJECT_IDENTIFIER;
    int _TAG_CLASS = Tag.CLASS_UNIVERSAL;
    boolean _TAG_PRIMITIVE = true;

    // public void setErrorType(ErrorCodeType t);
    ErrorCodeType getErrorType();

    void setLocalErrorCode(Long localErrorCode);

    void setGlobalErrorCode(long[] globalErrorCode);

    Long getLocalErrorCode();

    long[] getGlobalErrorCode();

}
