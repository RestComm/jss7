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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.Ext4QoSSubscribed;
import org.restcomm.protocols.ss7.map.primitives.OctetStringLength1Base;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class Ext4QoSSubscribedImpl extends OctetStringLength1Base implements Ext4QoSSubscribed {

    private static final String PRIORITY_LEVEL = "priorityLevel";

    private static final int DEFAULT_INT_VALUE = 0;

    public Ext4QoSSubscribedImpl() {
        super("Ext4QoSSubscribed");
    }

    public Ext4QoSSubscribedImpl(int data) {
        super("Ext4QoSSubscribed", data);
    }

    public int getData() {
        return data;
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<Ext4QoSSubscribedImpl> EXT4_QOS_SUBSCRIBED_XML = new XMLFormat<Ext4QoSSubscribedImpl>(Ext4QoSSubscribedImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, Ext4QoSSubscribedImpl qos4Subscribed) throws XMLStreamException {

            qos4Subscribed.data = xml.getAttribute(PRIORITY_LEVEL, DEFAULT_INT_VALUE);

        }

        @Override
        public void write(Ext4QoSSubscribedImpl qos4Subscribed, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {

            xml.setAttribute(PRIORITY_LEVEL, qos4Subscribed.data);

        }
    };
}
