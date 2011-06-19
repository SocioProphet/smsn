package net.fortytwo.myotherbrain.server;

import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.frames.FramesManager;
import com.tinkerpop.rexster.RexsterResourceContext;
import com.tinkerpop.rexster.extension.AbstractRexsterExtension;
import com.tinkerpop.rexster.extension.ExtensionDefinition;
import com.tinkerpop.rexster.extension.ExtensionDescriptor;
import com.tinkerpop.rexster.extension.ExtensionNaming;
import com.tinkerpop.rexster.extension.ExtensionPoint;
import com.tinkerpop.rexster.extension.ExtensionRequestParameter;
import com.tinkerpop.rexster.extension.ExtensionResponse;
import com.tinkerpop.rexster.extension.RexsterContext;
import net.fortytwo.myotherbrain.notes.Note;
import net.fortytwo.myotherbrain.notes.NotesIO;
import net.fortytwo.myotherbrain.notes.NotesViews;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * User: josh
 * Date: 6/19/11
 * Time: 1:40 PM
 */
@ExtensionNaming(namespace = "myotherbrain", name = "read-notes")
public class ReadNotesExtension extends AbstractRexsterExtension {
    private static final Logger LOGGER = Logger.getLogger(ReadNotesExtension.class.getName());

    @ExtensionDefinition(extensionPoint = ExtensionPoint.GRAPH)
    @ExtensionDescriptor(description = "A simple ping extension.")
    public ExtensionResponse evaluatePing(@RexsterContext RexsterResourceContext context,
                                          @RexsterContext Graph graph,
                                          @ExtensionRequestParameter(name = "root", description = "id of the root atom (vertex) to view") String root) {
        try {
            LOGGER.fine("notes request with root " + root);

            try {
                root = new Integer(root).toString();
            } catch (NumberFormatException e) {
                return ExtensionResponse.error("bad root id: " + root);
            }

            Map<String, String> map = new HashMap<String, String>();
            map.put("root", root);

            FramesManager manager = new FramesManager(graph);
            NotesViews m = new NotesViews(graph, manager);
            NotesIO p = new NotesIO();

            Note n = m.toNote(root, null, 3);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try {
                p.writeChildren(n, bos);
                map.put("view", bos.toString());
            } finally {
                bos.close();
            }

            return ExtensionResponse.ok(map);
        } catch (Exception e) {
            // TODO
            e.printStackTrace(System.err);
            return ExtensionResponse.error(e);
        }
    }
}
