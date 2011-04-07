/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mobicents.protocols.ss7.sccp.impl.router;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javolution.xml.XMLBinding;
import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mobicents.protocols.ss7.indicator.GlobalTitleIndicator;
import org.mobicents.protocols.ss7.indicator.NatureOfAddress;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.sccp.parameter.GT0001;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 * 
 * @author kulikov
 */
public class RuleTest {
	private final static String RULE = "1;ROUTING_BASED_ON_GLOBAL_TITLE; # #NATIONAL#9023629581# ; # #INTERNATIONAL#79023629581# ;linkset#14083#14155#0\n";

	XMLBinding binding = new XMLBinding();

	public RuleTest() {
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Before
	public void setUp() {

		binding.setAlias(AddressInformation.class, "addressInformation");
		binding.setAlias(MTPInfo.class, "mtpInfo");
		binding.setClassAttribute("type");
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testRoutingOnGTTSerialization() throws Exception {

		Rule rule = new Rule("Rule1", new AddressInformation(-1, null, NatureOfAddress.NATIONAL, "9023629581", -1),
				new AddressInformation(-1, null, NatureOfAddress.INTERNATIONAL, "79023629581", -1), new MTPInfo(
						"linkset", 14083, 14155, 0));
		SccpAddress address = new SccpAddress(GlobalTitle.getInstance(NatureOfAddress.NATIONAL, "9023629581"), 0);

		assertTrue(rule.matches(address));

		ByteArrayOutputStream output = new ByteArrayOutputStream();
		XMLObjectWriter writer = XMLObjectWriter.newInstance(output);
		writer.setBinding(binding); // Optional.
		writer.setIndentation("\t"); // Optional (use tabulation for
		// indentation).
		writer.write(rule, "Rule", Rule.class);
		writer.close();

		ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
		XMLObjectReader reader = XMLObjectReader.newInstance(input);
		reader.setBinding(binding);
		Rule ruleOut = reader.read("Rule", Rule.class);

		AddressInformation pattern = ruleOut.getPattern();
		assertNotNull(pattern);
		assertEquals(-1, pattern.getTranslationType());
		assertNull(pattern.getNumberingPlan());
		assertEquals(NatureOfAddress.NATIONAL, pattern.getNatureOfAddress());
		assertEquals("9023629581", pattern.getDigits());
		assertEquals(-1, pattern.getSubsystem());

		AddressInformation translation = ruleOut.getTranslation();
		assertNotNull(translation);
		assertEquals(-1, translation.getTranslationType());
		assertNull(translation.getNumberingPlan());
		assertEquals(NatureOfAddress.INTERNATIONAL, translation.getNatureOfAddress());
		assertEquals("79023629581", translation.getDigits());
		assertEquals(-1, translation.getSubsystem());

		MTPInfo mtpInfo = ruleOut.getMTPInfo();
		assertNotNull(mtpInfo);
		assertEquals("linkset", mtpInfo.getName());
		assertEquals(14083, mtpInfo.getOpc());
		assertEquals(14155, mtpInfo.getDpc());
		assertEquals(0, mtpInfo.getSls());

		System.out.println(output.toString());

	}

	@Test
	public void testRoutingOnSSNSerialization() throws Exception {

		Rule rule = new Rule("Rule2", 2, 8, new MTPInfo("linkset", 14083, 14155, 0));
		SccpAddress address = new SccpAddress(2, 8);

		assertTrue(rule.matches(address));

		ByteArrayOutputStream output = new ByteArrayOutputStream();
		XMLObjectWriter writer = XMLObjectWriter.newInstance(output);
		writer.setBinding(binding); // Optional.
		writer.setIndentation("\t"); // Optional (use tabulation for
		// indentation).
		writer.write(rule, "Rule", Rule.class);
		writer.close();

		ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
		XMLObjectReader reader = XMLObjectReader.newInstance(input);
		reader.setBinding(binding);
		Rule ruleOut = reader.read("Rule", Rule.class);

		assertEquals(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, ruleOut.getRoutingIndicator());
		assertEquals(2, ruleOut.getDpc());
		assertEquals(8, ruleOut.getSsn());

		AddressInformation pattern = ruleOut.getPattern();
		assertNull(pattern);

		AddressInformation translation = ruleOut.getTranslation();
		assertNull(translation);

		MTPInfo mtpInfo = ruleOut.getMTPInfo();
		assertNotNull(mtpInfo);
		assertEquals("linkset", mtpInfo.getName());
		assertEquals(14083, mtpInfo.getOpc());
		assertEquals(14155, mtpInfo.getDpc());
		assertEquals(0, mtpInfo.getSls());

		System.out.println(output.toString());

	}

	@Test
	public void testTranslation() {
		SccpAddress a1 = new SccpAddress(GlobalTitle.getInstance(NatureOfAddress.NATIONAL, "9023629581"), 0);
		Rule rule = new Rule("Rule1", new AddressInformation(-1, null, NatureOfAddress.NATIONAL, "9023629581", -1),
				new AddressInformation(-1, null, NatureOfAddress.INTERNATIONAL, "79023629581", -1), new MTPInfo(
						"linkset", 14083, 14155, 0));

		SccpAddress a2 = rule.translate(a1);
		assertEquals(a2.getGlobalTitle().getIndicator(),
				GlobalTitleIndicator.GLOBAL_TITLE_INCLUDES_NATURE_OF_ADDRESS_INDICATOR_ONLY);
		assertEquals(NatureOfAddress.INTERNATIONAL, ((GT0001) a2.getGlobalTitle()).getNoA());
		assertEquals("79023629581", a2.getGlobalTitle().getDigits());
	}

	@Test
	public void testGetInstanceWithOptions() {

		Rule rule = new Rule("Rule1", new AddressInformation(-1, null, NatureOfAddress.NATIONAL, "9023629581", -1),
				new AddressInformation(-1, null, NatureOfAddress.INTERNATIONAL, "79023629581", -1), null);

		assertEquals("Rule1", rule.getName());
		assertEquals(null, rule.getPattern().getNumberingPlan());
		assertEquals(NatureOfAddress.NATIONAL, rule.getPattern().getNatureOfAddress());
		assertEquals("9023629581", rule.getPattern().getDigits());

		assertEquals(null, rule.getTranslation().getNumberingPlan());
		assertEquals(NatureOfAddress.INTERNATIONAL, rule.getTranslation().getNatureOfAddress());
		assertEquals("79023629581", rule.getTranslation().getDigits());

		assertEquals(null, rule.getMTPInfo());
	}

	/**
	 * Test of toString method, of class Rule.
	 */
	@Test
	public void testToString() {
		AddressInformation ai = new AddressInformation(-1, null, NatureOfAddress.NATIONAL, "9023629581", -1);
		AddressInformation tr = new AddressInformation(-1, null, NatureOfAddress.INTERNATIONAL, "79023629581", -1);
		MTPInfo mtpInfo = new MTPInfo("linkset", 14083, 14155, 0);

		Rule rule = new Rule("1", ai, tr, mtpInfo);
		assertEquals(RULE, rule.toString());
	}

}