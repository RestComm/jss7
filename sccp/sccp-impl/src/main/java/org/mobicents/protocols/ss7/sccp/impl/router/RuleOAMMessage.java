package org.mobicents.protocols.ss7.sccp.impl.router;

/**
 * Declares static messages used by RouterImpl
 * 
 * @author amit bhayani
 * 
 */
public interface RuleOAMMessage {

    public static final String INVALID_COMMAND = "Invalid Command";

    public static final String RULE_ALREADY_EXIST = "Rule already exist";

    public static final String RULE_DOESNT_EXIST = "Rule doesn't exist";

    public static final String RULE_SUCCESSFULLY_ADDED = "Rule successfully added";
    
    public static final String RULE_SUCCESSFULLY_REMOVED = "Rule successfully removed";
    
    public static final String SERVER_ERROR = "Server Error";
}
