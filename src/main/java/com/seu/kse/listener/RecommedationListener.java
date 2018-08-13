//package com.seu.kse.listener;
//
//import com.seu.kse.service.impl.RecommendationService;
//import com.seu.kse.util.LogUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//
//import javax.servlet.ServletContextEvent;
//import javax.servlet.ServletContextListener;
//import java.util.Calendar;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.ScheduledFuture;
//import java.util.concurrent.TimeUnit;
//
///**
// * Created by yaosheng on 2017/6/3.
// */
//@Service
//public class RecommedationListener implements ServletContextListener {
//    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
//    ScheduledFuture<?> taskHandle;
//    @Autowired
//    RecommendationService rs;
//
//    public void contextInitialized(ServletContextEvent servletContextEvent) {
//        System.out.println("初始化推荐算法");
//
//        rs.init();
//        taskBegin();
//    }
//    public void taskBegin() {
//        //推荐时间在第二天的9点
//        int endHour = 24;
//        Calendar now  = Calendar.getInstance();
//        int nowHour = now.get(Calendar.HOUR_OF_DAY);
//        int afterHour = endHour - nowHour + 9;
//
//        // 定义一个任务
//        final Runnable task = new Runnable() {
//
//
//            public void run() {
//
//                LogUtils.info("开始推荐......",RecommedationListener.class);
//                System.out.println("开始推荐………………………………");
//                try {//yuantf乱加的
//
//                    rs.recommend(5);
//                    System.out.println("结束推荐………………………………");
//                    LogUtils.info("结束推荐......",RecommedationListener.class);
//                }
//                catch(Exception ex){
//                    ex.printStackTrace();
//
//                    rs.recommend(5);
//                }
//            }
//        };
//        // 参数
//        // command - 要执行的任务
//        // initialDelay - 首次执行的延迟时间
//        // period - 连续执行之间的周期
//        // unit - initialDelay 和 period 参数的时间单位
//        System.out.println("任务启动………………………………");
//
//        taskHandle = scheduler.scheduleAtFixedRate(task, afterHour*60*60, 24*60*60, TimeUnit.SECONDS);
//    }
//    public void taskEnd(){
//        scheduler.shutdown();
//    }
//    public void contextDestroyed(ServletContextEvent servletContextEvent) {
//        taskEnd();
//    }
//}
