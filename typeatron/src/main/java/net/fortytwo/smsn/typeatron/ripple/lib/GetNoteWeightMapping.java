package net.fortytwo.smsn.typeatron.ripple.lib;

import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.smsn.brain.model.Filter;
import net.fortytwo.smsn.brain.model.entities.Note;
import net.fortytwo.smsn.typeatron.ripple.BrainClient;

import java.util.logging.Logger;

public class GetNoteWeightMapping extends NoteMapping {

    private static final Logger logger = Logger.getLogger(GetNoteWeightMapping.class.getName());

    public GetNoteWeightMapping(final BrainClient client,
                                final Filter filter) {
        super(client, filter);
    }

    public String[] getIdentifiers() {
        return new String[]{
                SmSnLibrary.NS_2014_12 + "get-note-weight"
        };
    }

    public Parameter[] getParameters() {
        return new Parameter[]{new Parameter("note", "the reference note", true)};
    }

    public String getComment() {
        return "gets the @weight property of a note";
    }

    public void apply(RippleList stack,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {

        Object first = stack.getFirst();
        stack = stack.getRest();

        Note n = toTree(first, 0, true);

        if (null == n) {
            logger.warning("can't get @weight of non-note: " + first);
        } else {
            Float value = n.getWeight();
            if (null != value) {
                // put both the @weight property and the (synced) note back on the stack
                solutions.accept(stack.push(n).push(value));
            }
        }
    }
}
