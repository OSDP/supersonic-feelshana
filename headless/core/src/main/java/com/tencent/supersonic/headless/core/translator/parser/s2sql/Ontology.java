package com.tencent.supersonic.headless.core.translator.parser.s2sql;

import com.tencent.supersonic.headless.core.pojo.Database;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class Ontology {

    private List<Metric> metrics = new ArrayList<>();
    private Map<String, DataModel> dataModelMap = new HashMap<>();
    private Map<String, List<Dimension>> dimensionMap = new HashMap<>();
    private List<Materialization> materializationList = new ArrayList<>();
    private List<JoinRelation> joinRelations;
    private Database database;

    public List<Dimension> getDimensions() {
        return dimensionMap.values().stream().flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

}
