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
public class SctpCommandHandler extends CommandHandlerWithHelp {

	private final List<CommandLineCompleter> completion;

	public SctpCommandHandler() {
		this.completion = new ArrayList<CommandLineCompleter>();

		CommandLineCompleter commandLineCompleter = new CommandLineCompleter() {
			@Override
			public int complete(CommandContext ctx, String buffer, int cursor, List<String> candidates) {

				if (!ctx.isControllerConnected()) {
					return 0;
				}
				// very simple completor
				if (buffer.equals("") || buffer.equals("s") || buffer.equals("sc") || buffer.equals("sct")) {
					candidates.add("sctp");
				} else if (buffer.equals("sctp") || buffer.equals("sctp ")) {
					candidates.add("server");
					candidates.add("association");
				} else if (buffer.equals("sctp s") || buffer.equals("sctp se") || buffer.equals("sctp ser") || buffer.equals("sctp serv")
						|| buffer.equals("sctp serve")) {
					candidates.add("sctp server");
				} else if (buffer.equals("sctp server") || buffer.equals("sctp server ")) {
					candidates.add("create");
					candidates.add("destroy");
					candidates.add("start");
					candidates.add("stop");
					candidates.add("show");
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
		if (command.startsWith("sctp")) {
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
