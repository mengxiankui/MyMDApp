package com.mxk.utils.tuling;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.mxk.myapp.R;
import com.mxk.utils.APIKeys;


public class TuLingUtil
{
    public static final String LOG_TAG = "TuLingUtil";
    private String tulingQueryUrl = " http://www.tuling123.com/openapi/api?key="
        + APIKeys.TULING_KEY_HANZI + "&info=";

    private HashMap<String, SoftReference<Bitmap>> imageCache = null;
    private File cacheDir;
    private Context context;

    //返回码说明
    /** 文本类数据 */
    public static final int RETUEN_TYPE_TEXT = 100000;
    /** 网址类数据 */
    public static final int RETUEN_TYPE_URL = 200000;
    /** 新闻 */
    public static final int RETUEN_TYPE_NEWS = 302000;
    /** 应用、软件、下载 */
    public static final int RETUEN_TYPE_SOFTWARE = 304000;
    /** 列车 */
    public static final int RETUEN_TYPE_TRAINS = 305000;
    /** 航班 */
    public static final int RETUEN_TYPE_FLIGHT = 306000;
    /** 电影、菜谱、视频、小说 */
    public static final int RETUEN_TYPE_MOVIE = 308000;
    /** 酒店 */
    public static final int RETUEN_TYPE_HOTEL = 309000;
    /** 价格 */
    public static final int RETUEN_TYPE_PRICE = 311000;
    /** key的长度错误（32位） */
    public static final int RETUEN_TYPE_KEY_LENGTH_ERR = 40001;
    /** 请求内容为空 */
    public static final int RETUEN_TYPE_CONTENT_EMPTY_ERR = 40002;
    /** key错误或帐号未激活 */
    public static final int RETUEN_TYPE_KEY_ERR = 40003;
    /** 当天请求次数已用完 */
    public static final int RETUEN_TYPE_OUT_OF_QUOTA_ERR = 40004;
    /** 暂不支持该功能 */
    public static final int RETUEN_TYPE_NOT_SUPPORT_ERR = 40005;
    /** 服务器升级中 */
    public static final int RETUEN_TYPE_SERVER_UPDATING_ERR = 40006;
    /** 服务器数据格式异常 */
    public static final int RETUEN_TYPE_SERVER_DATA_ERR = 40007;

    //图灵机器人返回

    public static final String RETURN_CODE = "code";
    public static final String RETURN_TEXT = "text";
    public static final String RETURN_LIST = "list";

    //链接类
    public static final String RETURN_URL = "url";

    //新闻类
    public static final String RETURN_NEWS_ARTICLE = "article";
    public static final String RETURN_NEWS_SOURCE = "source";
    public static final String RETURN_NEWS_DETAILURL = "detailurl";
    public static final String RETURN_NEWS_ICON = "icon";

    //软件下载
    public static final String RETURN_SOFTWARE_NAME = "name";
    public static final String RETURN_SOFTWARE_COUNT = "count";
    public static final String RETURN_SOFTWARE_DETAILURL = "detailurl";
    public static final String RETURN_SOFTWARE_ICON = "icon";

    //列车
    public static final String RETURN_TRAIN_NUM = "trainnum";
    public static final String RETURN_TRAIN_START = "start";
    public static final String RETURN_TRAIN_TERMINAL = "terminal";
    public static final String RETURN_TRAIN_STARTTIME = "starttime";
    public static final String RETURN_TRAIN_ENDTIME = "endtime";
    public static final String RETURN_TRAIN_DETAILURL = "detailurl";
    public static final String RETURN_TRAIN_ICON = "icon";

    //航班
    public static final String RETURN_FLIGHT_FLIGHT = "flight";
    public static final String RETURN_FLIGHT_ROUTE = "route";
    public static final String RETURN_FLIGHT_STARTTIME = "starttime";
    public static final String RETURN_FLIGHT_ENDTIME = "endtime";
    public static final String RETURN_FLIGHT_STATE = "state";
    public static final String RETURN_FLIGHT_DETAILURL = "detailurl";
    public static final String RETURN_FLIGHT_ICON = "icon";

    //电影、视频、菜谱、小说
    public static final String RETURN_MOVIE_NAME = "name";
    public static final String RETURN_MOVIE_INFO = "info";
    public static final String RETURN_MOVIE_DETAILURL = "detailurl";
    public static final String RETURN_MOVIE_ICON = "icon";

    //酒店
    public static final String RETURN_HOTEL_NAME = "name";
    public static final String RETURN_HOTEL_PRICE = "price";
    public static final String RETURN_HOTEL_SATISFACTION = "satisfaction";
    public static final String RETURN_HOTEL_COUNT = "count";
    public static final String RETURN_HOTEL_DETAILURL = "detailurl";
    public static final String RETURN_HOTEL_ICON = "icon";

