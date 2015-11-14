/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
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
package org.mobicents.protocols.ss7.isup.message.parameter;

import java.io.Serializable;

import org.mobicents.protocols.asn.Tag;

/**
 * Base interface for operations carried in {@link RemoteOperations}
 * @author baranowb
 *
 */
public interface RemoteOperation extends Serializable{
    //I wonder why this is different then regular ops from TCAP...
    int _COMPONENT_TAG = 0x0C;
    boolean _COMPONENT_TAG_PC_PRIMITIVE = false;
    int _COMPONENT_TAG_CLASS = Tag.CLASS_APPLICATION;

    void setInvokeId(Long i);

    Long getInvokeId();

    OperationType getType();

    public enum OperationType {

        Invoke, ReturnResult, Reject, ReturnError;
    }
}
