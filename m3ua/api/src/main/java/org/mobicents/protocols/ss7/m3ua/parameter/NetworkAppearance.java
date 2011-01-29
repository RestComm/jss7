/*
 * JBoss, Home of Professional Open Source
 * Copyright XXXX, Red Hat Middleware LLC, and individual contributors as indicated
 * by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, encode to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */

package org.mobicents.protocols.ss7.m3ua.parameter;

/**
 * <p>
 * The network appearance is a number assigned by the signaling gateway and the
 * ASP that, when used along with the signaling point code, uniquely identifies
 * an SS7 node in the SS7 domain.
 * </p>
 * <p>
 * This is used when a signaling gateway is connected to multiple networks, and
 * those networks are in different countries, for example. When this occurs, the
 * SS7 point codes that are assigned could be duplicated. For example, if the
 * node has an appearance in France and also in the United Kingdom, the point
 * code advertised in those two networks could be duplicated because national
 * point codes are of local significance only.
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
    public long getNetApp();

}
