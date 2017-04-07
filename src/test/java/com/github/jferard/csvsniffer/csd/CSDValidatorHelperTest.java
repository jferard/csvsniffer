package com.github.jferard.csvsniffer.csd;

import org.apache.commons.csv.CSVRecord;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;
import java.util.logging.Logger;

import static org.junit.Assert.*;

/**
 * Created by jferard on 07/04/17.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(CSVRecord.class)
public class CSDValidatorHelperTest {
    private Logger logger;
    private CSDValidatorHelper<CSDField> vh;
    private CSDValidationResult<CSDField> vr;
    private CSVRecord r;

    @Before
    public void setUp() {
        this.logger = PowerMock.createNiceMock(Logger.class);
        this.vh = new CSDValidatorHelper<CSDField>(this.logger, new ExactColumnMatcher(logger));
        this.vr = (CSDValidationResult<CSDField>) PowerMock.createMock(CSDValidationResult.class);
        this.r = PowerMock.createMock(CSVRecord.class);
    }

    @Test
    public void validateHeaderHavingTooFewRows() throws Exception {
        EasyMock.expect(this.r.size()).andReturn(1);
        this.vr.schemaHasTooManyFieldsForHeader(this.r);

        PowerMock.replayAll();
        SizedIterable<CSDField> fields = TestUtil.fromCollection(Arrays.asList(TestUtil.getMandatoryField(), TestUtil.getMandatoryField(), TestUtil.getOptionalField()));
        Assert.assertEquals(-1, this.vh.validateHeader(this.vr, fields, this.r));
        PowerMock.verifyAll();
   }

    @Test
    public void validateRecordHavingTooFewRows() throws Exception {
        EasyMock.expect(this.r.size()).andReturn(1);
        this.vr.schemaHasTooManyFieldsForRecord(5, this.r);

        PowerMock.replayAll();
        SizedIterable<CSDField> fields = TestUtil.fromCollection(Arrays.asList(TestUtil.getMandatoryField(), TestUtil.getMandatoryField(), TestUtil.getOptionalField()));
        Assert.assertEquals(-1, this.vh.validateRecord(this.vr, fields, this.r, 5));
        PowerMock.verifyAll();
    }

}