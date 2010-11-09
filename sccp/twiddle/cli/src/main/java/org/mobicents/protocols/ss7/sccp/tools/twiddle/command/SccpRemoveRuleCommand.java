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

/**
 * @author baranowb
 * 
 */
public class SccpRemoveRuleCommand extends AbstractSccpCommand {

	private static final String METHOD = "removeRule";
	private static final char NUMBER = 'n';
	private int ruleNum = -1;

	/**
	 * @param name
	 * @param desc
	 */
	public SccpRemoveRuleCommand() {
		super("route.remove", "This command removes routing rule from SCCP layer.");

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
		out.println("usage: " + name + " <--number=#>");
		out.println("         -n, --number         Specifies rule number to be removed. It is mandatory and requires integer argument");
		out.println();
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

	private void processArguments(String[] args) throws CommandException {

		String sopts = ":n:"; // "-" is required to allow non option
		// args(I think)!, ":" is for req,
		// argument, lack of it after option
		// means no args.

		LongOpt[] lopts = { new LongOpt("number", LongOpt.REQUIRED_ARGUMENT, null, NUMBER),

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
			case NUMBER:

				try {
					String opt = getopt.getOptarg();
					this.ruleNum = Integer.parseInt(opt);
				} catch (Exception e) {
					throw new CommandException("Failed to parse rule number.", e);
				}
				break;

			default:
				throw new CommandException("Command: \"" + getName() + "\", found unexpected opt: " + args[getopt.getOptind() - 1]);

			}

		}

	}

	private void validate() throws CommandException {
		if (ruleNum == -1) {
			throw new CommandException("Rule number must be specified!");
		}
	}

	private void invoke() throws CommandException {
		try {
			super.context.getServer().invoke(super.createObjectName(), METHOD, new Object[] { this.ruleNum }, new String[] { int.class.toString() });
		} catch (Exception e) {
			throw new CommandException("Failed to add rule.", e);
		}

	}

}
