package com.wugroup.calmanage.demo.service;

import com.wugroup.calmanage.demo.model.Task;
import com.wugroup.calmanage.demo.model.ViewObject;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Protocol;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * task与question在本demo中相同
 * Created by Haozk on 2019/11/25
 */

@Service
public class SearchService {
    private static final String SOLR_UPL="http://127.0.0.1:8983/solr/new_core";
    private HttpSolrClient client =new HttpSolrClient.Builder(SOLR_UPL).build();
    private static final String TASK_NAME = "task_name";
    private static final String TASP_TYPE = "task_type";

    public ViewObject searchQuestion(String keyword, int offset, int count, String hlPre, String hlPos) throws Exception{
        ViewObject vo =new ViewObject();
        List<Task> taskList = new ArrayList<>();
        SolrQuery query = new SolrQuery(keyword);
        query.setRows(count);
        query.setStart(offset);
        query.setHighlight(true);
        query.setHighlightSimplePre(hlPre);
        query.setHighlightSimplePost(hlPos);
        query.set("hl.fl", TASK_NAME + "," + TASP_TYPE);
        QueryResponse response = client.query(query);
        for (Map.Entry<String, Map<String, List<String>>> entry : response.getHighlighting().entrySet()) {
            Task q = new Task();
            q.setId(Integer.parseInt(entry.getKey()));
            if (entry.getValue().containsKey(TASP_TYPE)) {
                List<String> taskTypeList = entry.getValue().get(TASP_TYPE);
                if (taskTypeList.size() > 0) {
                    q.setTaskType(taskTypeList.get(0));
                }
            }
            if (entry.getValue().containsKey(TASK_NAME)) {
                List<String> taskNameList = entry.getValue().get(TASK_NAME);
                if (taskNameList.size() > 0) {
                    q.setTaskName(taskNameList.get(0));
                }
            }
            taskList.add(q);
        }
        vo.set("task",taskList);
        if(response.getHighlighting().entrySet().size()<10) vo.set("lastpage",true);
        return vo;
    }


    public boolean indexQuestion(int qid, String title, String content) throws Exception {
        SolrInputDocument doc =  new SolrInputDocument();
        doc.setField("id", qid);
        doc.setField(TASK_NAME, title);
        doc.setField(TASP_TYPE, content);
        UpdateResponse response = client.add(doc, 1000);
        return response != null && response.getStatus() == 0;
    }
}
