package net.fortytwo.smsn.server;

import net.fortytwo.smsn.brain.Brain;
import net.fortytwo.smsn.brain.NoteQueries;
import net.fortytwo.smsn.brain.model.Atom;
import net.fortytwo.smsn.brain.model.Filter;
import net.fortytwo.smsn.brain.model.pg.GraphWrapper;
import net.fortytwo.smsn.brain.wiki.NoteReader;
import net.fortytwo.smsn.brain.wiki.NoteWriter;
import org.json.JSONObject;

import java.util.Map;

public class RequestParams {
    private GraphWrapper graphWrapper;
    private Brain brain;
    private String data;
    private Integer height;
    private String file;
    private Filter filter;
    private String format;
    private boolean includeTypes;
    private JSONObject jsonView;
    private Map<String, Object> map;
    private Integer maxResults;
    private NoteReader parser;
    private String propertyName;
    private Object propertyValue;
    private NoteQueries queries;
    private String query;
    private NoteQueries.QueryType queryType;
    private Atom root;
    private String rootId;
    private NoteQueries.ViewStyle style;
    private String styleName;
    private Integer valueCutoff;
    private String wikiView;
    private NoteWriter writer;

    public GraphWrapper getGraphWrapper() {
        return graphWrapper;
    }

    public void setGraphWrapper(GraphWrapper graphWrapper) {
        this.graphWrapper = graphWrapper;
    }

    public Brain getBrain() {
        return brain;
    }

    public void setBrain(Brain brain) {
        this.brain = brain;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public boolean isIncludeTypes() {
        return includeTypes;
    }

    public void setIncludeTypes(boolean includeTypes) {
        this.includeTypes = includeTypes;
    }

    public JSONObject getJsonView() {
        return jsonView;
    }

    public void setJsonView(JSONObject jsonView) {
        this.jsonView = jsonView;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    public Integer getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(Integer maxResults) {
        this.maxResults = maxResults;
    }

    public NoteReader getParser() {
        return parser;
    }

    public void setParser(NoteReader parser) {
        this.parser = parser;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public Object getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(Object propertyValue) {
        this.propertyValue = propertyValue;
    }

    public NoteQueries getQueries() {
        return queries;
    }

    public void setQueries(NoteQueries queries) {
        this.queries = queries;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public NoteQueries.QueryType getQueryType() {
        return queryType;
    }

    public void setQueryType(NoteQueries.QueryType queryType) {
        this.queryType = queryType;
    }

    public Atom getRoot() {
        return root;
    }

    public void setRoot(Atom root) {
        this.root = root;
    }

    public String getRootId() {
        return rootId;
    }

    public void setRootId(String rootId) {
        this.rootId = rootId;
    }

    public NoteQueries.ViewStyle getStyle() {
        return style;
    }

    public void setStyle(NoteQueries.ViewStyle style) {
        this.style = style;
    }

    public String getStyleName() {
        return styleName;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

    public Integer getValueCutoff() {
        return valueCutoff;
    }

    public void setValueCutoff(Integer valueCutoff) {
        this.valueCutoff = valueCutoff;
    }

    public String getWikiView() {
        return wikiView;
    }

    public void setWikiView(String wikiView) {
        this.wikiView = wikiView;
    }

    public NoteWriter getWriter() {
        return writer;
    }

    public void setWriter(NoteWriter writer) {
        this.writer = writer;
    }
}
