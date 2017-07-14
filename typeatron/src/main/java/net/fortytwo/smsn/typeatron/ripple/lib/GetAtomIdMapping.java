package net.fortytwo.smsn.typeatron.ripple.lib;

import net.fortytwo.smsn.brain.model.Filter;
import net.fortytwo.smsn.brain.model.entities.Link;
import net.fortytwo.smsn.brain.model.entities.TreeNode;
import net.fortytwo.smsn.brain.query.TreeViews;
import net.fortytwo.smsn.typeatron.ripple.BrainClient;
import net.fortytwo.flow.Sink;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RippleList;

import java.util.logging.Logger;

public class GetAtomIdMapping extends AtomMapping {

    private static final Logger logger = Logger.getLogger(GetAtomIdMapping.class.getName());

    public GetAtomIdMapping(final BrainClient client,
                            final Filter filter) {
        super(client, filter);
    }

    public String[] getIdentifiers() {
        return new String[]{
                SmSnLibrary.NS_2014_12 + "get-atom-id"
        };
    }

    public Parameter[] getParameters() {
        return new Parameter[]{new Parameter("atom", "the reference atom", true)};
    }

    public String getComment() {
        return "gets the unique id of an atom";
    }

    public void apply(RippleList stack,
                      final Sink<RippleList> solutions,
                      final ModelConnection mc) throws RippleException {

        Object first = stack.getFirst();
        stack = stack.getRest();

        TreeNode<Link> n = toTree(first, 0, true);

        if (null == n) {
            logger.warning("can't get id of non-atom: " + first);
        } else {
            String id = TreeViews.getId(n);
            if (null != id) {
                // put both the id and the (synced) atom back on the stack
                solutions.accept(stack.push(n).push(id));
            }
        }
    }
}
