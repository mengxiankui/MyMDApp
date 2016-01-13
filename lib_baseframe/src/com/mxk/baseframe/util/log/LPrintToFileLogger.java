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


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.mxk.baseapplication.LBaseApplication;
import com.mxk.baseframe.common.CacheDirUtil;


/**
 * @Title TAPrintToFileLogger
 * @Package com.ta.core.util.log
 * @Description TAPrintToFileLogger是TA框架中打印到sdcard上面的日志类
 * @author 白猫
 * @date 2013-1-16 14:25
 * @version V1.0
 */
public class LPrintToFileLogger implements ILogger
{
    private String mPath;
    private Writer mWriter;

    private static final SimpleDateFormat TIMESTAMP_FMT = new SimpleDateFormat(
        "[yyyy-MM-dd HH:mm:ss] ");
    private String basePath = "";
    private static String LOG_DIR = "log";
    private static String BASE_FILENAME = "log";
    private File logDir;

    public LPrintToFileLogger()
    {

    }

    public void open()
    {
        logDir = CacheDirUtil.getDiskCacheDir(LBaseApplication.getContext()
                .getApplicationContext(), LOG_DIR);
        if (!logDir.exists())
        {
            logDir.mkdirs();
            // do not allow media scan
            try
            {
                new File(logDir, ".nomedia").createNewFile();
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        basePath = logDir.getAbsolutePath() + "/" + BASE_FILENAME;
        try
        {
            File file = new File(basePath + "-" + getCurrentTimeString()+".txt");
            if (file.exists())
            {
            	file.delete();
			}
            file.createNewFile();
            mPath = file.getAbsolutePath();
            mWriter = new BufferedWriter(new FileWriter(mPath), 2048);
        }
        catch (Exception e)
        {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

    private String getCurrentTimeString()
    {
        Date now = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return simpleDateFormat.format(now);
    }

    public String getPath()
    {
        return mPath;
    }

    @Override
    public void d(String tag, String message)
    {
        // TODO Auto-generated method stub
        println(LoggerConfig.DEBUG, tag, message);
    }

    @Override
    public void e(String tag, String message)
    {
        println(LoggerConfig.ERROR, tag, message);
    }

    @Override
    public void i(String tag, String message)
    {
        println(LoggerConfig.INFO, tag, message);
    }

    @Override
    public void v(String tag, String message)
    {
        println(LoggerConfig.VERBOSE, tag, message);
    }

    @Override
    public void w(String tag, String message)
    {
        println(LoggerConfig.WARN, tag, message);
    }

    @Override
    public void println(int priority, String tag, String message)
    {
        String printMessage = "";
        switch (priority)
        {
            case LoggerConfig.VERBOSE :
                printMessage = "[V]|"
                    + tag
                    + "|"
                    + LBaseApplication.getContext().getApplicationContext()
                            .getPackageName() + "|" + message;
                break;
            case LoggerConfig.DEBUG :
                printMessage = "[D]|"
                    + tag
                    + "|"
                    + LBaseApplication.getContext().getApplicationContext()
                            .getPackageName() + "|" + message;
                break;
            case LoggerConfig.INFO :
                printMessage = "[I]|"
                    + tag
                    + "|"
                    + LBaseApplication.getContext().getApplicationContext()
                            .getPackageName() + "|" + message;
                break;
            case LoggerConfig.WARN :
                printMessage = "[W]|"
                    + tag
                    + "|"
                    + LBaseApplication.getContext().getApplicationContext()
                            .getPackageName() + "|" + message;
                break;
            case LoggerConfig.ERROR :
                printMessage = "[E]|"
                    + tag
                    + "|"
                    + LBaseApplication.getContext().getApplicationContext()
                            .getPackageName() + "|" + message;
                break;
            default :

                break;
        }
        println(printMessage);

    }

    public void println(String message)
    {
        try
        {
            mWriter.write(TIMESTAMP_FMT.format(new Date()));
            mWriter.write(message);
            mWriter.write('\n');
            mWriter.flush();
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void close()
    {
        if (null == mWriter)
        {
            return;
        }
        try
        {
            mWriter.close();
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
