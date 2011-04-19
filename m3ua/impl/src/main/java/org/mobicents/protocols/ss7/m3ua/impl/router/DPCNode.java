/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.mobicents.protocols.ss7.m3ua.impl.router;

import javolution.util.FastList;

import org.mobicents.protocols.ss7.m3ua.impl.As;

/**
 * <p>
 * dpc is mandatory in deciding the correct AS to route the MTP3 traffic.
 * </p>
 * 
 * @author amit bhayani
 * 
 */
public class DPCNode {
	int dpc = -1;

	private FastList<OPCNode> opcList = new FastList<OPCNode>();

	private OPCNode wildCardOpcNode = null;

	public DPCNode(int dpc) {
		this.dpc = dpc;
	}

	protected void addSi(int opc, int si, As as) throws Exception {
		for (FastList.Node<OPCNode> n = opcList.head(), end = opcList.tail(); (n = n.getNext()) != end;) {
			OPCNode opcNode = n.getValue();
			if (opcNode.opc == opc) {
				opcNode.addSi(si, as);
				return;
			}
		}

		OPCNode opcNode = new OPCNode(this.dpc, opc);
		opcNode.addSi(si, as);
		opcList.add(opcNode);

		if (opcNode.opc == -1) {
			// we have wild card OPCNode. Use this if no matching OPCNode
			// found while finding AS
			wildCardOpcNode = opcNode;
		}

	}

	protected As getAs(int opc, short si) {
		for (FastList.Node<OPCNode> n = opcList.head(), end = opcList.tail(); (n = n.getNext()) != end;) {
			OPCNode opcNode = n.getValue();
			if (opcNode.opc == opc) {
				return opcNode.getAs(si);
			}
		}

		if (wildCardOpcNode != null) {
			return wildCardOpcNode.getAs(si);
		}
		return null;
	}
}
