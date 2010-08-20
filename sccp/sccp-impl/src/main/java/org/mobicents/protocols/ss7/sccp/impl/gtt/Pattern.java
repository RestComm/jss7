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

