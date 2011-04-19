/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

