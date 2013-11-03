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

/**
 * @author abhayani
 *
 */
public class DisconnectHandler extends CommandHandlerWithHelp {

    static final Tree commandTree = new Tree("disconnect");

    /**
     *
     */
    public DisconnectHandler() {
        super(commandTree, CONNECT_MANDATORY_FLAG);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.ss7.management.console.CommandHandler#isAvailable(org.mobicents
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
     * @see org.mobicents.ss7.management.console.CommandHandler#handle(org.mobicents .ss7.management.console.CommandContext,
     * java.lang.String)
     */
    @Override
    public void handle(CommandContext ctx, String commandLine) {
        if (commandLine.contains("--help")) {
            this.printHelp(commandLine, ctx);
            return;
        }

        String[] commands = commandLine.split(" ");

        if (commands.length == 1) {
            ctx.sendMessage("disconnect");
            ctx.disconnectController();
        } else if (commands.length == 2) {
            if (commandLine.contains("--help")) {
                this.printHelp("help/disconnect.txt", ctx);
                return;
            }
        } else {
            ctx.printLine("Invalid command.");
        }

    }
}
