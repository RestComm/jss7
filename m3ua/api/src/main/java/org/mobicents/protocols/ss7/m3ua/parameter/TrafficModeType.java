/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
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
 * The optional Traffic Mode Type parameter identifies the traffic mode of
 * operation of the ASP(s) within an Application Server.
 * </p>
 * <p>
 * The valid values for Traffic Mode Type are shown in the following table:
 * <ul>
 * <li>1 Override</li>
 * <li>2 Loadshare</li>
 * <li>3 Broadcast</li>
 * </ul>
 * </p>
 * 
 * @author amit bhayani
 * 
 */
public interface TrafficModeType extends Parameter {
    
    public static final int Override = 1;
    public static final int Loadshare = 2;
    public static final int Broadcast = 3;

    public int getMode();

}
