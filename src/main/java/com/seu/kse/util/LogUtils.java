package com.seu.kse.util;


import org.apache.log4j.Logger;

public class LogUtils {

    public static void debug(String msg,Class caller){

        Logger logger = Logger.getLogger(caller);
        logger.debug(msg);

    }

    public static void error(String msg,Class caller){

        Logger logger = Logger.getLogger(caller);
        logger.error(msg);
    }

    public static void info(String msg,Class caller){

        Logger logger = Logger.getLogger(caller);
        logger.info(msg);
    }

}
