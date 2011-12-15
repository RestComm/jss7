package org.mobicents.protocols.ss7.m3ua.impl;

import java.util.Iterator;
import java.util.Map;

import javolution.xml.XMLBinding;
import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

/**
 * 
 * @author amit bhayani
 *
 */
public class M3UAXMLBinding extends XMLBinding {

	private M3UAManagement m3uaManagement = null;

	public void setM3uaManagement(M3UAManagement m3uaManagement) {
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
				As[] asList = (As[]) entry.getValue();

				if (asList == null) {
					continue;
				}

				xml.add((String) entry.getKey(), "key", String.class);

				StringBuffer sb = new StringBuffer();
				for (int count = 0; count < asList.length; count++) {
					As as = asList[count];
					if (as != null) {
						sb.append(as.getName()).append(",");
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
				As[] asList = new As[m3uaManagement.getMaxAsForRoute()];

				if (value != null && !value.equals("")) {
					String[] asNames = value.split(",");
					for (int count = 0; count < m3uaManagement.getMaxAsForRoute() && count < asNames.length; count++) {
						String asName = asNames[count];
						As as = m3uaManagement.getAs(asName);
						if (as == null) {
							// TODO add warning
							continue;
						}
						asList[count] = as;
					}
				}// if (value != null && !value.equals(""))

				obj.put(key, asList);
			}// while
		}

	};
}
