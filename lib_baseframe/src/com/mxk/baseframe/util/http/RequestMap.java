package com.mxk.baseframe.util.http;


import android.text.TextUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;


public class RequestMap
{
    private LinkedList<PFile> fList = new LinkedList<PFile>();
    private LinkedList<Param> pList = new LinkedList<Param>();
    private LinkedHashMap<String, String> pListMaps = new LinkedHashMap<String, String>();
    private boolean upload = false;

    
    
    public RequestMap() {
		super();
		// TODO Auto-generated constructor stub
	}
/**
	public RequestMap(Object object)
    {
        setParams(object);
    }

    private void setParams(Object object)
    {
        // TODO Auto-generated method stub
        List<Field> supportedFields = new ArrayList<Field>();

        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields)
        {
            int modifiers = field.getModifiers();
            if (!Modifier.isStatic(modifiers))
            {
                Class<?> fieldTypeClass = field.getType();
                String fieldType = fieldTypeClass.getName();
                if ("java.lang.String".equals(fieldType))
                {
                    supportedFields.add(field);
                }
            }
        }
        for (Field field : supportedFields)
        {
            try
            {
                field.setAccessible(true);//修改访问权限
                put(field.getName(), (String) field.get(object));
            }
            catch (IllegalAccessException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (IllegalArgumentException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    */

    class Param
    {
        String key;
        String value;
    }

    class PFile
    {
        String name;
        File file;
    }

    public LinkedList<PFile> getFList()
    {
        return this.fList;
    }

    public LinkedHashMap<String, String> getMap()
    {
        return this.pListMaps;
    }

    public String getParams()
    {
        StringBuffer sb = new StringBuffer();
        Iterator<Param> iterator = pList.iterator();
        if (iterator.hasNext())
        {
            Param p = (Param) iterator.next();
            String v = p.value;
            if (TextUtils.isEmpty(v))
            {
                v = "";
            }
            else
            {
                try
                {
                    String str = URLEncoder.encode(v, "utf-8");
                    sb.append(p.key).append("=").append((String) str).append("&");
                }
                catch (UnsupportedEncodingException unsupportedEncodingException)
                {
                    unsupportedEncodingException.printStackTrace();
                }
            }
        }
        sb.deleteCharAt(-1 + sb.length());
        return sb.toString();
    }

    boolean hasParams()
    {
        return this.pList.size() > 0;
    }

    boolean hasUpload()
    {
        return this.upload;
    }

    public void put(String name, File file)
    {
        PFile f = new PFile();
        f.name = name;
        f.file = file;
        this.fList.add(f);
        this.upload = true;
    }

    public void put(String key, String value)
    {
        if ((TextUtils.isEmpty(key)) || (value == null))
        {
            return;
        }
        Param p = new Param();
        p.key = key;
        p.value = value;
        this.pList.add(p);
        this.pListMaps.put(p.key, p.value);
    }
}