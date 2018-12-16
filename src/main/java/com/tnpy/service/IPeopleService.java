package com.tnpy.service;


import com.tnpy.pojo.People;

import java.util.List;

public interface IPeopleService {
    public String getName(String ID);
    public  String countPeople(String birthday);
    public List<People> allPeople();
}
