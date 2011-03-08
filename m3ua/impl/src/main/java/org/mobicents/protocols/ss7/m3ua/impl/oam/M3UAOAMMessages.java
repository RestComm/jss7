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
package org.mobicents.protocols.ss7.m3ua.impl.oam;

public interface M3UAOAMMessages {

    /**
     * Pre defined messages
     */
    public static final String INVALID_COMMAND = "Invalid Command";

    public static final String ADD_ASP_TO_AS_SUCESSFULL = "Successfully added ASP name=%s to AS name=%s";
    
    public static final String ADD_ASP_TO_AS_FAIL_NO_AS = "No Application Server found for given name %s";
    
    public static final String ADD_ASP_TO_AS_FAIL_NO_ASP = "No Application Server Process found for given name %s";
    
    public static final String ASP_START_SUCESSFULL = "Successfully started ASP name=%s";
    
    public static final String ASP_STOP_SUCESSFULL = "Successfully stopped ASP name=%s";
    
    public static final String CREATE_AS_SUCESSFULL = "Successfully created AS name=%s";

    public static final String CREATE_AS_FAIL_NAME_EXIST = "Creation of AS failed. Other AS with name=%s already exist";

    public static final String CREATE_ASP_SUCESSFULL = "Successfully created ASP name=%s";

    public static final String CREATE_ASP_FAIL_NAME_EXIST = "Creation of ASP failed. Other ASP with name=%s already exist";

    public static final String CREATE_ASP_FAIL_IPPORT_EXIST = "Creation of ASP failed. Other ASP with ip=%s port=%d already exist";

}
