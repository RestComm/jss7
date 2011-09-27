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
public class Pattern implements Serializable {
    private String pattern;
    private String template;
    
    /** Creates a new instance of Pattern */
    public Pattern(String pattern) {
        this.pattern = pattern;
        this.template = toRegExpression(pattern.split("/")[0]);
    }
    
    private String toRegExpression(String pattern) {
        pattern = pattern.replaceAll("x", "\\\\d");
        pattern = pattern.replaceAll("\\*", "\\\\d*");
        return pattern;
    }
    
    public String getResult(String number) {
        String[] parts = pattern.split("/");
        if (parts.length == 1) {
            return number;
        }
        
        int i = 1;
        Object expression = number;
        
        while ( i < parts.length) {
            String operationExpression = parts[i];
            
            String[] oparts = operationExpression.split(" ");
            
            String name = oparts[0];
            String[] args = oparts[1].split(",");
            
            Object[] argv = new Object[args.length + 1];
            argv[0] = expression;
            for (int j = 0; j < args.length; j++ ) {
                argv[j+1] = args[j];
            }
            expression = Action.getInstance(name, argv);
            i++;
        }
        return ((Action) expression).execute();
    }
    
    public boolean matches(String pattern) {
        return pattern.matches(template);
    }
    
    public static void main(String[] args) {
        Pattern pattern = new Pattern("1101/rem 0,4/ins 0,9023629581");
        System.out.println(pattern.getResult("1101"));
        System.out.println(pattern.template);
        System.out.println(pattern.matches("967"));
    }
}

