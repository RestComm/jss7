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
 * @author amit bhayani
 * 
 */
public class ExitHandler extends CommandHandlerWithHelp {

	private final List<CommandLineCompleter> completion;

	/**
	 * 
	 */
	public ExitHandler() {
		this.completion = new ArrayList<CommandLineCompleter>();

		CommandLineCompleter commandLineCompleter = new CommandLineCompleter() {

			@Override
			public int complete(CommandContext ctx, String buffer, int cursor, List<String> candidates) {
				if (ctx.isControllerConnected()) {
					return 0;
				}

				if (buffer.equals("") || buffer.equals("e") || buffer.equals("ex") || buffer.equals("exi")) {
					candidates.add("exit");
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
		if (command.startsWith("exit")) {
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
	public boolean isAvailable(CommandContext commandContext) {
		// Available only in disconnected mode
		if (commandContext.isControllerConnected()) {
			commandContext.printLine("The command is not available in the current context. Please disconnnect first");
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
		if (commands.length != 1) {
			ctx.printLine("Invalid command.");
			return;
		}

		ctx.terminateSession();
	}

}
