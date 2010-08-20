/*
 * JBoss, Home of Professional Open Source
 * Copyright XXXX, Red Hat Middleware LLC, and individual contributors as indicated
 * by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.mobicents.protocols.ss7.sccp.impl.gtt;

import java.io.Serializable;

/**
 *
 * @author kulikov
 */
public abstract class Action implements Serializable  {
    
    private Object[] args;
    
    /** Creates a new instance of Action */
    public Action() {
    }
    
    public Action(Object[] args) {
        this.args = args;
    }
    
    public static Action getInstance(String name, Object[] args) {
        if (name.equals("rem")) {
            return new Remove(args);
        } else if (name.equals("ins")) {
            return new Insert(args);
        }
        return null;
    }
    
    public abstract String doExecute(String[] args);
    
    public String execute() {
        String argv[] = new String[args.length];
        int i = 0;
        if (args[0] instanceof Action) {
           argv[0] = ((Action)args[0]).execute();
           i++;
        }
        
        while (i < args.length) {
            argv[i] = (String)args[i];
            i++;
        }
        
        return doExecute(argv);
    }
    
}

