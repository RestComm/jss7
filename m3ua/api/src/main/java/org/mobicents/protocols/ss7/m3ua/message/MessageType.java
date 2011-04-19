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

package org.mobicents.protocols.ss7.m3ua.message;

/**
 * Defines valid message types.
 * 
 * @author kulikov
 */
public interface MessageType {
    //Management (MGMT) Messages
    public final static int ERROR = 0;
    public final static int NOTIFY = 1;    
    
    //Transfer Messages
    public final static int PAYLOAD = 1;    
    
    //SS7 Signalling Network Management (SSNM) Messages
    public final static int DESTINATION_UNAVAILABLE = 1;
    public final static int DESTINATION_AVAILABLE = 2;
    public final static int DESTINATION_STATE_AUDIT = 3;
    public final static int SIGNALING_CONGESTION = 4;
    public final static int DESTINATION_USER_PART_UNAVAILABLE = 5;
    public final static int DESTINATION_RESTRICTED = 6;    
    
    //ASP State Maintenance (ASPSM) Messages
    public final static int ASP_UP = 1;
    public final static int ASP_DOWN = 2;
    public final static int HEARTBEAT = 3;
    public final static int ASP_UP_ACK = 4;
    public final static int ASP_DOWN_ACK = 5;
    public final static int HEARTBEAT_ACK = 6;    
    
    //ASP Traffic Maintenance (ASPTM) Messages
    public final static int ASP_ACTIVE = 1;
    public final static int ASP_INACTIVE = 2;
    public final static int ASP_ACTIVE_ACK = 3;
    public final static int ASP_INACTIVE_ACK = 4;    
    
    //Routing Key Management (RKM) Messages
    public final static int REG_REQUEST = 1;
    public final static int REG_RESPONSE = 2;
    public final static int DEREG_REQUEST = 3;
    public final static int DEREG_RESPONSE = 4;
}
