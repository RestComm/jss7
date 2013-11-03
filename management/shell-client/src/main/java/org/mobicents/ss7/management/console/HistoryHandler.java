/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package org.mobicents.ss7.management.console;

import java.util.List;

import org.mobicents.ss7.management.console.Tree.Node;

/**
 * @author amit bhayani
 *
 */
public class HistoryHandler extends CommandHandlerWithHelp {

    static final Tree commandTree = new Tree("history");
    static {
        Node parent = commandTree.getTopNode();
        parent.addChild("enable");
        parent.addChild("disable");
        parent.addChild("clear");
    };

    /**
     *
     */
    public HistoryHandler() {
        super(commandTree, DOESNT_CARE_CONNECT_DISCONNECT_FLAG);

    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.ss7.management.console.CommandHandler#isAvailable(org.mobicents
     * .ss7.management.console.CommandContext)
     */
    @Override
    public boolean isAvailable(CommandContext ctx) {
        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.ss7.management.console.CommandHandler#isValid(java.lang .String)
     */
    @Override
    public void handle(CommandContext ctx, String commandLine) {

        if (commandLine.contains("--help")) {
            this.printHelp(commandLine, ctx);
            return;
        }

        String[] commands = commandLine.split(" ");

        if (commands.length == 1) {
            this.printHistory(ctx);
        } else if (commands.length == 2) {
            String argument = commands[1];
            if ("clear".equals(argument)) {
                ctx.getHistory().clear();
            } else if ("enable".equals(argument)) {
                ctx.getHistory().setUseHistory(true);
            } else if ("disable".equals(argument)) {
                ctx.getHistory().setUseHistory(false);
            } else {
                ctx.printLine("Invalid command.");
            }
        } else {
            ctx.printLine("Invalid command.");
        }
    }

    private static void printHistory(CommandContext ctx) {

        CommandHistory history = ctx.getHistory();
        List<String> list = history.asList();
        for (String cmd : list) {
            ctx.printLine(cmd);
        }
        ctx.printLine("(The history is currently " + (history.isUseHistory() ? "enabled)" : "disabled)"));
    }

}
