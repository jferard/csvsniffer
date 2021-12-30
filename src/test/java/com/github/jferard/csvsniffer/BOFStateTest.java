package com.github.jferard.csvsniffer;

import org.easymock.EasyMock;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

public class BOFStateTest {
    @Test
    public void testQuote() {
        final Context c = PowerMock.createMock(Context.class);

        PowerMock.resetAll();
        EasyMock.expect(c.isQuote('"')).andReturn(true);
        c.storeQuote('"', false);
        c.setState(EasyMock.isA(InQuotedFieldState.class));

        PowerMock.replayAll();
        final BOFState state = new BOFState(',');
        state.handle(c, '"');

        PowerMock.verifyAll();
    }

    @Test
    public void testSpace() {
        final Context c = PowerMock.createMock(Context.class);

        PowerMock.resetAll();
        EasyMock.expect(c.isQuote(' ')).andReturn(false);
        EasyMock.expect(c.isSimpleSpace(' ')).andReturn(true);

        PowerMock.replayAll();
        final BOFState state = new BOFState(',');
        state.handle(c, ' ');

        PowerMock.verifyAll();
    }

    @Test
    public void testEOL() {
        final Context c = PowerMock.createMock(Context.class);

        PowerMock.resetAll();
        EasyMock.expect(c.isQuote('\n')).andReturn(false);
        EasyMock.expect(c.isSimpleSpace('\n')).andReturn(false);
        EasyMock.expect(c.isEOL('\n')).andReturn(true);
        c.setState(EasyMock.isA(EOLState.class));

        PowerMock.replayAll();
        final BOFState state = new BOFState(',');
        state.handle(c, '\n');

        PowerMock.verifyAll();
    }

    @Test
    public void testTraced() {
        final Context c = PowerMock.createMock(Context.class);

        PowerMock.resetAll();
        EasyMock.expect(c.isQuote(';')).andReturn(false);
        EasyMock.expect(c.isSimpleSpace(';')).andReturn(false);
        EasyMock.expect(c.isEOL(';')).andReturn(false);
        EasyMock.expect(c.isTraced(';')).andReturn(true);
        c.storeSeen(';');

        PowerMock.replayAll();
        final BOFState state = new BOFState(',');
        state.handle(c, ';');

        PowerMock.verifyAll();
    }

    @Test
    public void testIgnore() {
        final Context c = PowerMock.createMock(Context.class);

        PowerMock.resetAll();
        EasyMock.expect(c.isQuote('a')).andReturn(false);
        EasyMock.expect(c.isSimpleSpace('a')).andReturn(false);
        EasyMock.expect(c.isEOL('a')).andReturn(false);
        EasyMock.expect(c.isTraced('a')).andReturn(false);

        PowerMock.replayAll();
        final BOFState state = new BOFState(',');
        state.handle(c, 'a');

        PowerMock.verifyAll();
    }
}