package com.tnpy.controller;


import com.tnpy.pojo.People;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HelloWorldController {
    @RequestMapping("/list")
    public String  listUser(Model model) {
        List<People> userList = new ArrayList<People>();
        for (int i = 0; i <10; i++)
        {
            userList.add(new People("sdf","张三","sdf","中国广州"));
        }

        model.addAttribute("users", userList);
        return "/peopleWeb";
    }

    @RequestMapping( {"/" , "/index.html"})
    public String  showIndex() {
        return "index";
    }
}
