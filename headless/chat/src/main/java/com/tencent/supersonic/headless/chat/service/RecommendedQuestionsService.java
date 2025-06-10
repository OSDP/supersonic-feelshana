package com.tencent.supersonic.headless.chat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tencent.supersonic.headless.chat.persistence.dataobject.RecommendedQuestions;

import java.util.List;


public interface RecommendedQuestionsService extends IService<RecommendedQuestions> {
    /**
     * 根据agentId和question查询对应记录，如果存在则返回它的querySql，否则返回null
     */
    String findQuerySqlByQuestion(Integer agentId, String question);

    /**
     * 根据dataSetId查询对应的question
     */
    List<String> findQuestionByDataSetId(Long dataSetId);
}
