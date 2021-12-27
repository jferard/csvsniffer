package com.github.jferard.csvsniffer;

import org.easymock.EasyMock;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

public class BOLStateTest {
    @Test
    public void testQuote() {
        Context c = PowerMock.createMock(Context.class);

        PowerMock.resetAll();
        EasyMock.expect(c.isQuote('"')).andReturn(true);
        c.storeQuote('"', false);
        c.setState(EasyMock.isA(InQuotedFieldState.class));

        PowerMock.replayAll();
        BOLState state = new BOLState();
        state.handle(c, '"');

        PowerMock.verifyAll();
    }

    @Test
    public void testSpace() {
        Context c = PowerMock.createMock(Context.class);

        PowerMock.resetAll();
        EasyMock.expect(c.isQuote(' ')).andReturn(false);
        EasyMock.expect(c.isSimpleSpace(' ')).andReturn(true);

        PowerMock.replayAll();
        BOLState state = new BOLState();
        state.handle(c, ' ');

        PowerMock.verifyAll();
    }

    @Test
    public void testEOL() {
        Context c = PowerMock.createMock(Context.class);

        PowerMock.resetAll();
        EasyMock.expect(c.isQuote('\n')).andReturn(false);
        EasyMock.expect(c.isSimpleSpace('\n')).andReturn(false);
        EasyMock.expect(c.isEOL('\n')).andReturn(true);
        c.setState(EasyMock.isA(EOLState.class));

        PowerMock.replayAll();
        BOLState state = new BOLState();
        state.handle(c, '\n');

        PowerMock.verifyAll();
    }

    @Test
    public void testTraced() {
        Context c = PowerMock.createMock(Context.class);

        PowerMock.resetAll();
        EasyMock.expect(c.isQuote(';')).andReturn(false);
        EasyMock.expect(c.isSimpleSpace(';')).andReturn(false);
        EasyMock.expect(c.isEOL(';')).andReturn(false);
        EasyMock.expect(c.isTraced(';')).andReturn(true);
        c.storeSeen(';');

        PowerMock.replayAll();
        BOLState state = new BOLState();
        state.handle(c, ';');

        PowerMock.verifyAll();
    }

    @Test
    public void testIgnore() {
        Context c = PowerMock.createMock(Context.class);

        PowerMock.resetAll();
        EasyMock.expect(c.isQuote('a')).andReturn(false);
        EasyMock.expect(c.isSimpleSpace('a')).andReturn(false);
        EasyMock.expect(c.isEOL('a')).andReturn(false);
        EasyMock.expect(c.isTraced('a')).andReturn(false);

        PowerMock.replayAll();
        BOLState state = new BOLState();
        state.handle(c, 'a');

        PowerMock.verifyAll();
    }
}