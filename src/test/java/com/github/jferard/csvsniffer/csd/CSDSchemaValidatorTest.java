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

/**
 * Created by jferard on 07/04/17.
 */
public class CSDSchemaValidatorTest {
    private CSDSchemaValidator<CSDField> validator;
    private Logger logger;
    private CSDValidatorHelper<CSDField> vh;
    private CSDSchema<CSDField> s;

    @Before
    public void setUp() {
        this.logger = PowerMock.createNiceMock(Logger.class);
        this.vh = PowerMock.createMock(CSDValidatorHelper.class);
        this.s = PowerMock.createMock(CSDSchema.class);

        this.validator = new CSDSchemaValidator<CSDField>(this.logger, this.vh);
    }

    @Test
    public void sniffNoLine() throws Exception {
        CSVParser p = CSVFormat.RFC4180.parse(new StringReader(""));

        PowerMock.replayAll();
        CSDValidationResult<CSDField> r = this.validator.validate(this.s, p);
        Assert.assertEquals(1, r.errorCount());
        Assert.assertEquals("CSDValidationError of type NO_AVAILABLE_LINE: No available line. (line 0)", r.iterator().next().toString());
        PowerMock.verifyAll();
    }

    @Test
    public void sniffBadHeader1() throws Exception {
        CSVParser p = CSVFormat.RFC4180.parse(new StringReader("a,b,c\n1,2,3\n4,5,6"));

        EasyMock.expect(this.vh.validateHeader(EasyMock.isA(CSDValidationResult.class), EasyMock.eq(this.s), EasyMock.isA(CSVRecord.class))).andReturn(10);
        EasyMock.expect(this.vh.validateRecord(EasyMock.isA(CSDValidationResult.class), EasyMock.eq(this.s), EasyMock.isA(CSVRecord.class), EasyMock.eq(1))).andReturn(100);
        EasyMock.expect(this.vh.validateRecord(EasyMock.isA(CSDValidationResult.class), EasyMock.eq(this.s), EasyMock.isA(CSVRecord.class), EasyMock.eq(2))).andReturn(1000);
        EasyMock.expect(this.s.hasOptionalHeader()).andReturn(false);

        PowerMock.replayAll();
        CSDValidationResult<CSDField> r = this.validator.validate(this.s, p);
        Assert.assertEquals(0, r.errorCount());
        PowerMock.verifyAll();
    }

    @Test
    public void sniffBadHeader2() throws Exception {
        CSVParser p = CSVFormat.RFC4180.parse(new StringReader("a,b,c\n1,2,3\n4,5,6"));

        EasyMock.expect(this.vh.validateHeader(EasyMock.isA(CSDValidationResult.class), EasyMock.eq(this.s), EasyMock.isA(CSVRecord.class))).andReturn(-1);
        EasyMock.expect(this.vh.validateRecord(EasyMock.isA(CSDValidationResult.class), EasyMock.eq(this.s), EasyMock.isA(CSVRecord.class), EasyMock.eq(1))).andReturn(0);
        EasyMock.expect(this.vh.validateRecord(EasyMock.isA(CSDValidationResult.class), EasyMock.eq(this.s), EasyMock.isA(CSVRecord.class), EasyMock.eq(2))).andReturn(0);

        PowerMock.replayAll();
        CSDValidationResult<CSDField> r = this.validator.validate(this.s, p);
        Assert.assertEquals(0, r.errorCount());
        PowerMock.verifyAll();
    }

    @Test
    public void sniffGoodHeader() throws Exception {
        CSVParser p = CSVFormat.RFC4180.parse(new StringReader("a,b,c\n1,2,3\n4,5,6"));

        EasyMock.expect(this.vh.validateHeader(EasyMock.isA(CSDValidationResult.class), EasyMock.eq(this.s), EasyMock.isA(CSVRecord.class))).andReturn(0);
        EasyMock.expect(this.vh.validateRecord(EasyMock.isA(CSDValidationResult.class), EasyMock.eq(this.s), EasyMock.isA(CSVRecord.class), EasyMock.eq(1))).andReturn(0);
        EasyMock.expect(this.vh.validateRecord(EasyMock.isA(CSDValidationResult.class), EasyMock.eq(this.s), EasyMock.isA(CSVRecord.class), EasyMock.eq(2))).andReturn(0);

        PowerMock.replayAll();
        CSDValidationResult<CSDField> r = this.validator.validate(this.s, p);
        Assert.assertEquals(0, r.errorCount());
        Assert.assertTrue(r.isOk());
        PowerMock.verifyAll();
    }
}