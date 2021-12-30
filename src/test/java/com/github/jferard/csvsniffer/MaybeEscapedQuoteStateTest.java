package com.github.jferard.csvsniffer;

import org.easymock.EasyMock;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

public class MaybeEscapedQuoteStateTest {
    @Test
    public void testSpace() {
        final Context c = PowerMock.createMock(Context.class);

        PowerMock.resetAll();
        EasyMock.expect(c.isSimpleSpace(' ')).andReturn(true);

        PowerMock.replayAll();
        final MaybeEscapedQuoteState state = new MaybeEscapedQuoteState(',', '"', '\\');
        state.handle(c, ' ');

        PowerMock.verifyAll();
    }

    @Test
    public void testDelimiter() {
        final Context c = PowerMock.createMock(Context.class);

        PowerMock.resetAll();
        EasyMock.expect(c.isSimpleSpace(',')).andReturn(false);
        c.storeDelimiter(',', false);
        c.setState(EasyMock.isA(BOFState.class));

        PowerMock.replayAll();
        final MaybeEscapedQuoteState state = new MaybeEscapedQuoteState(',', '"', '\\');
        state.handle(c, ',');

        PowerMock.verifyAll();
    }

    @Test
    public void testEscape() {
        final Context c = PowerMock.createMock(Context.class);

        PowerMock.resetAll();
        EasyMock.expect(c.isSimpleSpace('a')).andReturn(false);
        c.storeEscape('\\');
        c.setState(EasyMock.isA(InQuotedFieldState.class));

        PowerMock.replayAll();
        final MaybeEscapedQuoteState state = new MaybeEscapedQuoteState(',', '"', '\\');
        state.handle(c, 'a');

        PowerMock.verifyAll();
    }
}