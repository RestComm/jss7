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
package org.mobicents.ss7.management.shell;

/**
 *
 * @author kulikov
 */
public class Parameter {
    private String name;
    private Value[] values;
    
    public Parameter(String name) {
        this.name = name;
    }
    
    public Parameter(String name, Value[] values) {
        this.name = name;
        this.values = values;
    }
    
    public String getName() {
        return name;
    }
    
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(name);
        
        if (values == null) {
            buffer.append(" ");
            buffer.append("<value>");
        } else {
            buffer.append(" ");
            buffer.append("{");
            for (int i = 0; i < values.length -1; i++) {
                buffer.append(values[i].toString());
                buffer.append("|");
            }
            
            buffer.append(values[values.length - 1].toString());
            buffer.append("}");
        }
        
        return buffer.toString();
    }
}
