package net.fortytwo.smsn.brain.rdf.classes.collections;

import net.fortytwo.smsn.brain.rdf.AtomCollection;
import net.fortytwo.smsn.brain.rdf.AtomRegex;
import net.fortytwo.smsn.brain.rdf.classes.DatedEvent;

import java.util.Arrays;
import java.util.Collections;
import java.util.regex.Pattern;

public class Log extends AtomCollection {

    public Log() {
        super(
                "log",
                Pattern.compile("log of .+"),
                null,
                new AtomRegex(Collections.singletonList(
                        new AtomRegex.El(null,
                                AtomRegex.Modifier.OneOrMore, DatedEvent.class, Log.class)
                )));
    }
}
