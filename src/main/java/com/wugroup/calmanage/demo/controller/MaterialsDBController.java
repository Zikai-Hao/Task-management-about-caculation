package com.wugroup.calmanage.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Haozk on 2019/11/30
 */
@Controller
public class MaterialsDBController {
    @RequestMapping("/materialsDB")
    public String materialsDB(){
        return "materialsDB";
    }
}
