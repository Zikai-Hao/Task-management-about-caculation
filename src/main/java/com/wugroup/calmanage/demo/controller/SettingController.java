package com.wugroup.calmanage.demo.controller;

import com.wugroup.calmanage.demo.Util.JedisAdapter;
import com.wugroup.calmanage.demo.Util.JedisKeyUtil;
import com.wugroup.calmanage.demo.async.EventModel;
import com.wugroup.calmanage.demo.async.EventType;
import com.wugroup.calmanage.demo.model.EntityType;
import com.wugroup.calmanage.demo.model.HostHolder;
import com.wugroup.calmanage.demo.model.User;
import com.wugroup.calmanage.demo.model.ViewObject;
import com.wugroup.calmanage.demo.service.CommentService;
import com.wugroup.calmanage.demo.service.DemoService;
import com.wugroup.calmanage.demo.service.FollowService;
import com.wugroup.calmanage.demo.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Set;

/**
 * Created by Haozk on 2019/11/16
 */


@Controller
public class SettingController {
    private final static Logger logger = LoggerFactory.getLogger(SettingController.class);
    @Autowired
    DemoService demoService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    UserService userService;

    @Autowired
    JedisAdapter jedisAdapter;

    @Autowired
    CommentService commentService;

    @Autowired
    FollowService followService;

    private ViewObject getVo(){


        int userId = hostHolder.getUser().getId();
        User user = userService.getUser(userId);
        String motto = jedisAdapter.get(JedisKeyUtil.getMotto(userId));
        motto = motto==null?"还没有签名": motto;
        ViewObject vo = new ViewObject();
        vo.set("motto",motto);
        vo.set("user", user);
        vo.set("commentCount", commentService.getUserCommentCount(userId));
        vo.set("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER, userId));
        vo.set("followeeCount", followService.getFolloweeCount(userId, EntityType.ENTITY_USER));
        return vo;
    }

    /**
     * 设置成功 待修改
     * @return
     */
    @RequestMapping(path = {"/setting"}, method = {RequestMethod.GET})
    public String user(Model model){
        if(hostHolder==null){
            return "redirect:/reglogin";
        }

        model.addAttribute("profileUser", getVo());
        return "setting";
    }
    @RequestMapping(path={"/set/"},method = {RequestMethod.POST})
    public String reg(Model model,

                      @RequestParam("motto") String motto,
                      HttpServletResponse response){


        try {
            if(hostHolder==null){
                return "redirect:/reglogin";
            }


            if(userService.updateMotto(motto,hostHolder.getUser().getId())) model.addAttribute("msg","修改成功");
            else model.addAttribute("msg","修改失败，请重试或联系管理员");
            model.addAttribute("profileUser", getVo());
            return "setting";


        }catch(Exception e){
            logger.error("个人资料修改异常"+e.getMessage());
            model.addAttribute("msg","修改失败，请重试或联系管理员");
            return "setting";

        }

    }
}
