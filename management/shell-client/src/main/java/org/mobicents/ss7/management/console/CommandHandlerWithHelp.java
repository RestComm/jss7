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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.mobicents.ss7.management.console.Tree.Node;

/**
 * @author amit bhayani
 *
 */
public abstract class CommandHandlerWithHelp extends AbstractCommandHandler {

    /**
     * @param tree
     */
    public CommandHandlerWithHelp(Tree tree, final int connectFlag) {
        super(tree, connectFlag);
        Node topNode = tree.getTopNode();

        if (topNode.getChildren().size() > 0) {
            // add --help only if there is atleast 1 child. Else for top command
            // --help is any way added
            addHelpToLastNode(topNode);
        }

        topNode.addChild("--help");

    }

    private void addHelpToLastNode(Node node) {
        List<Node> children = node.getChildren();
        if (children.size() == 0) {
            // If this is last Node in the branch we add --help
            node.addChild("--help");
        } else {
            for (Node n : children) {
                addHelpToLastNode(n);
            }
        }
    }

    protected void printHelp(String commandLine, CommandContext ctx) {

        String fileName = commandLine.trim().replaceAll(" --help", "");
        fileName = "help/" + fileName.replace(" ", "_") + ".txt";

        InputStream helpInput = SecurityActions.getClassLoader(CommandHandlerWithHelp.class).getResourceAsStream(fileName);
        if (helpInput != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(helpInput));
            try {
                String helpLine = reader.readLine();
                while (helpLine != null) {
                    ctx.printLine(helpLine);
                    helpLine = reader.readLine();
                }
            } catch (java.io.IOException e) {
                ctx.printLine("Failed to read help/help.txt: " + e.getLocalizedMessage());
            } finally {
                try {
                    reader.close();
                } catch (IOException e) {

                }
            }
        } else {
            ctx.printLine("Failed to locate command description " + fileName);
        }
    }
}
