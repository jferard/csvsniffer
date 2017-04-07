package com.github.jferard.csvsniffer.csd;

import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.util.Iterator;
import java.util.logging.Logger;

import static org.junit.Assert.*;

/**
 * Created by jferard on 07/04/17.
 */
public class CSDValidationResultTest {
    private Logger logger;
    private CSDSchemaPattern<CSDField> sp;

    @Before
    public void setUp() {
        this.logger = PowerMock.createNiceMock(Logger.class);
        this.sp = (CSDSchemaPattern<CSDField>) PowerMock.createMock(CSDSchemaPattern.class);
    }

    @Test
    public void testEmpty() {
        PowerMock.replayAll();
        CSDValidationResult<CSDField> vr = new CSDValidationResult<CSDField>(logger, sp);


        Assert.assertEquals(0, vr.errorCount());
        Assert.assertTrue(vr.isOk());
        Assert.assertFalse(vr.iterator().hasNext());
        PowerMock.verifyAll();
    }

    @Test
    public void testBunchOfErrors() {
        PowerMock.replayAll();
        CSDValidationResult<CSDField> vr = new CSDValidationResult<CSDField>(logger, sp);
        vr.noLine();
        vr.badHeader();
        vr.incorrectColumnName("a", "b");
        vr.incorrectValue(10, "c", TestUtil.getMandatoryField());
        vr.missingHeader();
        vr.schemaHasTooManyFieldsForHeader(null);
        vr.schemaHasTooManyFieldsForRecord(20, null);

        Assert.assertEquals(7, vr.errorCount());
        Assert.assertFalse(vr.isOk());

        Iterator<CSDValidationError> it = vr.iterator();
        for (int i = 0; i<vr.errorCount(); i++) {
            Assert.assertTrue(it.hasNext());
            Assert.assertNotNull(it.next());
        }
        Assert.assertFalse(it.hasNext());
        PowerMock.verifyAll();
    }
}