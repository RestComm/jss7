/**
 * 
 */
package org.mobicents.protocols.ss7.tcap.api.tc.component;

/**
 * Class of invoke type, ref Q.771 2.3.1.3.
 * <ul>
 * <li>Class 1 – Both success and failure are reported.</li>
 * <li>Class 2 – Only failure is reported.</li>
 * <li>Class 3 – Only success is reported.</li>
 * <li>Class 4 – Neither success, nor failure is reported.</li>
 * <ul>
 * 
 * @author baranowb
 * 
 */
public enum InvokeClass {

	Class1, Class2, Class3, Class4;
}
