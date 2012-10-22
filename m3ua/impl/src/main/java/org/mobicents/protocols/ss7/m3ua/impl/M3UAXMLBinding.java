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
package org.mobicents.protocols.ss7.m3ua.impl;

import java.util.Iterator;
import java.util.Map;

import javolution.xml.XMLBinding;
import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.ss7.m3ua.As;

/**
 * 
 * @author amit bhayani
 *
 */
public class M3UAXMLBinding extends XMLBinding {

	private M3UAManagementImpl m3uaManagement = null;

	public void setM3uaManagement(M3UAManagementImpl m3uaManagement) {
		this.m3uaManagement = m3uaManagement;
	}

	protected XMLFormat getFormat(Class forClass) throws XMLStreamException {
		if (RouteMap.class.equals(forClass)) {
			return ROUTEMAP;
		}
		return super.getFormat(forClass);
	}

	protected final XMLFormat<RouteMap> ROUTEMAP = new XMLFormat<RouteMap>(RouteMap.class) {

		@Override
		public void write(RouteMap obj, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {

			final Map map = (Map) obj;
			for (Iterator it = map.entrySet().iterator(); it.hasNext();) {
				Map.Entry entry = (Map.Entry) it.next();
				AsImpl[] asList = (AsImpl[]) entry.getValue();

				if (asList == null) {
					continue;
				}

				xml.add((String) entry.getKey(), "key", String.class);

				StringBuffer sb = new StringBuffer();
				for (int count = 0; count < asList.length; count++) {
					AsImpl asImpl = asList[count];
					if (asImpl != null) {
						sb.append(asImpl.getName()).append(",");
					}
				}

				String value = sb.toString();

				if (!value.equals("")) {
					// remove last comma
					value = value.substring(0, (value.length() - 1));
				}

				xml.add(value, "value", String.class);
			}
		}

		@Override
		public void read(javolution.xml.XMLFormat.InputElement xml, RouteMap obj) throws XMLStreamException {
			while (xml.hasNext()) {
				String key = xml.get("key", String.class);
				String value = xml.get("value", String.class);
				AsImpl[] asList = new AsImpl[m3uaManagement.getMaxAsForRoute()];

				if (value != null && !value.equals("")) {
					String[] asNames = value.split(",");
					for (int count = 0; count < m3uaManagement.getMaxAsForRoute() && count < asNames.length; count++) {
						String asName = asNames[count];
						As as = m3uaManagement.getAs(asName);
						if (as == null) {
							// TODO add warning
							continue;
						}
						asList[count] = (AsImpl)as;
					}
				}// if (value != null && !value.equals(""))

				obj.put(key, asList);
			}// while
		}

	};
}
