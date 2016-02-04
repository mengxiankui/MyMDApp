package com.qianghongbao.helper;

import android.content.ContentValues;

import com.example.util.Consts;
import com.example.util.TimeUtil;
import com.mxk.baseframe.util.log.Logger;
import com.qianghongbao.services.QHBAccessibilityService;

/**
 * Created by xiankuimeng on 16-2-4.
 */
public class HongBaoDBHelper extends IDBHelper{

    private static final String LOG_TAG = HongBaoDBHelper.class.getSimpleName();
    public void addMoney(float money, String name)
    {
        Logger.d(LOG_TAG,"addMoney,money = " +money + ", name = " + name);
        if (null != QHBAccessibilityService.getService())
        {
            ContentValues contentValues = new ContentValues();
            contentValues.put(Consts.WeixinQHBConst.MONEY,money);
            contentValues.put(Consts.WeixinQHBConst.NAME,name);
            contentValues.put(Consts.WeixinQHBConst.DATE, TimeUtil.getDBDateFormat());
            QHBAccessibilityService.getService().getContentResolver().insert(Consts.WeixinQHBConst.CONTENT_URI,contentValues);
        }

    }

    public static IDBHelper getInstance() {
        if (null == helper)
        {
            helper = new HongBaoDBHelper();
        }
        return helper;
    }
}