    //价格
    public static final String RETURN_PRICE_NAME = "name";
    public static final String RETURN_PRICE_PRICE = "price";
    public static final String RETURN_PRICE_DETAILURL = "detailurl";
    public static final String RETURN_PRICE_ICON = "icon";

    private static TuLingUtil instance;

    private ITuLing callback;

    public ITuLing getCallback()
    {
        return callback;
    }

    public void setCallback(ITuLing callback)
    {
        this.callback = callback;
    }

    public static TuLingUtil getInstance()
    {
        if (null == instance)
        {
            throw new IllegalArgumentException("TuLingUtil must setInstance first");
        }
        return instance;

    }

    public static void setInstance(String key, Context context)
    {
        if (null == instance)
        {
            instance = new TuLingUtil();
            instance.imageCache = new HashMap<String, SoftReference<Bitmap>>();
            instance.tulingQueryUrl = " http://www.tuling123.com/openapi/api?key=" + key
                + "&info=";
            instance.context = context;
        }

    }

    private class MyAsyncTask extends AsyncTask<String, Integer, TuLingData>
    {

        @Override
        protected void onPostExecute(TuLingData result)
        {
            // TODO Auto-generated method stub
            if (null != getCallback())
            {
                callback.TuLingCallback(result);
            }
        }

