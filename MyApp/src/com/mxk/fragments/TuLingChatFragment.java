package com.mxk.fragments;


import java.util.ArrayList;

import com.mxk.myapp.MyBaseFragment;
import com.mxk.myapp.R;
import com.mxk.utils.APIKeys;
import com.mxk.utils.tuling.TuLingData;
import com.mxk.utils.tuling.TuLingFlight;
import com.mxk.utils.tuling.TuLingHotel;
import com.mxk.utils.tuling.TuLingMovie;
import com.mxk.utils.tuling.TuLingNews;
import com.mxk.utils.tuling.TuLingPrice;
import com.mxk.utils.tuling.TuLingSoftware;
import com.mxk.utils.tuling.TuLingTrain;
import com.mxk.utils.tuling.TuLingUtil;
import com.mxk.utils.tuling.TuLingUtil.ITuLing;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


public class TuLingChatFragment extends MyBaseFragment implements ITuLing
{
    public static final String LOG_TAG = "TuLingRobotFragment";
    private ScrollView scrollView;
    private LinearLayout layout;
    private EditText editText;
    private Button button;

    private boolean bIsHanZi;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        bIsHanZi = getArguments().getBoolean("IsHanZi", true);
        if (bIsHanZi)
        {
            TuLingUtil.setInstance(APIKeys.TULING_KEY_HANZI, getActivity());
        }
        else
        {
            TuLingUtil.setInstance(APIKeys.TULING_KEY_MEIZI, getActivity());
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        View rootView = inflater
                .inflate(R.layout.tuling_robot_fragment, container, false);
        scrollView = (ScrollView) rootView.findViewById(R.id.chat_scroll);
        layout = (LinearLayout) rootView.findViewById(R.id.chat_llayout);
        editText = (EditText) rootView.findViewById(R.id.input_edit);
        button = (Button) rootView.findViewById(R.id.send);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        if (bIsHanZi)
        {
            addChat(getRobotChat(getResources().getString(R.string.robot_welcome_hanzi)));
        }
        else
        {
            addChat(getRobotChat(getResources().getString(R.string.robot_welcome_meizi)));
        }

        TuLingUtil.getInstance().setCallback(this);
        setListener();
    }

