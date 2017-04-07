package com.github.jferard.csvsniffer.csd;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.StringReader;
import java.util.logging.Logger;

import static org.junit.Assert.*;

/**
 * Created by jferard on 07/04/17.
 */
public class CSDSchemaSnifferTest {
    private CSDSchemaSniffer<CSDField> sniffer;
    private Logger logger;
    private CSDFieldFactory<CSDField> fy;
    private CSDValidatorHelper<CSDField> vh;
    private CSDSchemaPattern<CSDField> sp;
    private CSDSchema<CSDField> s;

    @Before
    public void setUp() {
        this.logger = PowerMock.createNiceMock(Logger.class);
        this.fy = PowerMock.createMock(CSDFieldFactory.class);
        this.vh = PowerMock.createMock(CSDValidatorHelper.class);
        this.sp = PowerMock.createMock(CSDSchemaPattern.class);
        this.s = PowerMock.createMock(CSDSchema.class);

        this.sniffer = new CSDSchemaSniffer<CSDField>(this.logger, this.fy, this.vh);

    }

    @Test
    public void sniffNoLine() throws Exception {
        CSVParser p = CSVFormat.RFC4180.parse(new StringReader(""));

        PowerMock.replayAll();
        CSDSchema<CSDField> s = this.sniffer.sniff(this.sp, p, 10);
        Assert.assertEquals(null, s);
        PowerMock.verifyAll();
    }

    @Test
    public void sniffBadHeader1() throws Exception {
        CSVParser p = CSVFormat.RFC4180.parse(new StringReader("a,b,c\n1,2,3\n4,5,6"));

        EasyMock.expect(this.vh.validateHeader(EasyMock.isA(CSDValidationResult.class), EasyMock.eq(this.sp), EasyMock.isA(CSVRecord.class))).andReturn(10);
        EasyMock.expect(this.sp.hasOptionalHeader()).andReturn(false);

        PowerMock.replayAll();
        CSDSchema<CSDField> s = this.sniffer.sniff(this.sp, p, 10);
        Assert.assertEquals(null, s);
        PowerMock.verifyAll();
    }

    @Test
    public void sniffBadHeader2() throws Exception {
        CSVParser p = CSVFormat.RFC4180.parse(new StringReader("a,b,c\n1,2,3\n4,5,6"));

        EasyMock.expect(this.vh.validateHeader(EasyMock.isA(CSDValidationResult.class), EasyMock.eq(this.sp), EasyMock.isA(CSVRecord.class))).andReturn(-1);

        PowerMock.replayAll();
        CSDSchema<CSDField> s = this.sniffer.sniff(this.sp, p, 10);
        Assert.assertEquals(null, s);
        PowerMock.verifyAll();
    }

    @Test
    public void sniffGoodHeader() throws Exception {
        CSVParser p = CSVFormat.RFC4180.parse(new StringReader("a,b,c\n1,2,3\n4,5,6"));

        EasyMock.expect(this.vh.validateHeader(EasyMock.isA(CSDValidationResult.class), EasyMock.eq(this.sp), EasyMock.isA(CSVRecord.class))).andReturn(0);
        EasyMock.expect(this.vh.validateRecord(EasyMock.isA(CSDValidationResult.class), EasyMock.eq(this.sp), EasyMock.isA(CSVRecord.class), EasyMock.eq(1))).andReturn(0);
        EasyMock.expect(this.vh.validateRecord(EasyMock.isA(CSDValidationResult.class), EasyMock.eq(this.sp), EasyMock.isA(CSVRecord.class), EasyMock.eq(2))).andReturn(0);
        EasyMock.expect(this.sp.newSchema(EasyMock.eq(this.fy), EasyMock.isA(CSVRecord.class))).andReturn(this.s);

        PowerMock.replayAll();
        CSDSchema<CSDField> s2 = this.sniffer.sniff(this.sp, p, 10);
        Assert.assertEquals(s, s2);
        PowerMock.verifyAll();
    }
}