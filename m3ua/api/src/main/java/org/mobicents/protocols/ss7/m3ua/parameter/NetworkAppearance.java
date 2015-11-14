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
