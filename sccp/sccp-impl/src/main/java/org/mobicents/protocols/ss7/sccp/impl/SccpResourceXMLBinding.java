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
package org.mobicents.protocols.ss7.sccp.impl;

import javolution.util.FastMap;
import javolution.xml.XMLBinding;
import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

/**
 * @author amit bhayani
 *
 */
public class SccpResourceXMLBinding extends XMLBinding {

    public SccpResourceXMLBinding() {
        // TODO Auto-generated constructor stub
    }

    protected XMLFormat getFormat(Class forClass) throws XMLStreamException {
        if (RemoteSubSystemMap.class.equals(forClass)) {
            return REMOTESUBSYSTEMMAP;
        }

        if (RemoteSignalingPointCodeMap.class.equals(forClass)) {
            return REMOTESIGNALINGPOINTCODEMAP;
        }

        if (ConcernedSignalingPointCodeMap.class.equals(forClass)) {
            return CONCERNEDSIGNALINGPOINTCODEMAP;
        }

        return super.getFormat(forClass);
    }

    protected final XMLFormat<ConcernedSignalingPointCodeMap> CONCERNEDSIGNALINGPOINTCODEMAP = new XMLFormat<ConcernedSignalingPointCodeMap>(
            ConcernedSignalingPointCodeMap.class) {

        @Override
        public void write(ConcernedSignalingPointCodeMap map, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {
            for (FastMap.Entry<Integer, ConcernedSignalingPointCodeImpl> e = map.head(), end = map.tail(); (e = e.getNext()) != end;) {
                Integer id = e.getKey();
                ConcernedSignalingPointCodeImpl concernedSignalingPointCodeImpl = e.getValue();

                xml.add(id, "id", Integer.class);
                xml.add(concernedSignalingPointCodeImpl, "value", ConcernedSignalingPointCodeImpl.class);
            }
        }

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, ConcernedSignalingPointCodeMap obj)
                throws XMLStreamException {
            while (xml.hasNext()) {
                Integer id = xml.get("id", Integer.class);
                ConcernedSignalingPointCodeImpl concernedSignalingPointCodeImpl = xml.get("value",
                        ConcernedSignalingPointCodeImpl.class);

                obj.put(id, concernedSignalingPointCodeImpl);
            }
        }

    };

    protected final XMLFormat<RemoteSignalingPointCodeMap> REMOTESIGNALINGPOINTCODEMAP = new XMLFormat<RemoteSignalingPointCodeMap>(
            RemoteSignalingPointCodeMap.class) {

        @Override
        public void write(RemoteSignalingPointCodeMap map, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {
            for (FastMap.Entry<Integer, RemoteSignalingPointCodeImpl> e = map.head(), end = map.tail(); (e = e.getNext()) != end;) {
                Integer id = e.getKey();
                RemoteSignalingPointCodeImpl remoteSignalingPointCodeImpl = e.getValue();

                xml.add(id, "id", Integer.class);
                xml.add(remoteSignalingPointCodeImpl, "value", RemoteSignalingPointCodeImpl.class);
            }
        }

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, RemoteSignalingPointCodeMap obj) throws XMLStreamException {
            while (xml.hasNext()) {
                Integer id = xml.get("id", Integer.class);
                RemoteSignalingPointCodeImpl remoteSignalingPointCodeImpl = xml
                        .get("value", RemoteSignalingPointCodeImpl.class);

                obj.put(id, remoteSignalingPointCodeImpl);
            }
        }

    };

    protected final XMLFormat<RemoteSubSystemMap> REMOTESUBSYSTEMMAP = new XMLFormat<RemoteSubSystemMap>(
            RemoteSubSystemMap.class) {
        @Override
        public void write(RemoteSubSystemMap map, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
            for (FastMap.Entry<Integer, RemoteSubSystemImpl> e = map.head(), end = map.tail(); (e = e.getNext()) != end;) {
                Integer id = e.getKey();
                RemoteSubSystemImpl remoteSubSystemImpl = e.getValue();

                xml.add(id, "id", Integer.class);
                xml.add(remoteSubSystemImpl, "value", RemoteSubSystemImpl.class);
            }
        }

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, RemoteSubSystemMap obj) throws XMLStreamException {
            while (xml.hasNext()) {
                Integer id = xml.get("id", Integer.class);
                RemoteSubSystemImpl remoteSubSystemImpl = xml.get("value", RemoteSubSystemImpl.class);

                obj.put(id, remoteSubSystemImpl);
            }
        }
    };
}
