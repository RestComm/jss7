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
public class M3UACommandHandler extends CommandHandlerWithHelp {

	private final List<CommandLineCompleter> completion;

	public M3UACommandHandler() {
		this.completion = new ArrayList<CommandLineCompleter>();

		CommandLineCompleter commandLineCompleter = new CommandLineCompleter() {
			@Override
			public int complete(CommandContext ctx, String buffer, int cursor, List<String> candidates) {

				if (!ctx.isControllerConnected()) {
					return 0;
				}
				// very simple completor
				if (buffer.equals("") || buffer.equals("m") || buffer.equals("m3") || buffer.equals("m3u")) {
					candidates.add("m3ua");
				} else if (buffer.equals("m3ua") || buffer.equals("m3ua ")) {
					candidates.add("as");
					candidates.add("asp");
					candidates.add("route");
				} else if (buffer.equals("m3ua as") || buffer.equals("m3ua as ")) {
					candidates.add("create");
					candidates.add("destroy");
					candidates.add("show");
					candidates.add("add");
					candidates.add("remove");
				} else if (buffer.equals("m3ua asp") || buffer.equals("m3ua asp ")) {
					candidates.add("create");
					candidates.add("destroy");
					candidates.add("show");
					candidates.add("start");
					candidates.add("stop");
				} else if (buffer.equals("m3ua route") || buffer.equals("m3ua route ")) {
					candidates.add("add");
					candidates.add("remove");
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
		if (command.startsWith("m3ua")) {
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
