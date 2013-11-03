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
 * The Error Code parameter indicates the reason for the Error Message.
 *
 * @author amit bhayani
 *
 */
public interface ErrorCode extends Parameter {
    int Invalid_Version = 0x01;
    // final int Not_Used_in_M3UA = 0x02;
    int Unsupported_Message_Class = 0x03;
    int Unsupported_Message_Type = 0x04;
    int Unsupported_Traffic_Mode_Type = 0x05;
    int Unexpected_Message = 0x06;
    int Protocol_Error = 0x07;
    // int Not_Used_in_M3UA2 = 0x08;
    int Invalid_Stream_Identifier = 0x09;
    int Refused_Management_Blocking = 0x0d;
    int ASP_Identifier_Required = 0x0e;
    int Invalid_ASP_Identifier = 0x0f;
    int Invalid_Parameter_Value = 0x11;
    int Parameter_Field_Error = 0x12;
    int Unexpected_Parameter = 0x13;
    int Destination_Status_Unknown = 0x14;
    int Invalid_Network_Appearance = 0x15;
    int Missing_Parameter = 0x16;
    int Invalid_Routing_Context = 0x19;
    int No_Configured_AS_for_ASP = 0x1a;

    int getCode();
}
