package com.github.jferard.csvsniffer;

import org.easymock.EasyMock;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

public class InQuotedFieldStateTest {
    @Test
    public void testQuote() {
        final Context c = PowerMock.createMock(Context.class);

        PowerMock.resetAll();
        EasyMock.expect(c.prev()).andReturn((int) '\\');
        c.setState(EasyMock.isA(QuoteInQuotedFieldState.class));

        PowerMock.replayAll();
        final InQuotedFieldState state = new InQuotedFieldState(',', '"');
        state.handle(c, '"');

        PowerMock.verifyAll();
    }

    @Test
    public void testNoQuote() {
        final Context c = PowerMock.createMock(Context.class);

        PowerMock.resetAll();

        PowerMock.replayAll();
        final InQuotedFieldState state = new InQuotedFieldState(',', '"');
        state.handle(c, 'a');

        PowerMock.verifyAll();
    }
}