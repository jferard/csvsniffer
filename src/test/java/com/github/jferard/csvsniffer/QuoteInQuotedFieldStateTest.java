package com.github.jferard.csvsniffer;

import org.easymock.EasyMock;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

public class QuoteInQuotedFieldStateTest {
    @Test
    public void testQuote() {
        Context c = PowerMock.createMock(Context.class);

        PowerMock.resetAll();
        c.storeDoubleQuote('"');
        c.setState(EasyMock.isA(InQuotedFieldState.class));

        PowerMock.replayAll();
        QuoteInQuotedFieldState state = new QuoteInQuotedFieldState(',', '"');
        state.handle(c, '"');

        PowerMock.verifyAll();
    }

    @Test
    public void testSpace() {
        Context c = PowerMock.createMock(Context.class);

        PowerMock.resetAll();
        EasyMock.expect(c.isSimpleSpace(' ')).andReturn(true);

        PowerMock.replayAll();
        QuoteInQuotedFieldState state = new QuoteInQuotedFieldState(',', '"');
        state.handle(c, ' ');

        PowerMock.verifyAll();
    }

    @Test
    public void testDelimiter() {
        Context c = PowerMock.createMock(Context.class);

        PowerMock.resetAll();
        EasyMock.expect(c.isSimpleSpace(',')).andReturn(false);
        EasyMock.expect(c.prev()).andReturn((int) '"');
        c.storeQuote('"', false);
        c.storeDelimiter(',', false);
        c.setState(EasyMock.isA(BOFState.class));

        PowerMock.replayAll();
        QuoteInQuotedFieldState state = new QuoteInQuotedFieldState(',', '"');
        state.handle(c, ',');

        PowerMock.verifyAll();
    }

    @Test
    public void testNewDelimiter() {
        Context c = PowerMock.createMock(Context.class);

        PowerMock.resetAll();
        EasyMock.expect(c.isSimpleSpace(',')).andReturn(false);
        EasyMock.expect(c.isTraced(',')).andReturn(true);
        EasyMock.expect(c.prev()).andReturn((int) '"');
        c.storeQuote('"', false);
        c.storeDelimiter(',', false);
        c.setState(EasyMock.isA(BOFState.class));

        PowerMock.replayAll();
        QuoteInQuotedFieldState state = new QuoteInQuotedFieldState(-1, '"');
        state.handle(c, ',');

        PowerMock.verifyAll();
    }

    @Test
    public void testEOL() {
        Context c = PowerMock.createMock(Context.class);

        PowerMock.resetAll();
        EasyMock.expect(c.isSimpleSpace('\n')).andReturn(false);
        EasyMock.expect(c.isTraced('\n')).andReturn(false);
        EasyMock.expect(c.isEOL('\n')).andReturn(true);
        c.setState(EasyMock.isA(EOLState.class));

        PowerMock.replayAll();
        QuoteInQuotedFieldState state = new QuoteInQuotedFieldState(-1, '"');
        state.handle(c, '\n');

        PowerMock.verifyAll();
    }

    @Test
    public void testEscape() {
        Context c = PowerMock.createMock(Context.class);

        PowerMock.resetAll();
        EasyMock.expect(c.isSimpleSpace('a')).andReturn(false);
        EasyMock.expect(c.isTraced('a')).andReturn(false);
        EasyMock.expect(c.isEOL('a')).andReturn(false);
        EasyMock.expect(c.prev()).andReturn((int) '\\');
        EasyMock.expect(c.isTraced('\\')).andReturn(true);
        c.setState(EasyMock.isA(MaybeEscapedQuoteState.class));

        PowerMock.replayAll();
        QuoteInQuotedFieldState state = new QuoteInQuotedFieldState(-1, '"');
        state.handle(c, 'a');

        PowerMock.verifyAll();
    }
}