    private void setListener()
    {
        // TODO Auto-generated method stub
        button.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                sendMyChat();
            }
        });

        //        editText.setOnEditorActionListener(new OnEditorActionListener()
        //        {
        //
        //            @Override
        //            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
        //            {
        //                // TODO Auto-generated method stub
        //                if (actionId == EditorInfo.IME_ACTION_SEND)
        //                {
        //                    sendMyChat();
        //                    return true;
        //                }
        //                return false;
        //            }
        //        });
        editText.addTextChangedListener(new TextWatcher()
        {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                // TODO Auto-generated method stub
                if (!TextUtils.isEmpty(s))
                {
                    button.setEnabled(true);
                }
                else
                {
                    button.setEnabled(false);
                }
            }
        });
    }

    private void addChat(View view)
    {
        layout.addView(view);
        new Handler().post(new Runnable()
        {
            public void run()
            {
                int offset = layout.getMeasuredHeight() - scrollView.getHeight();
                if (offset < 0)
                {
                    offset = 0;
                }
                scrollView.scrollTo(0, offset);
            }
        });
    }

    private View getRobotChat(String strWords)
    {
        View view = LayoutInflater.from(getActivity()).inflate(
            R.layout.chat_left_fragment, null);
        if (!bIsHanZi)
        {
            ((ImageView) view.findViewById(R.id.touxiang))
                    .setImageResource(R.drawable.me);
        }
        //        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        ((TextView) view.findViewById(R.id.txt_content)).setText(strWords);
        ((LinearLayout) view.findViewById(R.id.chat_llayout))
                .setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT));
        return view;

    }

    private View getRobotChatNews(String content, ArrayList<TuLingNews> listTuLingNews)
    {
        View view = LayoutInflater.from(getActivity()).inflate(
            R.layout.chat_left_fragment, null);
        if (!bIsHanZi)
        {
            ((ImageView) view.findViewById(R.id.touxiang))
                    .setImageResource(R.drawable.me);
        }
        //        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        ((TextView) view.findViewById(R.id.txt_content)).setText(content);
        for (int i = 0; i < listTuLingNews.size(); i++)
        {
            TuLingNews news = listTuLingNews.get(i);
            View child = LayoutInflater.from(getActivity()).inflate(
                R.layout.tuling_news_item_fragment, null);
            ((TextView) child.findViewById(R.id.txt_title)).setText(news.getArticle());
            ((TextView) child.findViewById(R.id.txt_source)).setText(news.getSource());
            ((TextView) child.findViewById(R.id.txt_detailurl)).setText(news
                    .getDetailurl());
            TuLingUtil.getInstance().setIcon(news.getIcon(),
                (ImageView) child.findViewById(R.id.icon));
            ((LinearLayout) view.findViewById(R.id.chat_llayout)).addView(child);
        }
        return view;

    }

    private View getRobotChatSoftware(String content,
            ArrayList<TuLingSoftware> listTuLingSoftWare)
    {
        View view = LayoutInflater.from(getActivity()).inflate(
            R.layout.chat_left_fragment, null);
        if (!bIsHanZi)
        {
            ((ImageView) view.findViewById(R.id.touxiang))
                    .setImageResource(R.drawable.me);
        }
        //        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        ((TextView) view.findViewById(R.id.txt_content)).setText(content);
        for (int i = 0; i < listTuLingSoftWare.size(); i++)
        {
            TuLingSoftware software = listTuLingSoftWare.get(i);
            View child = LayoutInflater.from(getActivity()).inflate(
                R.layout.tuling_software_item_fragment, null);
            ((TextView) child.findViewById(R.id.txt_name)).setText(software.getName());
            ((TextView) child.findViewById(R.id.txt_count)).setText(software.getCount());
            ((TextView) child.findViewById(R.id.txt_detailurl)).setText(software
                    .getDetailurl());
            TuLingUtil.getInstance().setIcon(software.getIcon(),
                (ImageView) child.findViewById(R.id.icon));
            ((LinearLayout) view.findViewById(R.id.chat_llayout)).addView(child);
        }
        return view;

    }

    private View getRobotChatTrain(String content, ArrayList<TuLingTrain> listTuLingTrain)
    {
        View view = LayoutInflater.from(getActivity()).inflate(
            R.layout.chat_left_fragment, null);
        if (!bIsHanZi)
        {
            ((ImageView) view.findViewById(R.id.touxiang))
                    .setImageResource(R.drawable.me);
        }
        //        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        ((TextView) view.findViewById(R.id.txt_content)).setText(content);
        for (int i = 0; i < listTuLingTrain.size(); i++)
        {
            TuLingTrain train = listTuLingTrain.get(i);
            View child = LayoutInflater.from(getActivity()).inflate(
                R.layout.tuling_train_item_fragment, null);
            ((TextView) child.findViewById(R.id.txt_trainnum)).setText(train
                    .getTrainnum());
            ((TextView) child.findViewById(R.id.txt_start)).setText(train.getStart());
            ((TextView) child.findViewById(R.id.txt_terminal)).setText(train
                    .getTerminal());
            ((TextView) child.findViewById(R.id.txt_starttime)).setText(train
                    .getStarttime());
            ((TextView) child.findViewById(R.id.txt_endtime)).setText(train.getEndtime());
            ((TextView) child.findViewById(R.id.txt_detailurl)).setText(train
                    .getDetailurl());
            TuLingUtil.getInstance().setIcon(train.getIcon(),
                (ImageView) child.findViewById(R.id.icon));
            ((LinearLayout) view.findViewById(R.id.chat_llayout)).addView(child);
        }
        return view;

    }

    private View getRobotChatFlight(String content,
            ArrayList<TuLingFlight> listTuLingFlight)
    {
        View view = LayoutInflater.from(getActivity()).inflate(
            R.layout.chat_left_fragment, null);
        if (!bIsHanZi)
        {
            ((ImageView) view.findViewById(R.id.touxiang))
                    .setImageResource(R.drawable.me);
        }
        //        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        ((TextView) view.findViewById(R.id.txt_content)).setText(content);
        for (int i = 0; i < listTuLingFlight.size(); i++)
        {
            TuLingFlight flight = listTuLingFlight.get(i);
            View child = LayoutInflater.from(getActivity()).inflate(
                R.layout.tuling_plane_item_fragment, null);
            ((TextView) child.findViewById(R.id.txt_flight)).setText(flight.getFlight());
            ((TextView) child.findViewById(R.id.txt_route)).setText(flight.getRoute());
            ((TextView) child.findViewById(R.id.txt_starttime)).setText(flight
                    .getStarttime());
            ((TextView) child.findViewById(R.id.txt_endtime))
                    .setText(flight.getEndtime());
            ((TextView) child.findViewById(R.id.txt_state)).setText(flight.getState());
            ((TextView) child.findViewById(R.id.txt_detailurl)).setText(flight
                    .getDetailurl());
            TuLingUtil.getInstance().setIcon(flight.getIcon(),
                (ImageView) child.findViewById(R.id.icon));
            ((LinearLayout) view.findViewById(R.id.chat_llayout)).addView(child);
        }
        return view;

    }

    private View getRobotChatMovie(String content, ArrayList<TuLingMovie> listTuLingMovie)
    {
        View view = LayoutInflater.from(getActivity()).inflate(
            R.layout.chat_left_fragment, null);
        if (!bIsHanZi)
        {
            ((ImageView) view.findViewById(R.id.touxiang))
                    .setImageResource(R.drawable.me);
        }
        //        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        ((TextView) view.findViewById(R.id.txt_content)).setText(content);
        for (int i = 0; i < listTuLingMovie.size(); i++)
        {
            TuLingMovie movie = listTuLingMovie.get(i);
            View child = LayoutInflater.from(getActivity()).inflate(
                R.layout.tuling_movie_item_fragment, null);
            ((TextView) child.findViewById(R.id.txt_name)).setText(movie.getName());
            ((TextView) child.findViewById(R.id.txt_info)).setText(movie.getInfo());
            ((TextView) child.findViewById(R.id.txt_detailurl)).setText(movie
                    .getDetailurl());
            TuLingUtil.getInstance().setIcon(movie.getIcon(),
                (ImageView) child.findViewById(R.id.icon));
            ((LinearLayout) view.findViewById(R.id.chat_llayout)).addView(child);
        }
        return view;

    }

    private View getRobotChatHotel(String content, ArrayList<TuLingHotel> listTuLingHotel)
    {
        View view = LayoutInflater.from(getActivity()).inflate(
            R.layout.chat_left_fragment, null);
        if (!bIsHanZi)
        {
            ((ImageView) view.findViewById(R.id.touxiang))
                    .setImageResource(R.drawable.me);
        }
        //        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        ((TextView) view.findViewById(R.id.txt_content)).setText(content);
        for (int i = 0; i < listTuLingHotel.size(); i++)
        {
            TuLingHotel hotel = listTuLingHotel.get(i);
            View child = LayoutInflater.from(getActivity()).inflate(
                R.layout.tuling_hotel_item_fragment, null);
            ((TextView) child.findViewById(R.id.txt_name)).setText(hotel.getName());
            ((TextView) child.findViewById(R.id.txt_price)).setText(hotel.getPrice());
            ((TextView) child.findViewById(R.id.txt_satisfaction)).setText(hotel
                    .getSatisfaction());
            ((TextView) child.findViewById(R.id.txt_count)).setText(hotel.getCount());
            ((TextView) child.findViewById(R.id.txt_detailurl)).setText(hotel
                    .getDetailurl());
            TuLingUtil.getInstance().setIcon(hotel.getIcon(),
                (ImageView) child.findViewById(R.id.icon));
            ((LinearLayout) view.findViewById(R.id.chat_llayout)).addView(child);
        }
        return view;

    }

    private View getRobotChatPrice(String content, ArrayList<TuLingPrice> listTuLingPrice)
    {
        View view = LayoutInflater.from(getActivity()).inflate(
            R.layout.chat_left_fragment, null);
        if (!bIsHanZi)
        {
            ((ImageView) view.findViewById(R.id.touxiang))
                    .setImageResource(R.drawable.me);
        }
        //        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        ((TextView) view.findViewById(R.id.txt_content)).setText(content);
        for (int i = 0; i < listTuLingPrice.size(); i++)
        {
            TuLingPrice price = listTuLingPrice.get(i);
            View child = LayoutInflater.from(getActivity()).inflate(
                R.layout.tuling_price_item_fragment, null);
            ((TextView) child.findViewById(R.id.txt_name)).setText(price.getName());
            ((TextView) child.findViewById(R.id.txt_price)).setText(price.getPrice());
            ((TextView) child.findViewById(R.id.txt_detailurl)).setText(price
                    .getDetailurl());
            TuLingUtil.getInstance().setIcon(price.getIcon(),
                (ImageView) child.findViewById(R.id.icon));
            ((LinearLayout) view.findViewById(R.id.chat_llayout)).addView(child);
        }
        return view;

    }

    private View getMyChat(String strWords)
    {
        View view = LayoutInflater.from(getActivity()).inflate(
            R.layout.chat_right_fragment, null);
        if (!bIsHanZi)
        {
            ((ImageView) view.findViewById(R.id.touxiang))
                    .setImageResource(R.drawable.dabai);
        }
        //        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        ((TextView) view.findViewById(R.id.txt_content)).setText(strWords);
        return view;

    }

    private void sendMyChat()
    {
        sendMyChat(editText.getText().toString());
        addChat(getMyChat(editText.getText().toString()));
        editText.setText("");
    }

    private void sendMyChat(String strWords)
    {
        TuLingUtil.getInstance().getMessageReturn(strWords);
        //        if (!TextUtils.isEmpty(returnWords))
        //        {
        //            addChat(getMyChat(returnWords));
        //        }
    }

    @Override
    public void TuLingCallback(TuLingData data)
    {
        // TODO Auto-generated method stub
        if (!isAdded())
        {
            return;
        }
        switch (data.getType())
        {

            case TuLingUtil.RETUEN_TYPE_NEWS :
                addChat(getRobotChatNews(data.getText(), data.getListTuLingNews()));
                break;
            case TuLingUtil.RETUEN_TYPE_SOFTWARE :
                addChat(getRobotChatSoftware(data.getText(), data.getListTuLingSoftWare()));
                break;
            case TuLingUtil.RETUEN_TYPE_TRAINS :
                addChat(getRobotChatTrain(data.getText(), data.getListTuLingTrain()));
                break;
            case TuLingUtil.RETUEN_TYPE_FLIGHT :
                addChat(getRobotChatFlight(data.getText(), data.getListTuLingFlight()));
                break;
            case TuLingUtil.RETUEN_TYPE_MOVIE :
                addChat(getRobotChatMovie(data.getText(), data.getListTuLingMovie()));
                break;
            case TuLingUtil.RETUEN_TYPE_HOTEL :
                addChat(getRobotChatHotel(data.getText(), data.getListTuLingHotel()));
                break;
            case TuLingUtil.RETUEN_TYPE_PRICE :
                addChat(getRobotChatPrice(data.getText(), data.getListTuLingPrice()));
                break;
            case TuLingUtil.RETUEN_TYPE_URL :
                try
                {
                    getActivity().startActivity(
                        new Intent(Intent.ACTION_VIEW, Uri.parse(data.getUrl())));
                }
                catch (Exception e)
                {
                    Toast.makeText(getActivity(), "Invalid Url", Toast.LENGTH_LONG)
                            .show();
                }
            case TuLingUtil.RETUEN_TYPE_TEXT :
            default :
                addChat(getRobotChat(data.getText()));
                break;
        }

    }

}