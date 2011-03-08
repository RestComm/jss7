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
 * The Deregistration Result Status field indicates the success or the reason
 * for failure of the deregistration.
 * </p>
 * 
 * <p>
 * Its values may be:
 * <ul>
 * <li>0 Successfully Deregistered</li>
 * <li>1 Error - Unknown</li>
 * <li>2 Error - Invalid Routing Context</li>
 * <li>3 Error - Permission Denied</li>
 * <li>4 Error - Not Registered</li>
 * <li>5 Error - ASP Currently Active for Routing Context</li>
 * </ul>
 * </p>
 * 
 * @author amit bhayani
 * 
 */
public interface DeregistrationStatus extends Parameter {
    public int getStatus();
}
