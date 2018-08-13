//package com.seu.kse.listener;
//
//
//import com.seu.kse.service.impl.RecommendationService;
//import com.seu.kse.util.LogUtils;
//
//import javax.servlet.ServletContextEvent;
//import javax.servlet.ServletContextListener;
//import java.util.Calendar;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.ScheduledFuture;
//import java.util.concurrent.TimeUnit;
//
//public class OffLineModelListener implements ServletContextListener {
//    RecommendationService rsService ;
//    public void taskBegin() {
//        //获取时间，设置任务在晚上24 后启动
//        int endHour = 24;
//        Calendar now = Calendar.getInstance();
//        int hour  = now.get(Calendar.HOUR_OF_DAY);
//        int afterHour = endHour - hour;
//        // 定义一个任务
//        final Runnable task = new Runnable() {
//
//            public void run() {
//                rsService.updateModel();
//
//            }
//        };
//        // 参数
//        // command - 要执行的任务
//        // initialDelay - 首次执行的延迟时间
//        // period - 连续执行之间的周期
//        // unit - initialDelay 和 period 参数的时间单位
//        System.out.println("离线推荐任务启动………………………………");
//        LogUtils.info("离线推荐任务启动………………………………",OffLineModelListener.class);
//        taskHandle = scheduler.scheduleAtFixedRate(task, afterHour*60*60, 24*60*60, TimeUnit.SECONDS);
//    }
//
//    public void contextDestroyed(ServletContextEvent sce) {
//
//        System.out.println("contextDestroyed…………");
//        LogUtils.info("contextDestroyed…………",OffLineModelListener.class);
//        taskEnd();
//    }
//
//    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
//    ScheduledFuture<?> taskHandle;
//    public void contextInitialized(ServletContextEvent arg0) {
//        rsService = new RecommendationService();
//        System.out.println("contextInitialized……");
//        LogUtils.info("contextInitialized……",OffLineModelListener.class);
//        taskBegin();
//    }
//    public void taskEnd(){
//        scheduler.shutdown();
//    }
//}
