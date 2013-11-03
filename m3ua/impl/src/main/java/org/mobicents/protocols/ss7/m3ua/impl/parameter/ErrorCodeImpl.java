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

package org.mobicents.protocols.ss7.m3ua.impl.parameter;

import org.mobicents.protocols.ss7.m3ua.parameter.ErrorCode;
import org.mobicents.protocols.ss7.m3ua.parameter.Parameter;

/**
 *
 * @author amit bhayani
 *
 */
public class ErrorCodeImpl extends ParameterImpl implements ErrorCode {

    private int code;

    public ErrorCodeImpl(int code) {
        this.code = code;
        this.tag = Parameter.Error_Code;
    }

    public ErrorCodeImpl(byte[] data) {
        this.code = 0;
        this.code |= data[0] & 0xFF;
        this.code <<= 8;
        this.code |= data[1] & 0xFF;
        this.code <<= 8;
        this.code |= data[2] & 0xFF;
        this.code <<= 8;
        this.code |= data[3] & 0xFF;
        this.tag = Parameter.Error_Code;
    }

    @Override
    protected byte[] getValue() {
        byte[] data = new byte[4];
        data[0] = (byte) (code >>> 24);
        data[1] = (byte) (code >>> 16);
        data[2] = (byte) (code >>> 8);
        data[3] = (byte) (code);

        return data;
    }

    public int getCode() {
        return this.code;
    }

    @Override
    public String toString() {
        return String.format("ErrorCode code=%d Error=%s", code, this.getErrorMessage(this.code));
    }

    private String getErrorMessage(int code) {
        switch (code) {
            case Invalid_Version:
                return "Invalid_Version";

            case Unsupported_Message_Class:
                return "Unsupported_Message_Class";

            case Unsupported_Message_Type:
                return "Unsupported_Message_Type";

            case Unsupported_Traffic_Mode_Type:
                return "Unsupported_Traffic_Mode_Type";

            case Unexpected_Message:
                return "Unexpected_Message";

            case Protocol_Error:
                return "Protocol_Error";

            case Invalid_Stream_Identifier:
                return "Invalid_Stream_Identifier";

            case Refused_Management_Blocking:
                return "Refused_Management_Blocking";

            case ASP_Identifier_Required:
                return "ASP_Identifier_Required";

            case Invalid_ASP_Identifier:
                return "Invalid_ASP_Identifier";

            case Invalid_Parameter_Value:
                return "Invalid_Parameter_Value";

            case Parameter_Field_Error:
                return "Parameter_Field_Error";

            case Unexpected_Parameter:
                return "Unexpected_Parameter";

            case Destination_Status_Unknown:
                return "Destination_Status_Unknown";

            case Invalid_Network_Appearance:
                return "Invalid_Network_Appearance";

            case Missing_Parameter:
                return "Missing_Parameter";

            case Invalid_Routing_Context:
                return "Invalid_Routing_Context";

            case No_Configured_AS_for_ASP:
                return "No_Configured_AS_for_ASP";

            default:
                return Integer.toString(code);
        }
    }

}
