package org.mobicents.protocols.ss7.m3ua.impl.oam;

/**
 *
 * @author amit bhayani
 *
 */
public interface SCTPOAMMessages {

    String ADD_SERVER_SUCCESS = "Successfully added Server=%s to stack=%s";

    String MODIFY_SERVER_SUCCESS = "Successfully modified Server=%s on stack=%s";

    String REMOVE_SERVER_SUCCESS = "Successfully removed Server=%s from stack=%s";

    String START_SERVER_SUCCESS = "Successfully started Server=%s on stack=%s";

    String STOP_SERVER_SUCCESS = "Successfully stopped Server=%s on stack=%s";

    String ADD_CLIENT_ASSOCIATION_SUCCESS = "Successfully added client Association=%s on stack=%s";

    String MODIFY_CLIENT_ASSOCIATION_SUCCESS = "Successfully modified client Association=%s on stack=%s";

    String ADD_SERVER_ASSOCIATION_SUCCESS = "Successfully added server Association=%s on stack=%s";

    String MODIFY_SERVER_ASSOCIATION_SUCCESS = "Successfully modified server Association=%s on stack=%s";

    String REMOVE_ASSOCIATION_SUCCESS = "Successfully removed Association=%s from stack=%s";

    String NO_SERVER_DEFINED_YET = "No Server defined yet for stack=%s";

    String NO_ASSOCIATION_DEFINED_YET = "No Association defined yet for stack=%s";

}
