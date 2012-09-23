/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.mobicents.ss7.management.console;

/**
 * @author amit bhayani
 * 
 */
public class ConnectHandler extends CommandHandlerWithHelp {

	static final Tree commandTree = new Tree("connect");

	/**
	 * 
	 */
	public ConnectHandler() {
		super(commandTree, DISCONNECT_MANDATORY_FLAG);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.ss7.management.console.CommandHandler#isAvailable(org.mobicents
	 * .ss7.management.console.CommandContext)
	 */
	@Override
	public boolean isAvailable(CommandContext ctx) {
		if (ctx.isControllerConnected()) {
			ctx.printLine("Already connected");
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.ss7.management.console.CommandHandler#handle(org.mobicents
	 * .ss7.management.console.CommandContext, java.lang.String)
	 */
	@Override
	public void handle(CommandContext ctx, String commandLine) {
		// TODO Validate command

		if (commandLine.contains("--help")) {
			this.printHelp(commandLine, ctx);
			return;
		}

		String[] commands = commandLine.split(" ");

		if (commands.length == 1) {
			ctx.connectController(null, -1);
		} else if (commands.length == 2) {
			if (commandLine.contains("--help")) {
				this.printHelp("help/connect.txt", ctx);
				return;
			}
		} else if (commands.length == 3) {
			String host = commands[1];
			try {
				int port = Integer.parseInt(commands[2]);
				ctx.connectController(host, port);
			} catch (NumberFormatException ne) {
				ctx.printLine("Port must be from 1 to 65535");
			}

		} else {
			ctx.printLine("Invalid command.");
		}

	}
}
