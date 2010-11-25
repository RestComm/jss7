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

import java.io.PrintWriter;

import org.mobicents.protocols.ss7.sccp.impl.router.Rule;

/**
 * @author baranowb
 * 
 */
public class SccpListRuleCommand extends AbstractSccpCommand {

	private static final String METHOD = "getRules";
	private static final String NO_RULES = "No rules present.";
	/**
	 * @param name
	 * @param desc
	 */
	public SccpListRuleCommand() {
		super("route.list", "This command lists routing rules present in SCCP layer.");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.console.twiddle.command.Command#displayHelp()
	 */
	public void displayHelp() {
		PrintWriter out = context.getWriter();

		out.println(desc);
		out.println();
		out.println("usage: " + name + " ");
		out.println();
		out.flush();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.console.twiddle.command.Command#execute(java.lang.String[])
	 */
	public void execute(String[] args) throws Exception {
		Rule[] rules = (Rule[]) super.context.getServer().invoke(super.createObjectName(), METHOD, new Object[] {}, new String[] {});
		// default impl of display;
		if (!context.isQuiet()) {
			if (rules == null || rules.length == 0) {

				PrintWriter out = context.getWriter();
				out.println(NO_RULES);
				out.flush();
			}else
			{
				StringBuffer sb = new StringBuffer();
				for(Rule r:rules)
				{
					sb.append("#            :").append(r.getNo()).append("\n");
					sb.append("Pattern      :").append(r.getPattern()).append("\n");
					sb.append("Translation  :").append(r.getTranslation()).append("\n");
					sb.append("MtpInfo      :").append(r.getTranslation()).append("\n\n");
				}
				PrintWriter out = context.getWriter();
				out.println(sb);
				out.flush();
			}
		}
	}

}
