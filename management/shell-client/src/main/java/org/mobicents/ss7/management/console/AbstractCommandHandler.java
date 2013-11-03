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

import java.util.ArrayList;
import java.util.List;

import org.mobicents.ss7.management.console.Tree.Node;

/**
 * @author amit bhayani
 *
 */
public abstract class AbstractCommandHandler implements CommandHandler {

    public static final int CONNECT_MANDATORY_FLAG = 1;
    public static final int DISCONNECT_MANDATORY_FLAG = 2;
    public static final int DOESNT_CARE_CONNECT_DISCONNECT_FLAG = 0;

    private final List<CommandLineCompleter> completion;
    private final Tree tree;

    // Variable to indicate that tab completion should show commands only when
    // CLI is connected or disconnected or doesn't care
    private final int connectFlag;

    /**
     *
     */
    public AbstractCommandHandler(Tree tree, final int connectFlag) {
        this.tree = tree;
        this.connectFlag = connectFlag;
        this.completion = new ArrayList<CommandLineCompleter>();
        final Node node = this.tree.getTopNode();
        final String data = node.getData();
        CommandLineCompleter commandLineCompleter = new CommandLineCompleter() {

            @Override
            public int complete(CommandContext ctx, String buffer, int cursor, List<String> candidates) {

                if (connectFlag == CONNECT_MANDATORY_FLAG && !ctx.isControllerConnected()) {
                    return 0;
                } else if (connectFlag == DISCONNECT_MANDATORY_FLAG && ctx.isControllerConnected()) {
                    return 0;
                }

                if (buffer.equals("")) {
                    candidates.add(data);
                } else {
                    addCompletionrecursively(buffer, candidates, node);
                }
                return 0;
            }

        };

        this.completion.add(commandLineCompleter);

    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.ss7.management.console.CommandHandler#handles(java.lang .String)
     */
    @Override
    public boolean handles(String command) {
        if (command.startsWith(this.tree.getTopNode().getData())) {
            return true;
        }
        return false;
    }

    private boolean addCompletionrecursively(String buffer, List<String> candidates, Node parentNode) {
        String command = parentNode.getCommand();
        String trimmedBuffer = buffer.trim();
        if (command.equals(trimmedBuffer)) {
            // If command is exactly what user has typed on CLI, show all
            // possible children
            List<Node> children = parentNode.getChildren();

            if (children.size() == 1) {
                // If there is only one child, directly print it rather than
                // showing option
                candidates.add(children.get(0).getCommand());
                return true;
            } else {
                for (Node n : children) {
                    candidates.add(n.getData());
                }
            }
            return true;
        } else if (command.startsWith(buffer)) {
            // If user has type partial command, complete it
            candidates.add(command);
            return true;
        }

        // None of the above conditions matched; lets check for child
        // recursively
        List<Node> children = parentNode.getChildren();
        for (Node n : children) {
            if (addCompletionrecursively(buffer, candidates, n)) {
                return true;
            }
        }

        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.ss7.management.console.CommandHandler# getCommandLineCompleterList()
     */
    @Override
    public List<CommandLineCompleter> getCommandLineCompleterList() {
        return this.completion;
    }

}
