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
package org.mobicents.protocols.ss7.sccp;

import java.util.Map;

/**
 *
 * @author Amit Bhayani
 *
 */
public interface Mtp3ServiceAccessPoint {

    int getMtp3Id();

    int getOpc();

    int getNi();

    Mtp3Destination getMtp3Destination(int destId);

    Map<Integer, Mtp3Destination> getMtp3Destinations();

    void addMtp3Destination(int destId, int firstDpc, int lastDpc, int firstSls, int lastSls, int slsMask)
            throws Exception;

    void modifyMtp3Destination(int destId, int firstDpc, int lastDpc, int firstSls, int lastSls, int slsMask)
            throws Exception;

    void removeMtp3Destination(int destId) throws Exception;

    boolean matches(int dpc, int sls);

}
