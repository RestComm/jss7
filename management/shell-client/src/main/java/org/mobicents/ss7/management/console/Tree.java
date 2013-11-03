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

/**
 * @author amit bhayani
 *
 */
public class Tree {
    private Node root;

    public Tree(String rootData) {
        root = new Node(rootData);
    }

    public Node getTopNode() {
        return this.root;
    }

    public class Node {
        private String data;
        private Node parent;
        private List<Node> children;

        /**
         * @param data
         */
        public Node(String data) {
            super();
            this.data = data;
            this.children = new ArrayList<Node>();
        }

        /**
         * @return the data
         */
        public String getData() {
            return data;
        }

        public Node addChild(String childData) {
            Node child = new Node(childData);
            child.parent = this;
            this.children.add(child);
            return child;
        }

        public List<Node> getChildren() {
            return this.children;
        }

        public Node getParent() {
            return this.parent;
        }

        /**
         * Returns the full command for this {@link Node}. For example consider below tree, when getCommand() is called on
         * Node111, it returns "Node1 Node11 Node111" Node1 |_Node11 |_Node111 |_Node12
         *
         * @return
         */
        public String getCommand() {
            StringBuffer command = new StringBuffer(this.data);
            Node parent = this.parent;
            while (parent != null) {
                command.insert(0, parent.getData() + " ");
                parent = parent.getParent();
            }
            return command.toString();
        }
    }// end of Node
}
