package com.seu.kse.quartz;

import com.seu.kse.service.DataInjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class DataInjectTask {

    private final DataInjectService dj ;

    @Autowired
    public DataInjectTask(DataInjectService dj){
        this.dj = dj;
        dj.dataInject_init();
    }
    public void execute(){

        dj.dataInject();
    }
}
