package com.github.jferard.csvsniffer;

import org.easymock.EasyMock;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

public class EOLStateTest {
    @Test
    public void testCRLF() {
        final Context c = PowerMock.createMock(Context.class);

        PowerMock.resetAll();
        EasyMock.expect(c.isEOL('\n')).andReturn(true);
        c.storeEol("\r\n");
        c.newRow();

        PowerMock.replayAll();
        final EOLState state = new EOLState('\r');
        state.handle(c, '\n');

        PowerMock.verifyAll();
    }

    @Test
    public void testLF() {
        final Context c = PowerMock.createMock(Context.class);

        PowerMock.resetAll();
        EasyMock.expect(c.isEOL('a')).andReturn(false);
        c.storeEol("\n");
        c.unget('a');
        c.newRow();

        PowerMock.replayAll();
        final EOLState state = new EOLState('\n');
        state.handle(c, 'a');

        PowerMock.verifyAll();
    }

    @Test
    public void testLFLF() {
        final Context c = PowerMock.createMock(Context.class);

        PowerMock.resetAll();
        EasyMock.expect(c.isEOL('\n')).andReturn(true);
        c.storeEol("\n");
        c.storeEol("\n");
        c.newRow();

        PowerMock.replayAll();
        final EOLState state = new EOLState('\n');
        state.handle(c, '\n');

        PowerMock.verifyAll();
    }
}