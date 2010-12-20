package org.mobicents.protocols.ss7.management.shell;

import java.util.HashMap;

/**
 * 
 * @author amit bhayani
 * 
 */
public class ShellCommand {

	private static HashMap<Integer, ShellCommand> intVsCmdEnum = new HashMap<Integer, ShellCommand>();
	private static HashMap<String, ShellCommand> stringVsCmdEnum = new HashMap<String, ShellCommand>();

	public static final ShellCommand MTP = new ShellCommand(1, "mtp");
	public static final ShellCommand SCCP = new ShellCommand(2, "sccp");
	public static final ShellCommand EXIT = new ShellCommand(9, "exit");

	public static final ShellCommand SHOW = new ShellCommand(100, "show");
	public static final ShellCommand SS7 = new ShellCommand(101, "ss7");
	public static final ShellCommand LINKSET = new ShellCommand(102, "linkset");
	public static final ShellCommand STATISTICS = new ShellCommand(103,
			"statistics");
	public static final ShellCommand ADDLINKSET = new ShellCommand(104,
			"addlinkset");
	public static final ShellCommand LINKSET_DAHDI = new ShellCommand(105,
			"dahdi");
	public static final ShellCommand LINKSET_DIALOGIC = new ShellCommand(106,
			"dialogic");
	public static final ShellCommand LINKSET_M3UA = new ShellCommand(107,
			"m3ua");
	public static final ShellCommand NETWORK_INDICATOR = new ShellCommand(108,
			"network-indicator");
	public static final ShellCommand NETWORK_INDICATOR_INT = new ShellCommand(
			109, "international");
	public static final ShellCommand NETWORK_INDICATOR_NAT = new ShellCommand(
			110, "national");
	public static final ShellCommand NETWORK_INDICATOR_RES = new ShellCommand(
			111, "reserved");
	public static final ShellCommand NETWORK_INDICATOR_SPA = new ShellCommand(
			112, "spare");
	public static final ShellCommand LOCAL_PC = new ShellCommand(113,
			"local-pc");
	public static final ShellCommand ADJACENT_PC = new ShellCommand(114,
			"adjacent-pc");
	public static final ShellCommand LOCAL_IP = new ShellCommand(115,
			"local-ip");
	public static final ShellCommand LOCAL_PORT = new ShellCommand(116,
			"local-port");
	public static final ShellCommand DELETELINKSET = new ShellCommand(117,
			"deletelinkset");
	public static final ShellCommand ADDLINK = new ShellCommand(118, "addlink");
	public static final ShellCommand LINK = new ShellCommand(119, "link");
	public static final ShellCommand SPAN = new ShellCommand(120, "span");
	public static final ShellCommand CHANNEL = new ShellCommand(121, "channel");
	public static final ShellCommand CODE = new ShellCommand(122, "code");
	public static final ShellCommand DELETELINK = new ShellCommand(123,
			"deletelink");
	public static final ShellCommand SHUTDOWN = new ShellCommand(124,
			"shutdown");
	public static final ShellCommand NOSHUTDOWN = new ShellCommand(125,
			"noshutdown");
	public static final ShellCommand INHIBIT = new ShellCommand(126, "inhibit");
	public static final ShellCommand UNINHIBIT = new ShellCommand(127,
			"uninhibit");

	private final int cmdInt;
	private final String cmdStr;

	private ShellCommand(final int cmdInt, final String cmdStr) {
		this.cmdInt = cmdInt;
		this.cmdStr = cmdStr;

		intVsCmdEnum.put(this.cmdInt, this);
		stringVsCmdEnum.put(this.cmdStr, this);

	}

	public final int getCmdInt() {
		return this.cmdInt;
	}

	public String getCmdStr() {
		return this.cmdStr;
	}

	public static ShellCommand getCommand(int cmdInt) {
		return intVsCmdEnum.get(cmdInt);

	}

	public static ShellCommand getCommand(String cmdStr) {
		return stringVsCmdEnum.get(cmdStr);
	}
}
