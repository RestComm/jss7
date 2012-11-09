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
package org.mobicents.protocols.ss7.sccp.impl.router;

import javolution.util.FastMap;
import javolution.xml.XMLBinding;
import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 * @author amit bhayani
 * 
 */
public class SccpRouterXMLBinding extends XMLBinding {

	/**
	 * 
	 */
	public SccpRouterXMLBinding() {
		// TODO Auto-generated constructor stub
	}

	protected XMLFormat getFormat(Class forClass) throws XMLStreamException {
		if (Mtp3DestinationMap.class.equals(forClass)) {
			return MTP3DESTINATIONMAP;
		}

		if (Mtp3ServiceAccessPointMap.class.equals(forClass)) {
			return MTP3SERVICEACCESSPOINTMAP;
		}

		if (LongMessageRuleMap.class.equals(forClass)) {
			return LONGMESSAGERULEMAP;
		}

		if (SccpAddressMap.class.equals(forClass)) {
			return SCCPADDRESSMAP;
		}

		if (RuleMap.class.equals(forClass)) {
			return RULEMAP;
		}

		return super.getFormat(forClass);
	}

	protected final XMLFormat<RuleMap> RULEMAP = new XMLFormat<RuleMap>(RuleMap.class) {

		@Override
		public void write(RuleMap obj, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
			for (FastMap.Entry<Integer, Rule> e = obj.head(), end = obj.tail(); (e = e.getNext()) != end;) {
				Integer id = e.getKey();
				Rule rule = e.getValue();

				xml.add(id, "id", Integer.class);
				xml.add(rule, "value", Rule.class);
			}
		}

		@Override
		public void read(javolution.xml.XMLFormat.InputElement xml, RuleMap obj) throws XMLStreamException {
			while (xml.hasNext()) {
				Integer id = xml.get("id", Integer.class);
				Rule rule = xml.get("value", Rule.class);
				obj.put(id, rule);
			}
		}

	};

	protected final XMLFormat<SccpAddressMap> SCCPADDRESSMAP = new XMLFormat<SccpAddressMap>(SccpAddressMap.class) {

		@Override
		public void write(SccpAddressMap obj, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
			for (FastMap.Entry<Integer, SccpAddress> e = obj.head(), end = obj.tail(); (e = e.getNext()) != end;) {
				Integer id = e.getKey();
				SccpAddress sccpAddress = e.getValue();

				xml.add(id, "id", Integer.class);
				xml.add(sccpAddress, "sccpAddress", SccpAddress.class);
			}
		}

		@Override
		public void read(javolution.xml.XMLFormat.InputElement xml, SccpAddressMap obj) throws XMLStreamException {
			while (xml.hasNext()) {
				Integer id = xml.get("id", Integer.class);
				SccpAddress sccpAddress = xml.get("sccpAddress", SccpAddress.class);

				obj.put(id, sccpAddress);
			}
		}

	};

	protected final XMLFormat<LongMessageRuleMap> LONGMESSAGERULEMAP = new XMLFormat<LongMessageRuleMap>(
			LongMessageRuleMap.class) {

		@Override
		public void write(LongMessageRuleMap obj, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
			for (FastMap.Entry<Integer, LongMessageRule> e = obj.head(), end = obj.tail(); (e = e.getNext()) != end;) {
				Integer id = e.getKey();
				LongMessageRule longMessageRule = e.getValue();

				xml.add(id, "id", Integer.class);
				xml.add(longMessageRule, "value", LongMessageRule.class);
			}
		}

		@Override
		public void read(javolution.xml.XMLFormat.InputElement xml, LongMessageRuleMap obj) throws XMLStreamException {
			while (xml.hasNext()) {
				Integer id = xml.get("id", Integer.class);
				LongMessageRule longMessageRule = xml.get("value", LongMessageRule.class);

				obj.put(id, longMessageRule);
			}
		}

	};

	protected final XMLFormat<Mtp3ServiceAccessPointMap> MTP3SERVICEACCESSPOINTMAP = new XMLFormat<Mtp3ServiceAccessPointMap>(
			Mtp3ServiceAccessPointMap.class) {

		@Override
		public void write(Mtp3ServiceAccessPointMap obj, javolution.xml.XMLFormat.OutputElement xml)
				throws XMLStreamException {
			for (FastMap.Entry<Integer, Mtp3ServiceAccessPoint> e = obj.head(), end = obj.tail(); (e = e.getNext()) != end;) {
				Integer id = e.getKey();
				Mtp3ServiceAccessPoint mtp3ServiceAccessPoint = e.getValue();

				xml.add(id, "id", Integer.class);
				xml.add(mtp3ServiceAccessPoint, "value", Mtp3ServiceAccessPoint.class);
			}
		}

		@Override
		public void read(javolution.xml.XMLFormat.InputElement xml, Mtp3ServiceAccessPointMap obj)
				throws XMLStreamException {
			while (xml.hasNext()) {
				Integer id = xml.get("id", Integer.class);
				Mtp3ServiceAccessPoint mtp3ServiceAccessPoint = xml.get("value", Mtp3ServiceAccessPoint.class);

				obj.put(id, mtp3ServiceAccessPoint);
			}
		}

	};

	protected final XMLFormat<Mtp3DestinationMap> MTP3DESTINATIONMAP = new XMLFormat<Mtp3DestinationMap>(
			Mtp3DestinationMap.class) {

		@Override
		public void write(Mtp3DestinationMap obj, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
			for (FastMap.Entry<Integer, Mtp3Destination> e = obj.head(), end = obj.tail(); (e = e.getNext()) != end;) {
				Integer id = e.getKey();
				Mtp3Destination mtp3Destination = e.getValue();

				xml.add(id, "id", Integer.class);
				xml.add(mtp3Destination, "value", Mtp3Destination.class);
			}
		}

		@Override
		public void read(javolution.xml.XMLFormat.InputElement xml, Mtp3DestinationMap obj) throws XMLStreamException {
			while (xml.hasNext()) {
				Integer id = xml.get("id", Integer.class);
				Mtp3Destination mtp3Destination = xml.get("value", Mtp3Destination.class);

				obj.put(id, mtp3Destination);
			}
		}

	};

}
