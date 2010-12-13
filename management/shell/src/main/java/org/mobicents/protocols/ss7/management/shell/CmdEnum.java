package org.mobicents.protocols.ss7.management.shell;

/**
 * 
 * @author amit bhayani
 *
 */
public enum CmdEnum {

	SHOW(100, "show"), 
	SS7(101, "ss7"), 
	LINKSET(102, "linkset"), 
	STATISTICS(103, "statistics"), 
	ADDLINKSET(104, "addlinkset"), 
	NETWORK_INDICATOR(105, "network-indicator"), 
	NETWORK_INDICATOR_INT(106,"international"), 
	NETWORK_INDICATOR_NAT(107, "national"), 
	NETWORK_INDICATOR_RES(108, "reserved"), 
	NETWORK_INDICATOR_SPA(109, "spare"), 
	LOCAL_PC(110, "local-pc"), 
	ADJACENT_PC(111, "adjacent-pc"), 
	LOCAL_IP(112,"local-ip"), 
	LOCAL_PORT(113, "local-port"), 
	DELETELINKSET(114,"deletelinkset"), 
	ADDLINK(115, "addlink"), 
	LINK(116, "link"), 
	SPAN(117, "span"), 
	CHANNEL(118, "channel"), 
	CODE(119, "code"), 
	DELETELINK(120, "deletelink"), 
	SHUTDOWN(121, "shutdown"), 
	NOSHUTDOWN(122,"noshutdown"), 
	INHIBIT(123, "inhibit"), 
	UNINHIBIT(124, "uninhibit"), 
	EXIT(125, "exit");

	private final int cmdInt;
	private final String cmdStr;

	CmdEnum(int cmdInt, String cmdStr) {
		this.cmdInt = cmdInt;
		this.cmdStr = cmdStr;
	}

	public final int getCmdInt() {
		return cmdInt;
	}

	public String getCmdStr() {
		return cmdStr;
	}

	public static CmdEnum getCommand(int cmdInt) {
		if (cmdInt == 100) {
			return SHOW;
		} else if (cmdInt == 101) {
			return SS7;
		} else if (cmdInt == 102) {
			return LINKSET;
		} else if (cmdInt == 103) {
			return STATISTICS;
		} else if (cmdInt == 104) {
			return ADDLINKSET;
		} else if (cmdInt == 105) {
			return NETWORK_INDICATOR;
		} else if (cmdInt == 106) {
			return NETWORK_INDICATOR_INT;
		} else if (cmdInt == 107) {
			return NETWORK_INDICATOR_NAT;
		} else if (cmdInt == 108) {
			return NETWORK_INDICATOR_RES;
		} else if (cmdInt == 109) {
			return NETWORK_INDICATOR_SPA;
		} else if (cmdInt == 110) {
			return LOCAL_PC;
		} else if (cmdInt == 111) {
			return ADJACENT_PC;
		} else if (cmdInt == 112) {
			return LOCAL_IP;
		} else if (cmdInt == 113) {
			return LOCAL_PORT;
		} else if (cmdInt == 114) {
			return DELETELINKSET;
		} else if (cmdInt == 115) {
			return ADDLINK;
		} else if (cmdInt == 116) {
			return LINK;
		} else if (cmdInt == 117) {
			return SPAN;
		} else if (cmdInt == 118) {
			return CHANNEL;
		} else if (cmdInt == 119) {
			return CODE;
		} else if (cmdInt == 120) {
			return DELETELINK;
		} else if (cmdInt == 121) {
			return SHUTDOWN;
		} else if (cmdInt == 122) {
			return NOSHUTDOWN;
		} else if (cmdInt == 123) {
			return INHIBIT;
		} else if (cmdInt == 124) {
			return UNINHIBIT;
		} else if (cmdInt == 125) {
			return EXIT;
		}
		return null;
	}

	public static CmdEnum getCommand(String cmdStr) {
		if (cmdStr.compareTo("show") == 0) {
			return SHOW;
		} else if (cmdStr.compareTo("ss7") == 0) {
			return SS7;
		} else if (cmdStr.compareTo("linkset") == 0) {
			return LINKSET;
		} else if (cmdStr.compareTo("statistics") == 0) {
			return STATISTICS;
		} else if (cmdStr.compareTo("addlinkset") == 0) {
			return ADDLINKSET;
		} else if (cmdStr.compareTo("network-indicator") == 0) {
			return NETWORK_INDICATOR;
		} else if (cmdStr.compareTo("international") == 0) {
			return NETWORK_INDICATOR_INT;
		} else if (cmdStr.compareTo("national") == 0) {
			return NETWORK_INDICATOR_NAT;
		} else if (cmdStr.compareTo("reserved") == 0) {
			return NETWORK_INDICATOR_RES;
		} else if (cmdStr.compareTo("spare") == 0) {
			return NETWORK_INDICATOR_SPA;
		} else if (cmdStr.compareTo("local-pc") == 0) {
			return LOCAL_PC;
		} else if (cmdStr.compareTo("adjacent-pc") == 0) {
			return ADJACENT_PC;
		} else if (cmdStr.compareTo("local-ip") == 0) {
			return LOCAL_IP;
		} else if (cmdStr.compareTo("local-port") == 0) {
			return LOCAL_PORT;
		} else if (cmdStr.compareTo("deletelinkset") == 0) {
			return DELETELINKSET;
		} else if (cmdStr.compareTo("addlink") == 0) {
			return ADDLINK;
		} else if (cmdStr.compareTo("link") == 0) {
			return LINK;
		} else if (cmdStr.compareTo("span") == 0) {
			return SPAN;
		} else if (cmdStr.compareTo("channel") == 0) {
			return CHANNEL;
		} else if (cmdStr.compareTo("code") == 0) {
			return CODE;
		} else if (cmdStr.compareTo("deletelink") == 0) {
			return DELETELINK;
		} else if (cmdStr.compareTo("shutdown") == 0) {
			return SHUTDOWN;
		} else if (cmdStr.compareTo("noshutdown") == 0) {
			return NOSHUTDOWN;
		} else if (cmdStr.compareTo("inhibit") == 0) {
			return INHIBIT;
		} else if (cmdStr.compareTo("uninhibit") == 0) {
			return UNINHIBIT;
		} else if (cmdStr.compareTo("exit") == 0) {
			return EXIT;
		}
		return null;
	}
}
