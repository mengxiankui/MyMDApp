package com.mxk.utils.tuling;

import java.util.ArrayList;

public class TuLingData
{
    private int iType;
    private String strText, strUrl;
    private ArrayList<TuLingNews> listTuLingNews;
    private ArrayList<TuLingSoftware> listTuLingSoftWare;
    private ArrayList<TuLingTrain> listTuLingTrain;
    private ArrayList<TuLingFlight> listTuLingFlight;
    private ArrayList<TuLingMovie> listTuLingMovie;
    private ArrayList<TuLingHotel> listTuLingHotel;
    private ArrayList<TuLingPrice> listTuLingPrice;
    
    

    public int getType()
    {
        return iType;
    }

    public void setType(int iType)
    {
        this.iType = iType;
    }

    public ArrayList<TuLingNews> getListTuLingNews()
    {
        return listTuLingNews;
    }

    public void setListTuLingNews(ArrayList<TuLingNews> listTuLingNews)
    {
        this.listTuLingNews = listTuLingNews;
    }

    public ArrayList<TuLingSoftware> getListTuLingSoftWare()
    {
        return listTuLingSoftWare;
    }

    public void setListTuLingSoftWare(ArrayList<TuLingSoftware> listTuLingSoftWare)
    {
        this.listTuLingSoftWare = listTuLingSoftWare;
    }

    public ArrayList<TuLingTrain> getListTuLingTrain()
    {
        return listTuLingTrain;
    }

    public void setListTuLingTrain(ArrayList<TuLingTrain> listTuLingTrain)
    {
        this.listTuLingTrain = listTuLingTrain;
    }

    public ArrayList<TuLingFlight> getListTuLingFlight()
    {
        return listTuLingFlight;
    }

    public void setListTuLingFlight(ArrayList<TuLingFlight> listTuLingFlight)
    {
        this.listTuLingFlight = listTuLingFlight;
    }

    public ArrayList<TuLingMovie> getListTuLingMovie()
    {
        return listTuLingMovie;
    }

    public void setListTuLingMovie(ArrayList<TuLingMovie> listTuLingMovie)
    {
        this.listTuLingMovie = listTuLingMovie;
    }

    public ArrayList<TuLingHotel> getListTuLingHotel()
    {
        return listTuLingHotel;
    }

    public void setListTuLingHotel(ArrayList<TuLingHotel> listTuLingHotel)
    {
        this.listTuLingHotel = listTuLingHotel;
    }

    public ArrayList<TuLingPrice> getListTuLingPrice()
    {
        return listTuLingPrice;
    }

    public void setListTuLingPrice(ArrayList<TuLingPrice> listTuLingPrice)
    {
        this.listTuLingPrice = listTuLingPrice;
    }

    public String getText()
    {
        return strText;
    }

    public void setText(String strText)
    {
        this.strText = strText;
    }

    public String getUrl()
    {
        return strUrl;
    }

    public void setUrl(String strUrl)
    {
        this.strUrl = strUrl;
    }
}