        @Override
        protected TuLingData doInBackground(String... params)
        {
            // TODO Auto-generated method stub
            URL getUrl;
            JSONObject object;
            try
            {
                getUrl = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) getUrl
                        .openConnection();
                connection.connect();

                // 取得输入流，并使用Reader读取 
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(), "utf-8"));
                StringBuffer sb = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line);
                }
                reader.close();
                // 断开连接 
                connection.disconnect();
                //            String strReturnWords = TuLingUtil.getMessageText(sb.toString());
                object = new JSONObject(sb.toString());
                if (object != null)
                {
                    Log.d(LOG_TAG, object.toString());
                    TuLingData data = new TuLingData();
                    JSONArray array;
                    data.setType(object.getInt(RETURN_CODE));
                    data.setText(object.getString(RETURN_TEXT));
                    switch (object.getInt(RETURN_CODE))
                    {
                        case RETUEN_TYPE_TEXT :

                            break;
                        case RETUEN_TYPE_URL :

                            data.setUrl(object.getString(RETURN_URL));
                            break;
                        case RETUEN_TYPE_NEWS :

                            array = object.getJSONArray(RETURN_LIST);
                            ArrayList<TuLingNews> listTuLingNews = new ArrayList<TuLingNews>();
                            for (int i = 0; i < array.length(); i++)
                            {
                                TuLingNews news = new TuLingNews();
                                JSONObject jsonObject = array.getJSONObject(i);
                                news.setArticle(jsonObject.getString(RETURN_NEWS_ARTICLE));
                                news.setSource(jsonObject.getString(RETURN_NEWS_SOURCE));
                                news.setDetailurl(jsonObject
                                        .getString(RETURN_NEWS_DETAILURL));
                                news.setIcon(jsonObject.getString(RETURN_NEWS_ICON));
                                listTuLingNews.add(news);
                            }
                            data.setListTuLingNews(listTuLingNews);
                            break;
                        case RETUEN_TYPE_SOFTWARE :

                            array = object.getJSONArray(RETURN_LIST);
                            ArrayList<TuLingSoftware> listTuLingSoftware = new ArrayList<TuLingSoftware>();
                            for (int i = 0; i < array.length(); i++)
                            {
                                TuLingSoftware software = new TuLingSoftware();
                                JSONObject jsonObject = array.getJSONObject(i);
                                software.setName(jsonObject
                                        .getString(RETURN_SOFTWARE_NAME));
                                software.setCount(jsonObject
                                        .getString(RETURN_SOFTWARE_COUNT));
                                software.setDetailurl(jsonObject
                                        .getString(RETURN_SOFTWARE_DETAILURL));
                                software.setIcon(jsonObject
                                        .getString(RETURN_SOFTWARE_ICON));
                                listTuLingSoftware.add(software);
                            }
                            data.setListTuLingSoftWare(listTuLingSoftware);
                            break;
                        case RETUEN_TYPE_TRAINS :

                            array = object.getJSONArray(RETURN_LIST);
                            ArrayList<TuLingTrain> listTuLingTrain = new ArrayList<TuLingTrain>();
                            for (int i = 0; i < array.length(); i++)
                            {
                                TuLingTrain train = new TuLingTrain();
                                JSONObject jsonObject = array.getJSONObject(i);
                                train.setTrainnum(jsonObject.getString(RETURN_TRAIN_NUM));
                                train.setStart(jsonObject.getString(RETURN_TRAIN_START));
                                train.setTerminal(jsonObject
                                        .getString(RETURN_TRAIN_TERMINAL));
                                train.setStarttime(jsonObject
                                        .getString(RETURN_TRAIN_STARTTIME));
                                train.setEndtime(jsonObject
                                        .getString(RETURN_TRAIN_ENDTIME));
                                train.setDetailurl(jsonObject
                                        .getString(RETURN_TRAIN_DETAILURL));
                                train.setIcon(jsonObject.getString(RETURN_TRAIN_ICON));
                                listTuLingTrain.add(train);
                            }
                            data.setListTuLingTrain(listTuLingTrain);
                            break;
                        case RETUEN_TYPE_FLIGHT :

                            array = object.getJSONArray(RETURN_LIST);
                            ArrayList<TuLingFlight> listTuLingFlight = new ArrayList<TuLingFlight>();
                            for (int i = 0; i < array.length(); i++)
                            {
                                TuLingFlight flight = new TuLingFlight();
                                JSONObject jsonObject = array.getJSONObject(i);
                                flight.setFlight(jsonObject
                                        .getString(RETURN_FLIGHT_FLIGHT));
                                flight.setRoute(jsonObject.getString(RETURN_FLIGHT_ROUTE));
                                flight.setStarttime(jsonObject
                                        .getString(RETURN_FLIGHT_STARTTIME));
                                flight.setEndtime(jsonObject
                                        .getString(RETURN_FLIGHT_ENDTIME));
                                flight.setState(jsonObject.getString(RETURN_FLIGHT_STATE));
                                flight.setDetailurl(jsonObject
                                        .getString(RETURN_FLIGHT_DETAILURL));
                                flight.setIcon(jsonObject.getString(RETURN_FLIGHT_ICON));
                                listTuLingFlight.add(flight);
                            }
                            data.setListTuLingFlight(listTuLingFlight);
                            break;
                        case RETUEN_TYPE_MOVIE :

                            array = object.getJSONArray(RETURN_LIST);
                            ArrayList<TuLingMovie> listTuLingMovie = new ArrayList<TuLingMovie>();
                            for (int i = 0; i < array.length(); i++)
                            {
                                TuLingMovie movie = new TuLingMovie();
                                JSONObject jsonObject = array.getJSONObject(i);
                                movie.setName(jsonObject.getString(RETURN_MOVIE_NAME));
                                movie.setInfo(jsonObject.getString(RETURN_MOVIE_INFO));
                                movie.setDetailurl(jsonObject
                                        .getString(RETURN_MOVIE_DETAILURL));
                                movie.setIcon(jsonObject.getString(RETURN_MOVIE_ICON));
                                listTuLingMovie.add(movie);
                            }
                            data.setListTuLingMovie(listTuLingMovie);
                            break;
                        case RETUEN_TYPE_HOTEL :

                            array = object.getJSONArray(RETURN_LIST);
                            ArrayList<TuLingHotel> listTuLingHotel = new ArrayList<TuLingHotel>();
                            for (int i = 0; i < array.length(); i++)
                            {
                                TuLingHotel hotel = new TuLingHotel();
                                JSONObject jsonObject = array.getJSONObject(i);
                                hotel.setName(jsonObject.getString(RETURN_HOTEL_NAME));
                                hotel.setPrice(jsonObject.getString(RETURN_HOTEL_PRICE));
                                hotel.setSatisfaction(jsonObject
                                        .getString(RETURN_HOTEL_SATISFACTION));
                                hotel.setCount(jsonObject.getString(RETURN_HOTEL_COUNT));
                                hotel.setDetailurl(jsonObject
                                        .getString(RETURN_HOTEL_DETAILURL));
                                hotel.setIcon(jsonObject.getString(RETURN_HOTEL_ICON));
                                listTuLingHotel.add(hotel);
                            }
                            data.setListTuLingHotel(listTuLingHotel);
                            break;
                        case RETUEN_TYPE_PRICE :

                            array = object.getJSONArray(RETURN_LIST);
                            ArrayList<TuLingPrice> listTuLingPrice = new ArrayList<TuLingPrice>();
                            for (int i = 0; i < array.length(); i++)
                            {
                                TuLingPrice price = new TuLingPrice();
                                JSONObject jsonObject = array.getJSONObject(i);
                                price.setName(jsonObject.getString(RETURN_PRICE_NAME));
                                price.setPrice(jsonObject.getString(RETURN_PRICE_PRICE));
                                price.setDetailurl(jsonObject
                                        .getString(RETURN_PRICE_DETAILURL));
                                price.setIcon(jsonObject.getString(RETURN_PRICE_ICON));
                                listTuLingPrice.add(price);
                            }
                            data.setListTuLingPrice(listTuLingPrice);
                            break;
                        case RETUEN_TYPE_KEY_LENGTH_ERR :

                            break;
                        case RETUEN_TYPE_CONTENT_EMPTY_ERR :

                            break;
                        case RETUEN_TYPE_KEY_ERR :

                            break;
                        case RETUEN_TYPE_OUT_OF_QUOTA_ERR :

                            break;
                        case RETUEN_TYPE_NOT_SUPPORT_ERR :

                            break;
                        case RETUEN_TYPE_SERVER_UPDATING_ERR :

                            break;
                        case RETUEN_TYPE_SERVER_DATA_ERR :

                            break;

                        default :
                            break;
                    }
                    return data;
                }

            }
            catch (MalformedURLException e2)
            {
                // TODO Auto-generated catch block
                e2.printStackTrace();
            }
            catch (JSONException e1)
            {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            catch (IOException e1)
            {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            return null;
        }

    }

    private class DownLoadImageAsyncTask extends AsyncTask<String, Integer, Bitmap>
    {
        private ImageView imageView;
        private String imageUrl;

        public DownLoadImageAsyncTask(String imageUrl, ImageView imageView)
        {
            super();
            this.imageView = imageView;
            this.imageUrl = imageUrl;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap)
        {
            // TODO Auto-generated method stub
            imageCache.put(imageUrl, new SoftReference<Bitmap>(bitmap));
            File bitmapFile = new File(getCacheDir(), imageUrl.substring(imageUrl
                    .lastIndexOf("/") + 1));
            if (!bitmapFile.exists())
            {
                try
                {
                    bitmapFile.createNewFile();
                }
                catch (IOException e)
                {
                    //TODOAuto-generatedcatchblock 
                    e.printStackTrace();
                }
            }
            FileOutputStream fos;
            try
            {
                fos = new FileOutputStream(bitmapFile);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.close();
            }
            catch (FileNotFoundException e)
            {
                //TODOAuto-generatedcatchblock 
                e.printStackTrace();
            }
            catch (IOException e)
            {
                //TODOAuto-generatedcatchblock 
                e.printStackTrace();
            }
            showImage(bitmap, imageView);
        }

        @Override
        protected Bitmap doInBackground(String... params)
        {
            // TODO Auto-generated method stub
            URL url;
            try
            {
                url = new URL(params[0]);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                InputStream inputStream = conn.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                // 断开连接 
                conn.disconnect();

                return bitmap;
            }
            catch (MalformedURLException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

    }

    public void getMessageReturn(String strWords)
    {
        try
        {
            new MyAsyncTask().execute(tulingQueryUrl
                + URLEncoder.encode(strWords, "utf-8"));
        }
        catch (UnsupportedEncodingException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void showImage(final Bitmap bitmap, final ImageView imageView)
    {
        // TODO Auto-generated method stub
        if (null != bitmap && null != imageView)
        {
            new Handler().post(new Runnable()
            {

                @Override
                public void run()
                {
                    // TODO Auto-generated method stub
                    imageView.setImageBitmap(bitmap);
                }
            });
        }
    }

    public void setIcon(String imageURL, ImageView imageView)
    {
        //在内存缓存中，则返回Bitmap对象 
        if (imageCache.containsKey(imageURL))
        {
            SoftReference<Bitmap> reference = imageCache.get(imageURL);
            Bitmap bitmap = reference.get();
            if (bitmap != null)
            {
                showImage(bitmap, imageView);
                return;
            }
        }
        else
        {
            /** 
            *加上一个对本地缓存的查找 
            */
            String bitmapName = imageURL.substring(imageURL.lastIndexOf("/") + 1);
            File[] cacheFiles = getCacheDir().listFiles();
            int i = 0;
            if (null != cacheFiles)
            {
                for (; i < cacheFiles.length; i++)
                {
                    if (bitmapName.equals(cacheFiles[i].getName()))
                    {
                        break;
                    }
                }
                if (i < cacheFiles.length)
                {
                    showImage(BitmapFactory.decodeFile(getCacheDir().getPath() +"/"+ bitmapName),
                        imageView);
                    return;
                }
            }
        }

        new DownLoadImageAsyncTask(imageURL, imageView).execute(imageURL);
    }

    private File getCacheDir()
    {

        if (cacheDir == null)
        {
            cacheDir = new File(context.getCacheDir(), "tuling");
            cacheDir.mkdirs();
        }

        return cacheDir;

    }

    public interface ITuLing
    {
        public void TuLingCallback(TuLingData data);
    }
}