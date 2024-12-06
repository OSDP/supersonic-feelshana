package com.tencent.supersonic.headless.core.translator.converter;

import com.tencent.supersonic.headless.api.pojo.enums.ModelDefineType;
import com.tencent.supersonic.headless.api.pojo.response.ModelResp;
import com.tencent.supersonic.headless.api.pojo.response.SemanticSchemaResp;
import com.tencent.supersonic.headless.core.pojo.QueryStatement;
import com.tencent.supersonic.headless.core.translator.parser.s2sql.DataModel;
import com.tencent.supersonic.headless.core.utils.SqlVariableParseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component("SqlVariableConverter")
public class SqlVariableConverter implements QueryConverter {

    @Override
    public boolean accept(QueryStatement queryStatement) {
        return Objects.nonNull(queryStatement.getStructQueryParam())
                && !queryStatement.getIsS2SQL();
    }

    @Override
    public void convert(QueryStatement queryStatement) {
        SemanticSchemaResp semanticSchemaResp = queryStatement.getSemanticSchemaResp();
        List<ModelResp> modelResps = semanticSchemaResp.getModelResps();
        if (CollectionUtils.isEmpty(modelResps)) {
            return;
        }
        for (ModelResp modelResp : modelResps) {
            if (ModelDefineType.SQL_QUERY.getName()
                    .equalsIgnoreCase(modelResp.getModelDetail().getQueryType())) {
                String sqlParsed =
                        SqlVariableParseUtils.parse(modelResp.getModelDetail().getSqlQuery(),
                                modelResp.getModelDetail().getSqlVariables(),
                                queryStatement.getStructQueryParam().getParams());
                DataModel dataModel =
                        queryStatement.getOntology().getDataModelMap().get(modelResp.getBizName());
                dataModel.setSqlQuery(sqlParsed);
            }
        }
    }
}
