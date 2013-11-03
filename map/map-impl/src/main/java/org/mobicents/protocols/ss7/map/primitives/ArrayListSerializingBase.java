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

package org.mobicents.protocols.ss7.map.primitives;

import java.util.ArrayList;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class ArrayListSerializingBase<T> {

    private ArrayList<T> data = new ArrayList<T>();
    private String elementName;
    private Class classDef;

    public ArrayListSerializingBase(String elementName, Class classDef, ArrayList<T> data) {
        this.data = data;
        this.classDef = classDef;
        this.elementName = elementName;
    }

    public ArrayListSerializingBase(String elementName, Class classDef) {
        this.elementName = elementName;
        this.classDef = classDef;
    }

    public ArrayList<T> getData() {
        return this.data;
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<ArrayListSerializingBase> ARRAY_LIST_SERIALIZING_BASE_XML = new XMLFormat<ArrayListSerializingBase>(
            ArrayListSerializingBase.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, ArrayListSerializingBase data) throws XMLStreamException {
            data.data.clear();

            while (xml.hasNext()) {
                Object pe = xml.get(data.elementName, data.classDef);
                data.data.add(pe);
            }
        }

        @Override
        public void write(ArrayListSerializingBase data, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
            if (data.data != null && data.data.size() > 0) {
                for (int i1 = 0; i1 < data.data.size(); i1++) {
                    Object pe = data.data.get(i1);
                    if (pe != null)
                        xml.add(pe, data.elementName, data.classDef);
                }
            }
        }
    };

}
