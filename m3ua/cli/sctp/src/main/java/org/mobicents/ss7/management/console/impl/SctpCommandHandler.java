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
package org.mobicents.ss7.management.console.impl;

import org.mobicents.ss7.management.console.CommandContext;
import org.mobicents.ss7.management.console.CommandHandlerWithHelp;
import org.mobicents.ss7.management.console.Tree;
import org.mobicents.ss7.management.console.Tree.Node;

/**
 * @author amit bhayani
 *
 */
public class SctpCommandHandler extends CommandHandlerWithHelp {

    static final Tree commandTree = new Tree("sctp");
    static {
        Node parent = commandTree.getTopNode();
        Node server = parent.addChild("server");
        server.addChild("create");
        server.addChild("destroy");
        server.addChild("start");
        server.addChild("stop");
        server.addChild("show");

        Node association = parent.addChild("association");
        association.addChild("create");
        association.addChild("destroy");
        association.addChild("show");

        Node set = parent.addChild("set");
        set.addChild("connectdelay");
        set.addChild("cc_delaythreshold_1");
        set.addChild("cc_delaythreshold_2");
        set.addChild("cc_delaythreshold_3");
        set.addChild("cc_backtonormal_delaythreshold_1");
        set.addChild("cc_backtonormal_delaythreshold_2");
        set.addChild("cc_backtonormal_delaythreshold_3");

        Node get = parent.addChild("get");
        get.addChild("connectdelay");
        get.addChild("cc_delaythreshold_1");
        get.addChild("cc_delaythreshold_2");
        get.addChild("cc_delaythreshold_3");
        get.addChild("cc_backtonormal_delaythreshold_1");
        get.addChild("cc_backtonormal_delaythreshold_2");
        get.addChild("cc_backtonormal_delaythreshold_3");

    };

    public SctpCommandHandler() {
        super(commandTree, CONNECT_MANDATORY_FLAG);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.ss7.management.console.CommandHandler#isValid(java.lang .String)
     */
    @Override
    public void handle(CommandContext ctx, String commandLine) {
        // TODO Validate command

        if (commandLine.contains("--help")) {
            this.printHelp(commandLine, ctx);
            return;
        }

        ctx.sendMessage(commandLine);
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
            ctx.printLine("The command is not available in the current context. Please connnect first");
            return false;
        }
        return true;
    }

}
