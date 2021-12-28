package com.github.jferard.csvsniffer;

import org.easymock.EasyMock;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

public class InQuotedFieldStateTest {
    @Test
    public void testQuote() {
        Context c = PowerMock.createMock(Context.class);

        PowerMock.resetAll();
        c.setState(EasyMock.isA(QuoteInQuotedFieldState.class));

        PowerMock.replayAll();
        InQuotedFieldState state = new InQuotedFieldState(',', '"');
        state.handle(c, '"');

        PowerMock.verifyAll();
    }

    @Test
    public void testNoQuote() {
        Context c = PowerMock.createMock(Context.class);

        PowerMock.resetAll();

        PowerMock.replayAll();
        InQuotedFieldState state = new InQuotedFieldState(',', '"');
        state.handle(c, 'a');

        PowerMock.verifyAll();
    }
}