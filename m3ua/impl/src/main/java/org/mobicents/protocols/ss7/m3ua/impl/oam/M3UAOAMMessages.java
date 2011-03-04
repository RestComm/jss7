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
