/*
 * Copyright (C) 2013  WhiteCat 白猫 (www.thinkandroid.cn)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mxk.baseframe.util.log;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.mxk.baseframe.util.log.ILogger;
import com.mxk.baseframe.util.log.LPrintToLogCatLogger;
import com.mxk.baseframe.util.log.LoggerConfig;
import com.mxk.baseframe.util.log.LoggerConfig.LogType;


/**
 * @Title TALogger
 * @Package com.ta.util.log
 * @Description TALogger是一个日志打印类
 * @author 白猫
 * @date 2013-1-16 14:20
 * @version V1.0
 */
public class Logger
{
    private static HashMap<String, ILogger> loggerHashMap = new HashMap<String, ILogger>();
    private static final ILogger logCatLogger = new LPrintToLogCatLogger();
    private static final ILogger fileLogger = new LPrintToFileLogger();

    public static void setLogType(LogType logType)
    {
        LoggerConfig.setLogType(logType);
        if (logType == LogType.NONE)
        {
            removeLogger(logCatLogger);
            removeLogger(fileLogger);
        }
        else if (logType == LogType.LOGCAT)
        {
            addLogger(logCatLogger);
            removeLogger(fileLogger);
        }
        else if (logType == LogType.FILE)
        {
            removeLogger(logCatLogger);
            addLogger(fileLogger);
        }
        else if (logType == LogType.BOTH)
        {
            addLogger(logCatLogger);
            addLogger(fileLogger);
        }
    }
    
    public static void setLogLevel(int level)
    {
    	LoggerConfig.setLogLevel(level);
    }

    private static void addLogger(ILogger logger)
    {
        String loggerName = logger.getClass().getName();
        if (!loggerHashMap.containsKey(loggerName))
        {
            logger.open();
            loggerHashMap.put(loggerName, logger);
        }

    }

    public static void removeLogger(ILogger logger)
    {
        String loggerName = logger.getClass().getName();
        if (loggerHashMap.containsKey(loggerName))
        {
            logger.close();
            loggerHashMap.remove(loggerName);
        }

    }

    public static void d(Object object, String message)
    {

        printLoger(LoggerConfig.DEBUG, object, message);

    }

    public static void e(Object object, String message)
    {

        printLoger(LoggerConfig.ERROR, object, message);

    }

    public static void i(Object object, String message)
    {

        printLoger(LoggerConfig.INFO, object, message);

    }

    public static void v(Object object, String message)
    {

        printLoger(LoggerConfig.VERBOSE, object, message);

    }

    public static void w(Object object, String message)
    {

        printLoger(LoggerConfig.WARN, object, message);

    }

    public static void d(String tag, String message)
    {

        printLoger(LoggerConfig.DEBUG, tag, message);

    }

    public static void e(String tag, String message)
    {

        printLoger(LoggerConfig.ERROR, tag, message);

    }

    public static void i(String tag, String message)
    {

        printLoger(LoggerConfig.INFO, tag, message);

    }

    public static void v(String tag, String message)
    {

        printLoger(LoggerConfig.VERBOSE, tag, message);

    }

    public static void w(String tag, String message)
    {

        printLoger(LoggerConfig.WARN, tag, message);

    }

    public static void println(int priority, String tag, String message)
    {
        printLoger(priority, tag, message);
    }

    private static void printLoger(int priority, Object object, String message)
    {
        Class<?> cls = object.getClass();
        String tag = cls.getName();
        String arrays[] = tag.split("\\.");
        tag = arrays[arrays.length - 1];
        printLoger(priority, tag, message);
    }

    private static void printLoger(int priority, String tag, String message)
    {
        if (LoggerConfig.needPrintLog(priority))
        {
            Iterator<Entry<String, ILogger>> iter = loggerHashMap.entrySet().iterator();
            while (iter.hasNext())
            {
                Map.Entry<String, ILogger> entry = iter.next();
                ILogger logger = entry.getValue();
                if (logger != null)
                {
                    printLoger(logger, priority, tag, message);
                }
            }
        }
    }

    private static void printLoger(ILogger logger, int priority, String tag,
            String message)
    {

        switch (priority)
        {
            case LoggerConfig.VERBOSE :
                logger.v(tag, message);
                break;
            case LoggerConfig.DEBUG :
                logger.d(tag, message);
                break;
            case LoggerConfig.INFO :
                logger.i(tag, message);
                break;
            case LoggerConfig.WARN :
                logger.w(tag, message);
                break;
            case LoggerConfig.ERROR :
                logger.e(tag, message);
                break;
            default :
                break;
        }
    }
}
