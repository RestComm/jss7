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
public class SccpCommandHandler extends CommandHandlerWithHelp {

    static final Tree commandTree = new Tree("sccp");
    static {
        Node parent = commandTree.getTopNode();
        Node sap = parent.addChild("sap");
        sap.addChild("create");
        sap.addChild("modify");
        sap.addChild("delete");
        sap.addChild("show");

        Node dest = parent.addChild("dest");
        dest.addChild("create");
        dest.addChild("modify");
        dest.addChild("delete");
        dest.addChild("show");

        Node rule = parent.addChild("rule");
        rule.addChild("create");
        rule.addChild("modify");
        rule.addChild("delete");
        rule.addChild("show");

        Node address = parent.addChild("address");
        address.addChild("create");
        address.addChild("modify");
        address.addChild("delete");
        address.addChild("show");

        Node rsp = parent.addChild("rsp");
        rsp.addChild("create");
        rsp.addChild("modify");
        rsp.addChild("delete");
        rsp.addChild("show");

        Node rss = parent.addChild("rss");
        rss.addChild("create");
        rss.addChild("modify");
        rss.addChild("delete");
        rss.addChild("show");

        Node lmr = parent.addChild("lmr");
        lmr.addChild("create");
        lmr.addChild("modify");
        lmr.addChild("delete");
        lmr.addChild("show");

        Node csp = parent.addChild("csp");
        csp.addChild("create");
        csp.addChild("modify");
        csp.addChild("delete");
        csp.addChild("show");

        Node set = parent.addChild("set");
        set.addChild("zmarginxudtmessage");
        set.addChild("reassemblytimerdelay");
        set.addChild("maxdatamessage");
        set.addChild("removespc");
        set.addChild("previewmode");
        set.addChild("ssttimerduration_min");
        set.addChild("ssttimerduration_max");
        set.addChild("ssttimerduration_increasefactor");
        Node sccpprotocolversion = set.addChild("sccpprotocolversion");
        set.addChild("cc_timer_a");
        set.addChild("cc_timer_d");
        Node cc_algo = set.addChild("cc_algo");
        set.addChild("cc_blockingoutgoungsccpmessages");

        sccpprotocolversion.addChild("ITU");
        sccpprotocolversion.addChild("ANSI");

        cc_algo.addChild("international");
        cc_algo.addChild("levelDepended");

        Node get = parent.addChild("get");
        get.addChild("zmarginxudtmessage");
        get.addChild("reassemblytimerdelay");
        get.addChild("maxdatamessage");
        get.addChild("removespc");
        get.addChild("previewmode");
        get.addChild("ssttimerduration_min");
        get.addChild("ssttimerduration_max");
        get.addChild("ssttimerduration_increasefactor");
        get.addChild("sccpprotocolversion");
        get.addChild("cc_timer_a");
        get.addChild("cc_timer_d");
        get.addChild("cc_algo");
        get.addChild("cc_blockingoutgoungsccpmessages");

    };

    public SccpCommandHandler() {
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
