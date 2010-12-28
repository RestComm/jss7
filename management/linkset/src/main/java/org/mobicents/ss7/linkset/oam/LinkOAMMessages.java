package org.mobicents.ss7.linkset.oam;

public interface LinkOAMMessages {

    /**
     * Pre defined messages
     */
    public static final String INVALID_COMMAND = "Invalid Command";

    public static final String LINKSET_ALREADY_EXIST = "LinkSet already exist";

    public static final String LINKSET_DOESNT_EXIST = "LinkSet doesn't exist";

    public static final String ACTIVATE_LINKSET_SUCCESSFULLY = "activated linkset successfully";

    public static final String ACTIVATE_LINKSET_FAILED = "activate for linkset failed";

    public static final String ACTIVATE_LINK_SUCCESSFULLY = "activated link successfully";

    public static final String ACTIVATE_LINK_FAILED = "activate for link failed";

    public static final String LINK_DOESNT_EXIST = "Link doesn't exist";

    public static final String LINKSET_SUCCESSFULLY_ADDED = "LinkSet successfully added";

    public static final String LINKSET_SUCCESSFULLY_REMOVED = "LinkSet successfully removed";

    public static final String INCORRECT_LINKSET_TYPE = "LinkSet type is incorrect";

    public static final String LINKSET_ALREADY_ACTIVE = "Linkset already active";

    public static final String LINKSET_NO_LINKS_CONFIGURED = "No Links for this LinkSet are configured";

    public static final String LINK_SUCCESSFULLY_ADDED = "Link successfully added";

    public static final String LINK_SUCCESSFULLY_REMOVED = "Link successfully removed";

    public static final String CANT_DELETE_LINKSET = "Linkset is Available. Can't delete. Please deactivate all links within Linkset and remove each of them before removing this linkset";

    public static final String LINK_ADD_FAILED = "Addition of Link failed";

    public static final String LINK_ALREADY_EXIST = "Link already exist";

    public static final byte[] LINKSET_NOT_DAHDI = "Linkset is of not Dahdi type"
            .getBytes();
    public static final byte[] LINKSET_NOT_DIALOGI = "Linkset is of not Dialogic type"
            .getBytes();
    public static final byte[] LINKSET_NOT_M3UA = "Linkset is of not M3UA type"
            .getBytes();

    public static final String NOT_IMPLEMENTED = "Not implemented yet";

    public static final String CANT_DELETE_LINK = "Link is Active. Can't delete. Please Shutdown first";

    public static final String LINK_ALREADY_ACTIVE = "Link already active";

    public static final String LINK_ALREADY_DEACTIVE = "Link already deactive";

    public static final String LINK_NOT_CONFIGURED = "Not all mandatory parameters are set";

    public static String OPERATION_NOT_SUPPORTED = "Operation not supported";
}
