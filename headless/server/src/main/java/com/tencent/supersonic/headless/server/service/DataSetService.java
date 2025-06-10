package com.tencent.supersonic.headless.server.service;

import com.tencent.supersonic.common.pojo.User;
import com.tencent.supersonic.headless.api.pojo.MetaFilter;
import com.tencent.supersonic.headless.api.pojo.request.DataSetReq;
import com.tencent.supersonic.headless.api.pojo.request.QueryDataSetReq;
import com.tencent.supersonic.headless.api.pojo.request.SemanticQueryReq;
import com.tencent.supersonic.headless.api.pojo.response.DataSetResp;
import com.tencent.supersonic.headless.api.pojo.response.DimensionResp;
import com.tencent.supersonic.headless.api.pojo.response.MetricResp;

import java.util.List;
import java.util.Map;

public interface DataSetService {

    DataSetResp save(DataSetReq dataSetReq, User user);

    DataSetResp update(DataSetReq dataSetReq, User user);

    DataSetResp getDataSet(Long id);

    List<DataSetResp> getDataSetList(MetaFilter metaFilter);

    void delete(Long id, User user);

    Map<Long, List<Long>> getModelIdToDataSetIds(List<Long> dataSetIds, User user);

    Map<Long, List<Long>> getModelIdToDataSetIds();

    List<DimensionResp> getDataSetDimensionRecord(DataSetResp dataSetResp);

    List<MetricResp> getDataSetMetricRecord(DataSetResp dataSetResp);

    List<DataSetResp> getDataSets(String dataSetName, User user);

    List<DataSetResp> getDataSets(List<String> dataSetNames, User user);

    public List<DataSetResp> getDataSetsByAuth(User user, MetaFilter metaFilter);

    List<Long> getDataSetsInheritAuth(User user);

    List<DataSetResp> getDataSetsInheritAuth(User user, Long domainId);

    SemanticQueryReq convert(QueryDataSetReq queryDataSetReq);

    Long getDataSetIdFromSql(String sql, User user);
}
