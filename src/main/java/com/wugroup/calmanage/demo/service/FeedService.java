package com.wugroup.calmanage.demo.service;

import com.wugroup.calmanage.demo.dao.FeedDAO;
import com.wugroup.calmanage.demo.model.Feed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Haozk on 2019/11/18
 */
@Service
public class FeedService {

    @Autowired
    FeedDAO feedDAO;

    public List<Feed> getUserFeeds(int maxId, List<Integer> userIds, int count) {
        return feedDAO.selectUserFeeds(maxId, userIds, count);
    }

    public boolean addFeed(Feed feed) {
        feedDAO.addFeed(feed);
        return feed.getId() > 0;
    }

    public Feed getById(int id) {
        return feedDAO.getFeedById(id);
    }
}
