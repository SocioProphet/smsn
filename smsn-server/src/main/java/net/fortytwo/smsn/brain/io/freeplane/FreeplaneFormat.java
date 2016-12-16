package net.fortytwo.smsn.brain.io.freeplane;

import net.fortytwo.smsn.brain.io.Format;

public class FreeplaneFormat extends Format {
    private static final FreeplaneFormat instance = new FreeplaneFormat();

    private FreeplaneFormat() {
        super("Freeplane", Type.FileBased, "mm");
    }

    public static FreeplaneFormat getInstance() {
        return instance;
    }
}
