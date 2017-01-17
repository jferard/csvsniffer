/*******************************************************************************
 * CSV Sniffer - A simple sniffer to detect file encoding and CSV format of a file
 *    Copyright (C) 2016 J. Férard <https://github.com/jferard>
 *
 * This file is part of CSV Sniffer.
 *
 * CSV Sniffer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CSV Sniffer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.github.jferard.csvsniffer;

import com.google.common.base.Joiner;
import com.google.common.io.Resources;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;

public class CSVHeaderSnifferTest {
	private static final Charset ASCII = Charset.forName("US-ASCII");
	private Joiner joiner;

	@Before
	public void setUp() {
		this.joiner = Joiner.on('\n');
	}

	@Test
	public final void testWithHeader() throws IOException {
		CSVOptionalHeaderSniffer csvHeaderSniffer = CSVOptionalHeaderSniffer
				.getSniffer((byte) ',', (byte) 0, (byte) 0, ASCII);
		InputStream stream = new ByteArrayInputStream(
				this.joiner.join("Year,Make,Model", "1997,Ford,E350",
						"2000,Mercury,Cougar").getBytes(ASCII));

		csvHeaderSniffer.sniff(stream, 10000);
		Assert.assertEquals(Arrays.asList("Year", "Make", "Model"),
				csvHeaderSniffer.getHeader());

	}

	@Test
	public final void testWithoutHeader() throws IOException {
		CSVOptionalHeaderSniffer csvHeaderSniffer = CSVOptionalHeaderSniffer
				.getSniffer((byte) ',', (byte) 0, (byte) 0, ASCII);
		InputStream stream = new ByteArrayInputStream(this.joiner
				.join("1997,Ford,E350", "2000,Mercury,Cougar").getBytes(ASCII));

		csvHeaderSniffer.sniff(stream, 10000);
		Assert.assertNull(csvHeaderSniffer.getHeader());
	}

	@Test
	public final void testWithHeader2() throws IOException {
		CSVOptionalHeaderSniffer csvHeaderSniffer = CSVOptionalHeaderSniffer
				.getSniffer((byte) ',', (byte) '"', (byte) '"', ASCII);
		final String s = this.joiner.join("Year,Make,Model,Description,Price",
				"1997,Ford,E350,\"ac, abs, moon\",3000.00",
				"1999,Chevy,\"Venture \"\"Extended Edition\"\"\",\"\",4900.00",
				"1999,Chevy,\"Venture \"\"Extended Edition, Very Large\"\"\",,5000.00",
				"1996,Jeep,Grand Cherokee,\"MUST SELL!\n air, moon roof, loaded\",4799.00");
		InputStream stream = new ByteArrayInputStream(s.getBytes(ASCII));

		csvHeaderSniffer.sniff(stream, 10000);
		Assert.assertEquals(
				Arrays.asList("Year", "Make", "Model", "Description", "Price"),
				csvHeaderSniffer.getHeader());

	}

	@Test
	public final void testWithoutHeader2() throws IOException {

		CSVOptionalHeaderSniffer csvHeaderSniffer = CSVOptionalHeaderSniffer
				.getSniffer((byte) ',', (byte) '"', (byte) '"', ASCII);
		final String s = this.joiner.join(
				"1997,Ford,E350,\"ac, abs, moon\",3000.00",
				"1999,Chevy,\"Venture \"\"Extended Edition\"\"\",\"\",4900.00",
				"1999,Chevy,\"Venture \"\"Extended Edition, Very Large\"\"\",,5000.00",
				"1996,Jeep,Grand Cherokee,\"MUST SELL!\n air, moon roof, loaded\",4799.00");
		InputStream stream = new ByteArrayInputStream(s.getBytes(ASCII));

		csvHeaderSniffer.sniff(stream, 10000);
		Assert.assertNull(csvHeaderSniffer.getHeader());
	}

	@Test
	public final void test3() throws IOException {
		CSVOptionalHeaderSniffer csvHeaderSniffer = CSVOptionalHeaderSniffer
				.getSniffer((byte) ';', (byte) '"', (byte) '"', ASCII);
		InputStream stream = Resources.getResource("sirc-17804_9075_14209_201612_L_M_20170104_171522721-part" +
				".csv").openStream();
		csvHeaderSniffer.sniff(stream, 100000);
		Assert.assertEquals(Arrays.asList("SIREN", "NIC", "L1_NORMALISEE", "L2_NORMALISEE", "L3_NORMALISEE",
				"L4_NORMALISEE", "L5_NORMALISEE", "L6_NORMALISEE", "L7_NORMALISEE", "L1_DECLAREE", "L2_DECLAREE",
				"L3_DECLAREE", "L4_DECLAREE", "L5_DECLAREE", "L6_DECLAREE", "L7_DECLAREE", "NUMVOIE", "INDREP",
				"TYPVOIE", "LIBVOIE", "CODPOS", "CEDEX", "RPET", "LIBREG", "DEPET", "ARRONET", "CTONET", "COMET",
				"LIBCOM", "DU", "TU", "UU", "EPCI", "TCD", "ZEMET", "SIEGE", "ENSEIGNE", "IND_PUBLIPO", "DIFFCOM",
				"AMINTRET", "NATETAB", "LIBNATETAB", "APET700", "LIBAPET", "DAPET", "TEFET", "LIBTEFET", "EFETCENT",
				"DEFET", "ORIGINE", "DCRET", "DATE_DEB_ETAT_ADM_ET", "ACTIVNAT", "LIEUACT", "ACTISURF", "SAISONAT",
				"MODET", "PRODET", "PRODPART", "AUXILT", "NOMEN_LONG", "SIGLE", "NOM", "PRENOM", "CIVILITE", "RNA",
				"NICSIEGE", "RPEN", "DEPCOMEN", "ADR_MAIL", "NJ", "LIBNJ", "APEN700", "LIBAPEN", "DAPEN", "APRM",
				"ESSEN", "DATEESS", "TEFEN", "LIBTEFEN", "EFENCENT", "DEFEN", "CATEGORIE", "DCREN", "AMINTREN",
				"MONOACT", "MODEN", "PRODEN", "ESAANN", "TCA", "ESAAPEN", "ESASEC1N", "ESASEC2N", "ESASEC3N",
				"ESASEC4N", "VMAJ", "VMAJ1", "VMAJ2", "VMAJ3", "DATEMAJ"), csvHeaderSniffer.getHeader());
	}
}
