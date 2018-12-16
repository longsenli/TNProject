package com.tnpy.controller;


import com.tnpy.pojo.People;
import com.tnpy.service.IPeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class PeopleController {
    @Autowired
    IPeopleService peopleService;
    @RequestMapping(value = "/name",method = RequestMethod.GET)
    public String hello(String id) {
        return peopleService.getName(id);
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public int hello(int a,int b) {
        return a+b;
    }

    @RequestMapping(value = "/allpeople")
    public List<People> allpeople()
    {
        return peopleService.allPeople();
    }

    @RequestMapping("/list")
    public String  listUser(Model model) {

        return "/peopleWeb";
    }
}
