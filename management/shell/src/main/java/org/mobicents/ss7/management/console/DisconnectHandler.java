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

import java.util.ArrayList;
import java.util.List;

/**
 * @author abhayani
 * 
 */
public class DisconnectHandler extends CommandHandlerWithHelp {

	private final List<CommandLineCompleter> completion;

	/**
	 * 
	 */
	public DisconnectHandler() {

		this.completion = new ArrayList<CommandLineCompleter>();

		CommandLineCompleter commandLineCompleter = new CommandLineCompleter() {

			@Override
			public int complete(CommandContext ctx, String buffer, int cursor, List<String> candidates) {

				if (!ctx.isControllerConnected()) {
					return 0;
				}

				if (buffer.equals("") || buffer.equals("d") || buffer.equals("di") || buffer.equals("dis") || buffer.equals("disc") || buffer.equals("disco")
						|| buffer.equals("discon") || buffer.equals("disconn") || buffer.equals("disconne") || buffer.equals("disconnec")) {
					candidates.add("disconnect");
				}
				return 0;
			}

		};

		this.completion.add(commandLineCompleter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.ss7.management.console.CommandHandler#handles(java.lang
	 * .String)
	 */
	@Override
	public boolean handles(String command) {
		if (command.startsWith("disconnect")) {
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.ss7.management.console.CommandHandler#
	 * getCommandLineCompleterList()
	 */
	@Override
	public List<CommandLineCompleter> getCommandLineCompleterList() {
		return this.completion;
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
		if (!ctx.isControllerConnected()) {
			ctx.printLine("Already disconnected");
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
		String[] commands = commandLine.split(" ");

		if (commands.length == 1) {
			ctx.sendMessage("disconnect");
			ctx.disconnectController();
		} else {
			ctx.printLine("Invalid command.");
		}

	}
}
