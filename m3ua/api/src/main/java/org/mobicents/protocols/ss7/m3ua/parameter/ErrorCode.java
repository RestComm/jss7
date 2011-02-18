package org.mobicents.protocols.ss7.m3ua.parameter;

/**
 * The Error Code parameter indicates the reason for the Error Message.
 * 
 * @author amit bhayani
 * 
 */
public interface ErrorCode extends Parameter {
    public static final int Invalid_Version = 0x01;
    // public static final int Not_Used_in_M3UA = 0x02;
    public static final int Unsupported_Message_Class = 0x03;
    public static final int Unsupported_Message_Type = 0x04;
    public static final int Unsupported_Traffic_Mode_Type = 0x05;
    public static final int Unexpected_Message = 0x06;
    public static final int Protocol_Error = 0x07;
    // public static final int Not_Used_in_M3UA2 = 0x08;
    public static final int Invalid_Stream_Identifier = 0x09;
    public static final int Refused_Management_Blocking = 0x0d;
    public static final int ASP_Identifier_Required = 0x0e;
    public static final int Invalid_ASP_Identifier = 0x0f;
    public static final int Invalid_Parameter_Value = 0x11;
    public static final int Parameter_Field_Error = 0x12;
    public static final int Unexpected_Parameter = 0x13;
    public static final int Destination_Status_Unknown = 0x14;
    public static final int Invalid_Network_Appearance = 0x15;
    public static final int Missing_Parameter = 0x16;
    public static final int Invalid_Routing_Context = 0x19;
    public static final int No_Configured_AS_for_ASP = 0x1a;

    public int getCode();
}
