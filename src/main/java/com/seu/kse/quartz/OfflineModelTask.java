package com.seu.kse.quartz;


import com.seu.kse.service.impl.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class OfflineModelTask {
    @Autowired
    RecommendationService rs ;

    public void execute(){
        rs.updateModel();
    }

}
