/*
 * JBoss, Home of Professional Open Source
 * Copyright XXXX, Red Hat Middleware LLC, and individual contributors as indicated
 * by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.mobicents.protocols.ss7.sccp.tools.twiddle.command;

import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;

import java.io.PrintWriter;

import org.jboss.console.twiddle.command.CommandException;
import org.mobicents.protocols.ss7.indicator.NatureOfAddress;
import org.mobicents.protocols.ss7.indicator.NumberingPlan;
import org.mobicents.protocols.ss7.sccp.impl.router.AddressInformation;
import org.mobicents.protocols.ss7.sccp.impl.router.MTPInfo;
import org.mobicents.protocols.ss7.sccp.impl.router.Rule;

/**
 * @author baranowb
 * 
 */
public class SccpAddRuleCommand extends AbstractSccpCommand {

	private static final char PATTERN = 'p';
	private static final char TRANSLATION = 't';
	private static final char MTP_INFO = 'm';

	private static final char T = 'z';
	private static final char NP = 'x';
	private static final char NOA = 'c';
	private static final char DIGITS = 'v';
	private static final char SSN = 'b';

	private static final char NAME = 'g';
	private static final char OPC = 'h';
	private static final char APC = 'i';
	private static final char SLS = 'y';

	private static final String METHOD = "addRule";
	
	private boolean pattern;
	private boolean translation;
	private boolean mtpinfo;

	// ///////////
	// Pattern //
	// ///////////
	private int p_tt = -1;
	/** numbering plan */
	private NumberingPlan p_np;
	/** nature of address */
	private NatureOfAddress p_noa;
	/** digits */
	private String p_digits;
	/** subsytem number */
	private int p_ssn = -1;

	// ///////////////
	// Translation //
	// ///////////////
	private int t_tt = -1;
	/** numbering plan */
	private NumberingPlan t_np;
	/** nature of address */
	private NatureOfAddress t_noa;
	/** digits */
	private String t_digits;
	/** subsytem number */
	private int t_ssn = -1;

	// ////////////
	// MTP Info //
	// ////////////
	private String mtp_name;
	/** opc */
	private int mtp_opc = -1;
	/** apc */
	private int mtp_apc = -1;
	/** sls */
	private int mtp_sls = -1;

	// //////////////////
	// Rule & arg :) //
	// /////////////////
	private Rule rule;

