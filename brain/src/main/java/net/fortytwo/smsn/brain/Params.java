package net.fortytwo.smsn.brain;

public interface Params {
    public enum Format{json, wiki};

    public static final String
            ACTION = "action",
            DATASET = "dataset",
            DEFAULT_SHARABILITY = "defaultSharability",
            DEFAULT_WEIGHT = "defaultWeight",
            FILE = "file",
            FILTER = "filter",
            FORMAT = "format",
            HEIGHT = "height",
            ID = "id",
            INCLUDE_TYPES = "includeTypes",
            MAX_RESULTS = "maxResults",
            MAX_SHARABILITY = "maxSharability",
            MAX_WEIGHT = "maxWeight",
            MIN_SHARABILITY = "minSharability",
            MIN_WEIGHT = "minWeight",
            NAME = "name",
            QUERY = "query",
            QUERY_TYPE = "queryType",
            REQUEST = "request",
            ROOT = "root",
            STYLE = "style",
            TITLE = "title",
            VALUE = "value",
            VALUE_CUTOFF = "valueCutoff",
            VIEW = "view",
            VIEW_FORMAT = "viewFormat";
}
