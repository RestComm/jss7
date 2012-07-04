package org.mobicents.ss7.management.console.impl;

import java.util.ArrayList;
import java.util.List;

import org.mobicents.ss7.management.console.CommandContext;
import org.mobicents.ss7.management.console.CommandHandlerWithHelp;
import org.mobicents.ss7.management.console.CommandLineCompleter;

/**
 * @author amit bhayani
 * 
 */
public class LinksetCommandHandler extends CommandHandlerWithHelp {

	private final List<CommandLineCompleter> completion;

	public LinksetCommandHandler() {
		this.completion = new ArrayList<CommandLineCompleter>();

		CommandLineCompleter commandLineCompleter = new CommandLineCompleter() {
			@Override
			public int complete(CommandContext ctx, String buffer, int cursor, List<String> candidates) {

				if (!ctx.isControllerConnected()) {
					return 0;
				}
				// very simple completor
				if (buffer.equals("") || buffer.equals("l") || buffer.equals("li") || buffer.equals("lin") || buffer.equals("link") || buffer.equals("links")
						|| buffer.equals("linkse")) {
					candidates.add("linkset");
				} else if (buffer.equals("linkset") || buffer.equals("linkset ")) {
					candidates.add("create");
					candidates.add("delete");
					candidates.add("activate");
					candidates.add("deactivate");
					candidates.add("link");
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
	 * org.mobicents.ss7.management.console.CommandHandler#getCompletionList()
	 */
	@Override
	public List<CommandLineCompleter> getCommandLineCompleterList() {
		return this.completion;
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
		if (command.startsWith("linkset")) {
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.ss7.management.console.CommandHandler#isValid(java.lang
	 * .String)
	 */
	@Override
	public void handle(CommandContext ctx, String commandLine) {
		// TODO Validate command
		String[] commands = commandLine.split(" ");
		ctx.sendMessage(commandLine);
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
			ctx.printLine("The command is not available in the current context. Please connnect first");
			return false;
		}
		return true;
	}

}
