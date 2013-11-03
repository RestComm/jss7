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

package org.mobicents.protocols.ss7.m3ua.parameter;

/**
 * <p>
 * The network appearance is a number assigned by the signaling gateway and the ASP that, when used along with the signaling
 * point code, uniquely identifies an SS7 node in the SS7 domain.
 * </p>
 * <p>
 * This is used when a signaling gateway is connected to multiple networks, and those networks are in different countries, for
 * example. When this occurs, the SS7 point codes that are assigned could be duplicated. For example, if the node has an
 * appearance in France and also in the United Kingdom, the point code advertised in those two networks could be duplicated
 * because national point codes are of local significance only.
 * </p>
 *
 * @author amit bhayani
 * @author kulikov
 */
public interface NetworkAppearance extends Parameter {

    /**
     * A value in the range 1 to 4294967295 to be used in the Network Appearance
     *
     * @return
     */
    long getNetApp();

}
