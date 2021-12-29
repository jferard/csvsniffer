package com.github.jferard.csvsniffer;

import org.easymock.EasyMock;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

public class QuoteInQuotedFieldStateTest {
    @Test
    public void testQuote() {
        final Context c = PowerMock.createMock(Context.class);

        PowerMock.resetAll();
        c.storeDoubleQuote('"');
        c.setState(EasyMock.isA(InQuotedFieldState.class));

        PowerMock.replayAll();
        final QuoteInQuotedFieldState state = new QuoteInQuotedFieldState(',', '"', '\\');
        state.handle(c, '"');

        PowerMock.verifyAll();
    }

    @Test
    public void testSpace() {
        final Context c = PowerMock.createMock(Context.class);

        PowerMock.resetAll();
        EasyMock.expect(c.isSimpleSpace(' ')).andReturn(true);

        PowerMock.replayAll();
        final QuoteInQuotedFieldState state = new QuoteInQuotedFieldState(',', '"', '\\');
        state.handle(c, ' ');

        PowerMock.verifyAll();
    }

    @Test
    public void testDelimiter() {
        final Context c = PowerMock.createMock(Context.class);

        PowerMock.resetAll();
        EasyMock.expect(c.isSimpleSpace(',')).andReturn(false);
        EasyMock.expect(c.prev()).andReturn((int) '"');
        c.storeQuote('"', false);
        c.storeDelimiter(',', false);
        c.setState(EasyMock.isA(BOFState.class));

        PowerMock.replayAll();
        final QuoteInQuotedFieldState state = new QuoteInQuotedFieldState(',', '"', '\\');
        state.handle(c, ',');

        PowerMock.verifyAll();
    }

    @Test
    public void testNewDelimiter() {
        final Context c = PowerMock.createMock(Context.class);

        PowerMock.resetAll();
        EasyMock.expect(c.isSimpleSpace(',')).andReturn(false);
        EasyMock.expect(c.isTraced(',')).andReturn(true);
        EasyMock.expect(c.prev()).andReturn((int) '"');
        c.storeQuote('"', false);
        c.storeDelimiter(',', false);
        c.setState(EasyMock.isA(BOFState.class));

        PowerMock.replayAll();
        final QuoteInQuotedFieldState state = new QuoteInQuotedFieldState(-1, '"', '\\');
        state.handle(c, ',');

        PowerMock.verifyAll();
    }

    @Test
    public void testEOL() {
        final Context c = PowerMock.createMock(Context.class);

        PowerMock.resetAll();
        EasyMock.expect(c.isSimpleSpace('\n')).andReturn(false);
        EasyMock.expect(c.isTraced('\n')).andReturn(false);
        EasyMock.expect(c.isEOL('\n')).andReturn(true);
        c.setState(EasyMock.isA(EOLState.class));

        PowerMock.replayAll();
        final QuoteInQuotedFieldState state = new QuoteInQuotedFieldState(-1, '"', '\\');
        state.handle(c, '\n');

        PowerMock.verifyAll();
    }

    @Test
    public void testEscape() {
        final Context c = PowerMock.createMock(Context.class);

        PowerMock.resetAll();
        EasyMock.expect(c.isSimpleSpace('a')).andReturn(false);
        EasyMock.expect(c.isTraced('a')).andReturn(false);
        EasyMock.expect(c.isEOL('a')).andReturn(false);
        c.storeEscape('\\');
        EasyMock.expect(c.isTraced('\\')).andReturn(true);
        c.setState(EasyMock.isA(InQuotedFieldState.class));

        PowerMock.replayAll();
        final QuoteInQuotedFieldState state = new QuoteInQuotedFieldState(-1, '"', '\\');
        state.handle(c, 'a');

        PowerMock.verifyAll();
    }
}