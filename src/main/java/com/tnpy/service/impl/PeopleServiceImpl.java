package com.tnpy.service.impl;


import com.tnpy.dao.PeopleMapper;
import com.tnpy.pojo.People;
import com.tnpy.service.IPeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PeopleServiceImpl implements IPeopleService {

    @Autowired
    PeopleMapper dbMapper;
    @Override
    public String getName(String ID) {
        return dbMapper.getName(ID);
    }

    @Override
    public String countPeople(String birthday) {
        return null;
    }

    @Override
    public List<People> allPeople()
    {
        return dbMapper.allPeople();
    }

}
