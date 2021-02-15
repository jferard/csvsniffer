package com.github.jferard.csvsniffer;

public interface State {
    void handle(Context context, char c);
}
