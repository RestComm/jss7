package org.mobicents.protocols.ss7.mtp.oam;

public interface ShellMessages {

	/**
	 * Pre defined messages
	 */
	public static final byte[] LINKSET_ALREADY_EXIST = "LinkSet already exist"
			.getBytes();

	public static final byte[] LINKSET_DOESNT_EXIST = "LinkSet doesn't exist"
			.getBytes();

	public static final byte[] NOSHUTDOWN_LINKSET_SUCCESSFULLY = "noshutdown for Linkset successfull"
			.getBytes();

	public static final byte[] NOSHUTDOWN_LINKSET_FAILED = "noshutdown for Linkset failed"
			.getBytes();

	public static final byte[] NOSHUTDOWN_LINK_SUCCESSFULLY = "noshutdown for Link successfull"
			.getBytes();

	public static final byte[] NOSHUTDOWN_LINK_FAILED = "noshutdown for Link failed"
			.getBytes();

	public static final byte[] NETWORK_INDICATOR_SUCCESSFULLY_SET = "Network Indicator successfully set"
			.getBytes();

	public static final byte[] OPC_SUCCESSFULLY_SET = "Originating Point Code successfully set"
			.getBytes();

	public static final byte[] APC_SUCCESSFULLY_SET = "Adjacent Point Code successfully set"
			.getBytes();

	public static final byte[] LOCAL_IP_PORT_SUCCESSFULLY_SET = "Local ip:port successfully set"
			.getBytes();

	public static final byte[] LINK_DOESNT_EXIST = "Link doesn't exist"
			.getBytes();

	public static final byte[] LINKSET_SUCCESSFULLY_ADDED = "LinkSet successfully added"
			.getBytes();

	public static final byte[] INCORRECT_LINKSET_TYPE = "LinkSet type is incorrect"
			.getBytes();

	public static final byte[] LINKSET_ALREADY_ACTIVE = "Linkset already active"
			.getBytes();

	public static final byte[] LINKSET_NO_LINKS_CONFIGURED = "No Links for this LinkSet are configured"
			.getBytes();

	public static final byte[] LINK_SUCCESSFULLY_ADDED = "Link successfully added"
			.getBytes();

	public static final byte[] LINK_SPAN_SUCCESSFULL = "Span successfully set for Link"
			.getBytes();

	public static final byte[] LINK_CHANNEL_SUCCESSFULL = "Channel successfully set for Link"
			.getBytes();

	public static final byte[] LINK_CODE_SUCCESSFULL = "Code successfully set for Link"
			.getBytes();

	public static final byte[] LINK_ADD_FAILED = "Addition of Link failed"
			.getBytes();

	public static final byte[] LINK_ALREADY_EXIST = "Link already exist"
			.getBytes();

	public static final byte[] LINKSET_NOT_DAHDI = "Linkset is of not Dahdi type"
			.getBytes();
	public static final byte[] LINKSET_NOT_DIALOGI = "Linkset is of not Dialogic type"
			.getBytes();
	public static final byte[] LINKSET_NOT_M3UA = "Linkset is of not M3UA type"
			.getBytes();

	public static final byte[] NOT_IMPLEMENTED = "Not implemented yet"
			.getBytes();

}
