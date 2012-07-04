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
public class SccpCommandHandler extends CommandHandlerWithHelp {

	private final List<CommandLineCompleter> completion;

	public SccpCommandHandler() {
		this.completion = new ArrayList<CommandLineCompleter>();

		CommandLineCompleter commandLineCompleter = new CommandLineCompleter() {
			@Override
			public int complete(CommandContext ctx, String buffer, int cursor, List<String> candidates) {

				if (!ctx.isControllerConnected()) {
					return 0;
				}
				// very simple completor
				if (buffer.equals("") || buffer.equals("s") || buffer.equals("sc") || buffer.equals("scc")) {
					candidates.add("sccp");
				} else if (buffer.equals("sccp") || buffer.equals("sccp ")) {
					candidates.add("sap");
					candidates.add("dest");
					candidates.add("rsp");
					candidates.add("rss");
					candidates.add("rule");
					candidates.add("primary_add");
					candidates.add("backup_add");
					candidates.add("lmr");
					candidates.add("csp");
				} else if (buffer.equals("sccp sap") || buffer.equals("sccp sap ")) {
					candidates.add("create");
					candidates.add("delete");
					candidates.add("modify");
					candidates.add("show");
				} else if (buffer.equals("sccp dest") || buffer.equals("sccp dest ")) {
					candidates.add("create");
					candidates.add("delete");
					candidates.add("modify");
					candidates.add("show");
				} else if (buffer.equals("sccp rsp") || buffer.equals("sccp rsp ")) {
					candidates.add("create");
					candidates.add("delete");
					candidates.add("modify");
					candidates.add("show");
				} else if (buffer.equals("sccp rss") || buffer.equals("sccp rss ")) {
					candidates.add("create");
					candidates.add("delete");
					candidates.add("modify");
					candidates.add("show");
				} else if (buffer.equals("m3ua rule") || buffer.equals("m3ua rule ")) {
					candidates.add("create");
					candidates.add("delete");
					candidates.add("modify");
					candidates.add("show");
				} else if (buffer.equals("m3ua primary_add") || buffer.equals("m3ua primary_add ")) {
					candidates.add("create");
					candidates.add("delete");
					candidates.add("modify");
					candidates.add("show");
				} else if (buffer.equals("m3ua backup_add") || buffer.equals("m3ua backup_add ")) {
					candidates.add("create");
					candidates.add("delete");
					candidates.add("modify");
					candidates.add("show");
				} else if (buffer.equals("m3ua lmr") || buffer.equals("m3ua lmr ")) {
					candidates.add("create");
					candidates.add("delete");
					candidates.add("modify");
					candidates.add("show");
				} else if (buffer.equals("m3ua csp") || buffer.equals("m3ua csp ")) {
					candidates.add("create");
					candidates.add("delete");
					candidates.add("modify");
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
		if (command.startsWith("sccp")) {
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
