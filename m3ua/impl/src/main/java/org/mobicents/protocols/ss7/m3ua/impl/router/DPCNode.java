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

package org.mobicents.protocols.ss7.m3ua.impl.router;

import javolution.util.FastList;

import org.mobicents.protocols.ss7.m3ua.impl.AsImpl;

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

    protected void addSi(int opc, int si, AsImpl asImpl) throws Exception {
        for (FastList.Node<OPCNode> n = opcList.head(), end = opcList.tail(); (n = n.getNext()) != end;) {
            OPCNode opcNode = n.getValue();
            if (opcNode.opc == opc) {
                opcNode.addSi(si, asImpl);
                return;
            }
        }

        OPCNode opcNode = new OPCNode(this.dpc, opc);
        opcNode.addSi(si, asImpl);
        opcList.add(opcNode);

        if (opcNode.opc == -1) {
            // we have wild card OPCNode. Use this if no matching OPCNode
            // found while finding AS
            wildCardOpcNode = opcNode;
        }

    }

    protected AsImpl getAs(int opc, short si) {
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
