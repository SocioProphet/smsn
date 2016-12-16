package net.fortytwo.smsn.brain.io.edges;

import net.fortytwo.smsn.brain.io.Format;

public class EdgeTSVFormat extends Format {
    private static final EdgeTSVFormat instance = new EdgeTSVFormat();

    private EdgeTSVFormat() {
        super("Edges", Type.FileBased, "tsv");
    }

    public static EdgeTSVFormat getInstance() {
        return instance;
    }
}
