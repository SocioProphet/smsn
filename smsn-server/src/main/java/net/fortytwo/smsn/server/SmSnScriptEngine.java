package net.fortytwo.smsn.server;

import net.fortytwo.smsn.SemanticSynchrony;
import net.fortytwo.smsn.brain.Params;
import net.fortytwo.smsn.server.action.BroadcastRDF;
import net.fortytwo.smsn.server.action.EvaluateRippleQuery;
import net.fortytwo.smsn.server.action.EvaluateTextSearch;
import net.fortytwo.smsn.server.action.FindDuplicates;
import net.fortytwo.smsn.server.action.FindIsolatedAtoms;
import net.fortytwo.smsn.server.action.FindRoots;
import net.fortytwo.smsn.server.action.GetEvents;
import net.fortytwo.smsn.server.action.GetHistory;
import net.fortytwo.smsn.server.action.GetPriorities;
import net.fortytwo.smsn.server.action.GetView;
import net.fortytwo.smsn.server.action.InferTypes;
import net.fortytwo.smsn.server.action.Ping;
import net.fortytwo.smsn.server.action.PushEvent;
import net.fortytwo.smsn.server.action.ReadGraph;
import net.fortytwo.smsn.server.action.RemoveIsolatedAtoms;
import net.fortytwo.smsn.server.action.SetProperties;
import net.fortytwo.smsn.server.action.UpdateView;
import net.fortytwo.smsn.server.action.WriteGraph;
import org.apache.tinkerpop.gremlin.jsr223.GremlinScriptEngine;
import org.apache.tinkerpop.gremlin.jsr223.GremlinScriptEngineFactory;
import org.apache.tinkerpop.gremlin.neo4j.structure.Neo4jGraph;
import org.apache.tinkerpop.gremlin.process.traversal.Bytecode;
import org.apache.tinkerpop.gremlin.process.traversal.Traversal;
import org.json.JSONException;
import org.json.JSONObject;

import javax.script.AbstractScriptEngine;
import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptException;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

public class SmSnScriptEngine extends AbstractScriptEngine implements GremlinScriptEngine {

    protected static final Logger logger = Logger.getLogger(SmSnScriptEngine.class.getName());

    private final Map<String, Action> actionsByName;

    private final GremlinScriptEngineFactory factory;

    public SmSnScriptEngine(GremlinScriptEngineFactory factory) {
        this.factory = factory;

        actionsByName = new HashMap<>();

        // TODO: replace with a classloader
        add(new BroadcastRDF());
        add(new FindDuplicates());
        add(new WriteGraph());
        add(new FindIsolatedAtoms());
        add(new FindRoots());
        add(new GetEvents());
        add(new GetHistory());
        add(new ReadGraph());
        add(new InferTypes());
        add(new GetPriorities());
        add(new Ping());
        add(new PushEvent());
        add(new RemoveIsolatedAtoms());
        add(new EvaluateRippleQuery());
        add(new EvaluateTextSearch());
        add(new SetProperties());
        add(new UpdateView());
        add(new GetView());
    }


    @Override
    public Object eval(String script, ScriptContext context) throws ScriptException {

        Neo4jGraph graph = getNeo4jGraph(context);

        try {
            return handleRequest(script, graph);
        } catch (JSONException e) {
            throw new IllegalStateException(e);
        }
    }

    // note: this is a hack.  This is currently how config properties are loaded.
    @Override
    public Object eval(Reader reader, ScriptContext context) throws ScriptException {
        Properties properties = new Properties();
        try {
            properties.load(reader);
        } catch (IOException e) {
            throw new ScriptException(e);
        }

        SemanticSynchrony.addConfiguration(properties);
        return "added " + properties.size() + " configurations properties";
    }

    @Override
    public Traversal.Admin eval(Bytecode bytecode, Bindings bindings) throws ScriptException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void put(String key, Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Bindings createBindings() {
        throw new UnsupportedOperationException();
    }

    @Override
    public GremlinScriptEngineFactory getFactory() {
        return factory;
    }

    private Neo4jGraph getNeo4jGraph(final ScriptContext context) {
        Neo4jGraph graph = (Neo4jGraph) context.getAttribute("graph");
        if (null == graph) {
            throw new IllegalStateException("expected Neo4j graph not found");
        }

        return graph;
    }

    private void add(Action action) {
        actionsByName.put(action.getName(), action);
    }

    private JSONObject handleRequest(String requestStr, Neo4jGraph graph) throws JSONException {
        JSONObject request = new JSONObject(requestStr);

        String actionName;
        try {
            actionName = request.getString(Params.ACTION);
        } catch (JSONException e) {
            throw new IllegalStateException(e);
        }

        if (null == actionName) {
            throw new IllegalArgumentException("action not found");
        }

        RequestParams params = Action.createParams(graph);

        Action action = actionsByName.get(actionName);
        if (null == action) {
            throw new IllegalArgumentException("unsupported action: " + actionName);
        }

        action.parseRequest(request, params);

        action.handleRequest(params);

        return toJson(params.getMap());
    }

    private JSONObject toJson(final Map<String, Object> map) {
        return new JSONObject(map);
    }
}
