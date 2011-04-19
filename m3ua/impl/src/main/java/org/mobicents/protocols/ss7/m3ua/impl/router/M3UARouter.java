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
import org.mobicents.protocols.ss7.m3ua.parameter.OPCList;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingKey;
import org.mobicents.protocols.ss7.m3ua.parameter.ServiceIndicators;

/**
 * <p>
 * All the incoming MTP3 traffic is routed as per the rules defined in
 * M3UARouter. The routing decision is based on passed dpc, opc and si from the
 * MTP3 Signaling Information field (SIF) and Service Information Octet (SIO)
 * </p>
 * <p>
 * The {@link M3UARouter} act as tree where each {@link DPCNode} act as parent
 * node containing {@link OPCNode} as leafs. Each {@link OPCNode} further
 * contains {@link SINode} as leafs. The {@link SINode} contains the reference
 * to corresponding {@link As}.
 * </p>
 * 
 * @author amit bhayani
 * 
 */
public class M3UARouter {

	private FastList<DPCNode> dpcList = new FastList<DPCNode>();

	public M3UARouter() {
	}

	/**
	 * <p>
	 * Create a Tree structure adding the reference to passed As. The Tree is
	 * created starting from {@link DPCNode} as parent node containing 'n'
	 * {@link OPCNode} leafs where 'n' is list of OPC passed. For each
	 * {@link OPCNode} there will be 'm' {@link SINode} leafs where 'm' is
	 * number of Service Indicator passed.
	 * </p>
	 * 
	 * <p>
	 * DPC is mandatory while OPC and SI list are optional. If OPC or SI is not
	 * passed the wild card '-1' leaf will be added to parent node
	 * </p>
	 * 
	 * @param rk
	 * @param as
	 * @throws Exception
	 */
	public void addRk(RoutingKey rk, As as) throws Exception {
		int dpc = rk.getDestinationPointCodes()[0].getPointCode();
		OPCList[] opcArray = rk.getOPCLists();

		int opcIntArr[] = null;
		if (opcArray == null) {
			opcIntArr = new int[] { -1 };
		} else {
			opcIntArr = opcArray[0].getPointCodes();
		}

		ServiceIndicators[] siArray = rk.getServiceIndicators();
		short[] siShortArr = null;
		if (siArray == null) {
			siShortArr = new short[] { -1 };
		} else {
			siShortArr = siArray[0].getIndicators();
		}

		for (FastList.Node<DPCNode> n = dpcList.head(), end = dpcList.tail(); (n = n.getNext()) != end;) {
			DPCNode dpcNode = n.getValue();
			if (dpcNode.dpc == dpc) {
				this.addSi(dpcNode, opcIntArr, siShortArr, as);
				return;
			}
		}

		DPCNode dpcNode = new DPCNode(dpc);
		this.addSi(dpcNode, opcIntArr, siShortArr, as);
		this.dpcList.add(dpcNode);

	}

	/**
	 * <p>
	 * Match the passed dpc, opc and si with Tree structure and return the As
	 * from corresponding matched {@link SINode}.
	 * </p>
	 * 
	 * <p>
	 * For example if AS1 is added for Routing Key with DPC=123 only. The tree
	 * will be formed with 123 as {@link DPCNode} parent node and -1 as
	 * {@link OPCNode} leaf and within {@link OPCNode} -1 as {@link SINode}
	 * </p>
	 * 
	 * @param dpc
	 * @param opc
	 * @param si
	 * @return
	 */
	public As getAs(int dpc, int opc, short si) {
		for (FastList.Node<DPCNode> n = dpcList.head(), end = dpcList.tail(); (n = n.getNext()) != end;) {
			DPCNode dpcNode = n.getValue();
			if (dpcNode.dpc == dpc) {
				return dpcNode.getAs(opc, si);
			}
		}
		return null;
	}

	private void addSi(DPCNode dpcNode, int[] opcIntArr, short[] siShortArr, As as) throws Exception {
		for (int i = 0; i < opcIntArr.length; i++) {
			for (int j = 0; j < siShortArr.length; j++) {
				dpcNode.addSi(opcIntArr[i], siShortArr[j], as);
			}
		}
	}
}
