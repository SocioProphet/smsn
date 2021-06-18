package net.fortytwo.smsn.typeatron.ripple.lib;

import com.illposed.osc.OSCMessage;
import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.smsn.brain.model.Filter;
import net.fortytwo.smsn.brain.model.Note;
import net.fortytwo.smsn.rdf.vocab.SmSnActivityOntology;
import net.fortytwo.smsn.typeatron.TypeatronControl;
import net.fortytwo.smsn.typeatron.ripple.BrainClient;
import org.openrdf.model.IRI;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GiveAtomMapping extends AtomMapping {

    private static final Logger logger = Logger.getLogger(GiveAtomMapping.class.getName());
    private final TypeatronControl typeatron;

    public GiveAtomMapping(final BrainClient client,
                           final Filter filter,
                           final TypeatronControl typeatron) {
        super(client, filter);
        this.typeatron = typeatron;
    }

    public String[] getIdentifiers() {
        return new String[]{
                SmSnLibrary.NS_2014_12 + "give-atom"
        };
    }

    public Parameter[] getParameters() {
        return new Parameter[]{new Parameter("atom", "the reference atom", true)};
    }

    public String getComment() {
        return "prepares an atom for the 'give' half of a hand-off gesture";
    }

    public void apply(RippleList stack,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {

        Object first = stack.getFirst();
        Note n = toNote(first, 0, true);

        if (null == n) {
            logger.warning("can't give non-atom: " + first);
        } else {
            IRI iri = iriOf(n);

            // value is informational; it is used only for development/debugging purposes
            String value = n.getTitle();

            logger.log(Level.INFO, "preparing to give " + iri + " (" + value + ")");

            final List<Object> arguments = new LinkedList<>();
            arguments.add(typeatron.getAgent().getAgentIri().stringValue());
            arguments.add(iri.stringValue());
            //arguments.add(value);
            OSCMessage m = new OSCMessage(SmSnActivityOntology.EXO_ACTIVITY_GIVE, arguments);
            typeatron.getAgent().sendOSCMessageToCoordinator(m);

            // keep the stack unchanged
            solutions.accept(stack);
        }
    }
}