	/**
	 * @param name
	 * @param desc
	 */
	public SccpAddRuleCommand() {
		super("route.add", "This command adds routing rule to SCCP layer.");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.console.twiddle.command.Command#displayHelp()
	 */
	@Override
	public void displayHelp() {
		PrintWriter out = context.getWriter();

		out.println(desc);
		out.println();
		out.println("usage: " + name + " -p <pattern> -t <translation> -m <mtp-info>");
		out.println();
		out.println("<pattern>|<translation> - specifies information about address pattern or translation translation. Each consists of following options:");
		out.println("         --type                 Value indicating type of translation. It is not mandatory, if absent assumed value is \"-1\".");
		out.println("                                It requires integer argument.");
		out.println("         --numbering-plan       Specifies numberingplan to be used by SCCP to aid it in determinig correct network system.");
		out.println("                                It is not mandatory. It requires one of enumerated values as argument: UNKNOWN,ISDN_TELEPHONY,DATA,TELEX");
		out.println("                                ,MERITIME_MOBILE,LAND_MOBILE,ISDN_MOBILE.");
		out.println("         --nature-of-address    Specifies nature of address to indicate scope of address. It is not mandatory. It requires one of enumerated");
		out.println("                                values as argument: SPARE,SUBSCRIBER,UNKNOWN,NATIONAL,INTERNATIONAL.");
		out.println("         --digits               Specifies string representation of number(digits). It is mandatory and requires argument.");
		out.println("         --ssn                  Specifies subsystem number. It is not mandatory, if absent assumed value is \"-1\".");
		out.println("                                It requires integer argument.");
		out.println("<mtp-info> - specifies mtp information used to route messages:");
		out.println("         --name                 Specifies name of mtp linkset. It is mandatory, requires string argument.");
		out.println("         --opc                  Specifies originating point code. It is mandatory, requires integer argument.");
		out.println("         --apc                  Specifies adjacent point code. It is mandatory, requires integer argument.");
		out.println("         --sls                  Specifies signal link selector. It is mandatory, requires integer argument.");
		out.flush();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.console.twiddle.command.Command#execute(java.lang.String[])
	 */
	@Override
	public void execute(String[] args) throws Exception {
		processArguments(args);
		validate();
		invoke();
	}

	/**
	 * @throws CommandException 
	 * 
	 */
	private void invoke() throws CommandException {
		try {
			super.context.getServer().invoke(super.createObjectName(), METHOD, new Object[]{this.rule}, new String[]{"org.mobicents.protocols.ss7.sccp.impl.router.Rule"});
		} catch (Exception e) {
			throw new CommandException("Failed to add rule.",e);
		} 

	}

	/**
	 * @throws CommandException
	 * 
	 */
	private void validate() throws CommandException {

		if (this.p_digits == null) {
			throw new CommandException("pattern digits not set!");
		}

		if (this.t_digits == null) {
			throw new CommandException("translation digits not set!");
		}

		if (this.mtp_name == null) {
			throw new CommandException("mtp-info name not set!");
		}
		if (this.mtp_opc == -1) {
			throw new CommandException("mtp-info opc not set!");
		}
		if (this.mtp_apc == -1) {
			throw new CommandException("mtp-info apc not set!");
		}
		if (this.mtp_sls == -1) {
			throw new CommandException("mtp-info sls not set!");
		}

		AddressInformation p = new AddressInformation(p_tt, p_np, p_noa, p_digits, p_ssn);
		AddressInformation t = new AddressInformation(t_tt, t_np, t_noa, t_digits, t_ssn);
		MTPInfo i = new MTPInfo(mtp_name, mtp_opc, mtp_apc, mtp_sls);
		this.rule = new Rule(p, t, i);
	}

	/**
	 * @throws CommandException
	 * 
	 */
	private void processArguments(String[] args) throws CommandException {

		String sopts = ":ptm"; // "-" is required to allow non option
		// args(I think)!, ":" is for req,
		// argument, lack of it after option
		// means no args.

		LongOpt[] lopts = {
				new LongOpt("pattern", LongOpt.NO_ARGUMENT, null, PATTERN),
				// Notification source for list
				new LongOpt("translation", LongOpt.NO_ARGUMENT, null, TRANSLATION),
				new LongOpt("mtp-info", LongOpt.NO_ARGUMENT, null, MTP_INFO),

				// long opts for pattern/translation
				new LongOpt("type", LongOpt.REQUIRED_ARGUMENT, null, T), new LongOpt("numbering-plan", LongOpt.REQUIRED_ARGUMENT, null, NP),
				new LongOpt("nature-of-address", LongOpt.REQUIRED_ARGUMENT, null, NOA), new LongOpt("digits", LongOpt.REQUIRED_ARGUMENT, null, DIGITS),
				new LongOpt("ssn", LongOpt.REQUIRED_ARGUMENT, null, SSN),

				// long opts for mtp-info
				new LongOpt("name", LongOpt.REQUIRED_ARGUMENT, null, NAME), new LongOpt("opc", LongOpt.REQUIRED_ARGUMENT, null, OPC),
				new LongOpt("apc", LongOpt.REQUIRED_ARGUMENT, null, APC), new LongOpt("sls", LongOpt.REQUIRED_ARGUMENT, null, SLS),

		};

		// this actually can be hacked as 3 separate opts, but...
		Getopt getopt = new Getopt(null, args, sopts, lopts);
		getopt.setOpterr(false);

		int code;
		while ((code = getopt.getopt()) != -1) {
			switch (code) {
			case ':':
				throw new CommandException("Option requires an argument: " + args[getopt.getOptind() - 1]);

			case '?':
				throw new CommandException("Invalid (or ambiguous) option: " + args[getopt.getOptind() - 1]);

				// switches.
			case PATTERN:
				pattern = true;
				translation = false;
				mtpinfo = false;
				break;
			case TRANSLATION:
				pattern = false;
				translation = true;
				mtpinfo = false;
				break;
			case MTP_INFO:
				pattern = false;
				translation = false;
				mtpinfo = true;
				break;

			// pattern/translation options

			case T:
				assertAddressInfo(args,getopt);
				if (translation) {
					try {
						this.t_tt = Integer.parseInt(getopt.getOptarg());
					} catch (Exception e) {
						throw new CommandException("Failed to parse translation type for translation.");
					}
				} else if (pattern) {
					try {
						this.p_tt = Integer.parseInt(getopt.getOptarg());
					} catch (Exception e) {
						throw new CommandException("Failed to parse translation type for pattern.");
					}
				} 
				break;

			case NP:
				assertAddressInfo(args,getopt);
				if (translation) {
					this.t_np = NumberingPlan.valueOf(getopt.getOptarg());
				} else if (pattern) {
					this.p_np = NumberingPlan.valueOf(getopt.getOptarg());
				} 
				break;

			case NOA:
				assertAddressInfo(args,getopt);
				if (translation) {
					this.t_noa = NatureOfAddress.valueOf(getopt.getOptarg());
				} else if (pattern) {
					this.p_noa = NatureOfAddress.valueOf(getopt.getOptarg());
				} 
				break;

			case DIGITS:
				assertAddressInfo(args,getopt);
				if (translation) {
					this.t_digits = getopt.getOptarg();
				} else if (pattern) {
					this.p_digits = getopt.getOptarg();
				}
				break;

			case SSN:
				assertAddressInfo(args,getopt);
				if (translation) {
					try {
						this.t_ssn = Integer.parseInt(getopt.getOptarg());
					} catch (Exception e) {
						throw new CommandException("Failed to parse ssn for translation.");
					}
				} else if (pattern) {
					try {
						this.p_ssn = Integer.parseInt(getopt.getOptarg());
					} catch (Exception e) {
						throw new CommandException("Failed to parse ssn for pattern.");
					}
				} 
				break;

			// MTP Info options
			case NAME:
				assertMtpInfo(args, getopt);
				this.mtp_name = getopt.getOptarg();

				break;
			case OPC:
				assertMtpInfo(args, getopt);
				this.mtp_opc = Integer.parseInt(getopt.getOptarg());
				break;

			case SLS:
				assertMtpInfo(args, getopt);
				this.mtp_sls = Integer.parseInt(getopt.getOptarg());

				break;
			case APC:
				assertMtpInfo(args, getopt);
				this.mtp_apc = Integer.parseInt(getopt.getOptarg());

				break;

			default:
				throw new CommandException("Command: \"" + getName() + "\", found unexpected opt: " + args[getopt.getOptind() - 1]);

			}

		}

	}

	private void assertMtpInfo(String[] args, Getopt getopt) throws CommandException {
		if (translation) {
			throw new CommandException("translation does not support this option: " + args[getopt.getOptind() - 1]);
		} else if (pattern) {
			throw new CommandException("pattern does not support this option: " + args[getopt.getOptind() - 1]);
		} else if (mtpinfo) {
			return;
		} else {
			throw new CommandException("Option: " + args[getopt.getOptind() - 1] + " must be used withing mtp-info scope.");
		}
	}
	private void assertAddressInfo(String[] args, Getopt getopt) throws CommandException {
		if (translation) {
			
		} else if (pattern) {
			
		} else if (mtpinfo) {
			throw new CommandException("mtp-info does not support this option: " + args[getopt.getOptind() - 1]);
		} else {
			throw new CommandException("Option: " + args[getopt.getOptind() - 1] + " must be used withing pattern/translation scope.");
		}
	}
}
