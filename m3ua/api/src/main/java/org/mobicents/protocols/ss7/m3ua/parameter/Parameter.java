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
 * v. 2.0 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */

package org.mobicents.protocols.ss7.m3ua.parameter;

/**
 * Defines list of valid parameters.
 * 
 * @author kulikov
 */
public interface Parameter {
    public final static short INFO_String = 0x0004;
    public final static short Routing_Context = 0x0006;
    public final static short Diagnostic_Information = 0x0007;
    public final static short Heartbeat_Data = 0x0009;
    public final static short Traffic_Mode_Type = 0x000b;
    public final static short Error_Code = 0x000c;
    public final static short Status = 0x000d;
    public final static short ASP_Identifier = 0x0011;
    public final static short Affected_Point_Code = 0x0012;
    public final static short Correlation_ID = 0x0013;
    public final static short Network_Appearance = 0x0200;
    public final static short User_Cause = 0x0204;
    public final static short Congestion_Indications = 0x0205;
    public final static short Concerned_Destination = 0x0206;
    public final static short Routing_Key = 0x0207;
    public final static short Registration_Result = 0x0208;
    public final static short Deregistration_Result = 0x0209;
    public final static short Local_Routing_Key_Identifier = 0x020a;
    public final static short Destination_Point_Code = 0x020b;
    public final static short Service_Indicators = 0x020c;
    public final static short Originating_Point_Code_List = 0x020e;
    public final static short Circuit_Range = 0x020f;
    public final static short Protocol_Data = 0x0210;
    public final static short Registration_Status = 0x0212;
    public final static short Deregistration_Status = 0x0213;

}
