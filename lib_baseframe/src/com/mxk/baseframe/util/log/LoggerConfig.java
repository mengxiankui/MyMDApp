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


public class LoggerConfig
{
    /**
     * Priority constant for the println method; use Logger.v.
     */
    public static final int VERBOSE = 2;

    /**
     * Priority constant for the println method; use Logger.d.
     */
    public static final int DEBUG = 3;

    /**
     * Priority constant for the println method; use Logger.i.
     */
    public static final int INFO = 4;

    /**
     * Priority constant for the println method; use Logger.w.
     */
    public static final int WARN = 5;

    /**
     * Priority constant for the println method; use Logger.e.
     */
    public static final int ERROR = 6;
    /**
     * Priority constant for the println method.
     */
    public static final int ASSERT = 7;

    private static int loglevel = ERROR;

    private static LogType logtype = LogType.NONE;

    public static enum LogType
    {
        NONE, LOGCAT, FILE, BOTH;
    }

    public static int getLogLevel()
    {
        return loglevel;
    }

    public static void setLogLevel(int level)
    {
        LoggerConfig.loglevel = level;
    }

    public static LogType getLogType()
    {
        return logtype;
    }

    public static void setLogType(LogType logtype)
    {
        LoggerConfig.logtype = logtype;
    }

    public static boolean needPrintLog(int level)
    {
        if (loglevel <= level)
        {
            return true;
        }
        return false;
    }

}
