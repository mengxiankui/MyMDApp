package com.mxk.ui.floatbutton;


import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mxk.myapp.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;


/**
 * Provides a "Path" like menu for android. ??
 * 
 * TODO: tell about usage
 * 
 * @author Siyamed SINIR
 * 
 */
public class SatelliteMenu extends FrameLayout
{

    private static final int DEFAULT_SATELLITE_DISTANCE = 200;
    private static final float DEFAULT_TOTAL_SPACING_DEGREES = 90f;
    private static final boolean DEFAULT_CLOSE_ON_CLICK = true;
    private static final int DEFAULT_EXPAND_DURATION = 400;
    private static final int DEFAULT_EXPAND_LAYOUT_EXTRA_WIDTH = 150;
    private static final int DEFAULT_EXPAND_BUTTON_OFFSET = 10;
    private static final int DEFAULT_BUTTON_OFFSET = 40;

    private PointF pDown = new PointF();
    private boolean isTouchBtn = false;
    private boolean isMove = false;
    LayoutParams lpImgMain, lpFlExpandButtons;
    private int touchSlop;

    private long lastTouchTime = 0;

    private ExpandState currentExpandState = ExpandState.STATE_LEFT_BOTTOM;

    public enum ExpandState
    {
        STATE_LEFT_TOP, STATE_RIGHT_TOP, STATE_LEFT_BOTTOM, STATE_RIGHT_BOTTOM;
    }

    //	private Animation mainAnimClose;
    //    private Animation mainAnimOpen;

    private FrameLayout flExpandButtons;
    private ImageView imgMain;

    private TransitionDrawable drawable;

    private SateliteClickedListener itemClickedListener;
    private SateliteLongPressedListener longPressedListener;
    private InternalSatelliteOnClickListener internalItemClickListener;

    private List<SatelliteMenuItem> menuItems = new ArrayList<SatelliteMenuItem>();
    private Map<View, SatelliteMenuItem> viewToItemMap = new HashMap<View, SatelliteMenuItem>();

    //    private AtomicBoolean plusAnimationActive = new AtomicBoolean(false);

    // ?? how to save/restore?
    private IDegreeProvider gapDegreesProvider = new DefaultDegreeProvider();

    //States of these variables are saved
    private boolean opened = false;
    private int measureDiff = 0;
    //States of these variables are saved - Also configured from XML 
    private float totalSpacingDegree = DEFAULT_TOTAL_SPACING_DEGREES;
    private int satelliteDistance = DEFAULT_SATELLITE_DISTANCE;
    private int expandDuration = DEFAULT_EXPAND_DURATION;
    private boolean closeItemsOnClick = DEFAULT_CLOSE_ON_CLICK;
    private int expandButtonOffset = DEFAULT_EXPAND_BUTTON_OFFSET;
    private int buttonOffset = DEFAULT_BUTTON_OFFSET;

    public SatelliteMenu(Context context)
    {
        super(context);
        init(context, null, 0);
    }

    public SatelliteMenu(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public SatelliteMenu(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle)
    {
        LayoutInflater.from(context).inflate(R.layout.sat_main, this, true);
        imgMain = (ImageView) findViewById(R.id.sat_main);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        if (attrs != null)
        {
            TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.SatelliteMenu, defStyle, 0);
            satelliteDistance = (int) typedArray.getInt(
                R.styleable.SatelliteMenu_satelliteDistance, DEFAULT_SATELLITE_DISTANCE);
            totalSpacingDegree = typedArray.getFloat(
                R.styleable.SatelliteMenu_totalSpacingDegree,
                DEFAULT_TOTAL_SPACING_DEGREES);
            closeItemsOnClick = typedArray.getBoolean(
                R.styleable.SatelliteMenu_closeOnClick, DEFAULT_CLOSE_ON_CLICK);
            expandDuration = typedArray.getInt(R.styleable.SatelliteMenu_expandDuration,
                DEFAULT_EXPAND_DURATION);
            expandButtonOffset = (int) typedArray.getInt(
                R.styleable.SatelliteMenu_expandButtonOffset,
                DEFAULT_EXPAND_BUTTON_OFFSET);
            buttonOffset = (int) typedArray.getInt(
                R.styleable.SatelliteMenu_buttonOffset, DEFAULT_BUTTON_OFFSET);
            //float satelliteDistance = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 170, getResources().getDisplayMetrics());
            typedArray.recycle();
        }
//        satelliteDistance = (int) (satelliteDistance / DisplayManager.getScreenDensity());
//        satelliteDistance = DisplayManager.getRealHeight(satelliteDistance) < DisplayManager
//                .getRealWidth(satelliteDistance) ? DisplayManager
//                .getRealHeight(satelliteDistance) : DisplayManager
//                .getRealWidth(satelliteDistance);
////        expandButtonOffset = (int) (expandButtonOffset / DisplayManager
////                .getScreenDensity());
//        expandButtonOffset = DisplayManager.getRealHeight(expandButtonOffset) < DisplayManager
//                .getRealWidth(expandButtonOffset) ? DisplayManager
//                .getRealHeight(expandButtonOffset) : DisplayManager
//                .getRealWidth(expandButtonOffset);
////        buttonOffset = (int) (buttonOffset / DisplayManager.getScreenDensity());
//        buttonOffset = DisplayManager.getRealHeight(buttonOffset) < DisplayManager
//                .getRealWidth(buttonOffset)
//            ? DisplayManager.getRealHeight(buttonOffset)
//            : DisplayManager.getRealWidth(buttonOffset);
        //        satelliteDistance = (int) (satelliteDistance / DisplayManager.getScreenDensity());
        //        expandButtonOffset = (int) (expandButtonOffset / DisplayManager
        //                .getScreenDensity());
        //        buttonOffset = (int) (buttonOffset / DisplayManager.getScreenDensity());

        imgMain.setImageResource(SatelliteAnimationCreator
                .getMainButtonAnimationDrawable());
        drawable = (TransitionDrawable) imgMain.getDrawable();
        lpImgMain = (LayoutParams) imgMain.getLayoutParams();

        flExpandButtons = new FrameLayout(context);
        flExpandButtons.setLayoutParams(new FrameLayout.LayoutParams(
            LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        lpFlExpandButtons = (LayoutParams) flExpandButtons.getLayoutParams();

        getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener()
        {

            @Override
            public void onGlobalLayout()
            {
                // TODO Auto-generated method stub
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
                lpImgMain.leftMargin = buttonOffset;
                lpImgMain.topMargin = getHeight() - imgMain.getHeight() - getPaddingTop()
                    - getPaddingBottom() - buttonOffset;
                imgMain.setLayoutParams(lpImgMain);
                imgMain.setVisibility(View.VISIBLE);
                lpFlExpandButtons.leftMargin = 0;
                lpFlExpandButtons.topMargin = getHeight() - flExpandButtons.getHeight()
                    - getPaddingTop() - getPaddingBottom() - buttonOffset;
                lpFlExpandButtons.width = flExpandButtons.getWidth()
                    + DEFAULT_EXPAND_LAYOUT_EXTRA_WIDTH;
                lpFlExpandButtons.height = flExpandButtons.getHeight()
                    + DEFAULT_EXPAND_LAYOUT_EXTRA_WIDTH;
                flExpandButtons.setLayoutParams(lpFlExpandButtons);
            }
        });

        //		mainAnimClose = SatelliteAnimationCreator.createMainButtonInverseAnimation(context);

        //        Animation.AnimationListener plusAnimationListener = new Animation.AnimationListener()
        //        {
        //            @Override
        //            public void onAnimationStart(Animation animation)
        //            {
        //            }
        //
        //            @Override
        //            public void onAnimationRepeat(Animation animation)
        //            {
        //            }
        //
        //            @Override
        //            public void onAnimationEnd(Animation animation)
        //            {
        //                plusAnimationActive.set(false);
        //            }
        //        };

        //        mainAnimOpen.setAnimationListener(plusAnimationListener);
        //		mainAnimClose.setAnimationListener(plusAnimationListener);

        //        imgMain.setOnClickListener(new View.OnClickListener()
        //        {
        //            @Override
        //            public void onClick(View v)
        //            {
        //                SatelliteMenu.this.onClick();
        //            }
        //        });

        internalItemClickListener = new InternalSatelliteOnClickListener(this);
    }

    private void startPressAnim()
    {
        drawable.startTransition(500);
    }

    private void startReverseAnim()
    {
        drawable.reverseTransition(500);
    }

    private void onClick()
    {
        //        plusAnimationActive.compareAndSet(false, true)
        if (true)
        {
            if (!opened)
            {

                for (SatelliteMenuItem item : menuItems)
                {
                    item.getView().startAnimation(item.getOutAnimation());
                }
                startPressAnim();
            }
            else
            {

                for (SatelliteMenuItem item : menuItems)
                {
                    item.getView().startAnimation(item.getInAnimation());
                }
                startReverseAnim();
            }
            opened = !opened;
        }
    }

    private void onLongClick()
    {
        //        plusAnimationActive.compareAndSet(false, true)

    }

    private void openItems()
    {
        if (true)
        {
            if (!opened)
            {

                for (SatelliteMenuItem item : menuItems)
                {
                    item.getView().startAnimation(item.getOutAnimation());
                }
                startPressAnim();
                opened = !opened;
            }

        }
    }

    private void closeItems()
    {
        if (true)
        {
            if (opened)
            {

                for (SatelliteMenuItem item : menuItems)
                {
                    item.getView().startAnimation(item.getInAnimation());
                }
                startReverseAnim();
                opened = !opened;
            }

        }
    }

    public void addItems(List<SatelliteMenuItem> items)
    {

        menuItems.addAll(items);
        this.removeView(imgMain);

        float[] degrees = getDegrees(menuItems.size());
        int index = 0;
        for (SatelliteMenuItem menuItem : menuItems)
        {
            int finalX = SatelliteAnimationCreator.getTranslateX(degrees[index],
                satelliteDistance);
            int finalY = SatelliteAnimationCreator.getTranslateY(degrees[index],
                satelliteDistance);

            ImageView itemView = (ImageView) LayoutInflater.from(getContext()).inflate(
                R.layout.sat_item_cr, this, false);
            ImageView cloneView = (ImageView) LayoutInflater.from(getContext()).inflate(
                R.layout.sat_item_cr, this, false);
            itemView.setTag(menuItem.getId());
            cloneView.setVisibility(View.INVISIBLE);
            itemView.setVisibility(View.INVISIBLE);
//            DisplayManager.scaleView(itemView);
//            DisplayManager.scaleView(cloneView);

            cloneView.setOnClickListener(internalItemClickListener);
            cloneView.setTag(Integer.valueOf(menuItem.getId()));
            FrameLayout.LayoutParams layoutParamsItem = getLayoutParams(itemView);
            FrameLayout.LayoutParams layoutParams = getLayoutParams(cloneView);
            switch (currentExpandState.ordinal())
            {
                case 0 :
                    layoutParamsItem.leftMargin = expandButtonOffset + buttonOffset;
                    layoutParamsItem.topMargin = expandButtonOffset + buttonOffset;
                    layoutParams.leftMargin = Math.abs(finalX) + expandButtonOffset
                        + buttonOffset;
                    layoutParams.topMargin = Math.abs(finalY) + expandButtonOffset
                        + buttonOffset;
                    break;
                case 1 :
                    layoutParamsItem.leftMargin = satelliteDistance
                        + DEFAULT_EXPAND_LAYOUT_EXTRA_WIDTH;
                    layoutParamsItem.topMargin = expandButtonOffset + buttonOffset;
                    layoutParams.leftMargin = satelliteDistance - Math.abs(finalX)
                        + DEFAULT_EXPAND_LAYOUT_EXTRA_WIDTH;
                    layoutParams.topMargin = Math.abs(finalY) + expandButtonOffset
                        + buttonOffset;
                    break;
                case 2 :
                    layoutParamsItem.leftMargin = expandButtonOffset + buttonOffset;
                    layoutParamsItem.topMargin = satelliteDistance
                        + DEFAULT_EXPAND_LAYOUT_EXTRA_WIDTH;
                    layoutParamsItem.bottomMargin = expandButtonOffset;
                    layoutParams.leftMargin = Math.abs(finalX) + expandButtonOffset
                        + buttonOffset;
                    layoutParams.topMargin = satelliteDistance - Math.abs(finalY)
                        + DEFAULT_EXPAND_LAYOUT_EXTRA_WIDTH;
                    layoutParams.bottomMargin = expandButtonOffset;
                    break;
                case 3 :
                    layoutParamsItem.leftMargin = satelliteDistance
                        + DEFAULT_EXPAND_LAYOUT_EXTRA_WIDTH;
                    layoutParamsItem.rightMargin = expandButtonOffset;
                    layoutParamsItem.topMargin = satelliteDistance
                        + DEFAULT_EXPAND_LAYOUT_EXTRA_WIDTH;
                    layoutParamsItem.bottomMargin = expandButtonOffset;
                    layoutParams.leftMargin = satelliteDistance - Math.abs(finalX)
                        + DEFAULT_EXPAND_LAYOUT_EXTRA_WIDTH;
                    layoutParams.rightMargin = expandButtonOffset;
                    layoutParams.topMargin = satelliteDistance - Math.abs(finalY)
                        + DEFAULT_EXPAND_LAYOUT_EXTRA_WIDTH;
                    layoutParams.bottomMargin = expandButtonOffset;
                    break;

                default :
                    break;
            }
            itemView.setLayoutParams(layoutParamsItem);
            cloneView.setLayoutParams(layoutParams);

            if (menuItem.getImgResourceId() > 0)
            {
                itemView.setImageResource(menuItem.getImgResourceId());
                cloneView.setImageResource(menuItem.getImgResourceId());
            }
            else if (menuItem.getImgDrawable() != null)
            {
                itemView.setImageDrawable(menuItem.getImgDrawable());
                cloneView.setImageDrawable(menuItem.getImgDrawable());
            }

            Animation itemOut = SatelliteAnimationCreator.createItemOutAnimation(
                getContext(), index, expandDuration, finalX, finalY);
            Animation itemIn = SatelliteAnimationCreator.createItemInAnimation(
                getContext(), index, expandDuration, finalX, finalY);
            Animation itemClick = SatelliteAnimationCreator
                    .createItemClickAnimation(getContext());

            menuItem.setView(itemView);
            menuItem.setCloneView(cloneView);
            menuItem.setInAnimation(itemIn);
            menuItem.setOutAnimation(itemOut);
            menuItem.setClickAnimation(itemClick);
            menuItem.setFinalX(finalX);
            menuItem.setFinalY(finalY);

            itemIn.setAnimationListener(new SatelliteAnimationListener(itemView, true,
                viewToItemMap));
            itemOut.setAnimationListener(new SatelliteAnimationListener(itemView, false,
                viewToItemMap));
            itemClick.setAnimationListener(new SatelliteItemClickAnimationListener(this,
                menuItem.getId()));

            flExpandButtons.addView(itemView);
            flExpandButtons.addView(cloneView);
            viewToItemMap.put(itemView, menuItem);
            viewToItemMap.put(cloneView, menuItem);
            index++;
        }

        this.addView(flExpandButtons);
        this.addView(imgMain);
    }

    private float[] getDegrees(int count)
    {
        return gapDegreesProvider.getDegrees(count, totalSpacingDegree,
            currentExpandState);
    }

    //    private void recalculateMeasureDiff()
    //    {
    //        int itemWidth = 0;
    //        if (menuItems.size() > 0)
    //        {
    //            itemWidth = menuItems.get(0).getView().getWidth();
    //        }
    //        measureDiff = Float.valueOf(satelliteDistance * 0.2f).intValue() + itemWidth;
    //    }

    private void resetItems()
    {
        if (menuItems.size() > 0)
        {
            List<SatelliteMenuItem> items = new ArrayList<SatelliteMenuItem>(menuItems);
            menuItems.clear();
            this.removeAllViews();
            flExpandButtons.removeAllViews();
            addItems(items);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //        recalculateMeasureDiff();
        //
        //        int totalHeight = imgMain.getHeight() + satelliteDistance + measureDiff;
        //        int totalWidth = imgMain.getWidth() + satelliteDistance + measureDiff;
        //        setMeasuredDimension(totalWidth, totalHeight);
    }

    private static class SatelliteItemClickAnimationListener
        implements
            Animation.AnimationListener
    {
        private WeakReference<SatelliteMenu> menuRef;
        private int tag;

        public SatelliteItemClickAnimationListener(SatelliteMenu menu, int tag)
        {
            this.menuRef = new WeakReference<SatelliteMenu>(menu);
            this.tag = tag;
        }

        @Override
        public void onAnimationEnd(Animation animation)
        {
        }

        @Override
        public void onAnimationRepeat(Animation animation)
        {
        }

        @Override
        public void onAnimationStart(Animation animation)
        {
            SatelliteMenu menu = menuRef.get();
            if (menu != null && menu.closeItemsOnClick)
            {
                menu.close();
                if (menu.itemClickedListener != null)
                {
                    menu.itemClickedListener.eventOccured(tag);
                }
            }
        }
    }

    private static class SatelliteAnimationListener
        implements
            Animation.AnimationListener
    {
        private WeakReference<View> viewRef;
        private boolean isInAnimation;
        private Map<View, SatelliteMenuItem> viewToItemMap;

        public SatelliteAnimationListener(View view, boolean isIn,
                Map<View, SatelliteMenuItem> viewToItemMap)
        {
            this.viewRef = new WeakReference<View>(view);
            this.isInAnimation = isIn;
            this.viewToItemMap = viewToItemMap;
        }

        @Override
        public void onAnimationStart(Animation animation)
        {
            if (viewRef != null)
            {
                View view = viewRef.get();
                if (view != null)
                {
                    SatelliteMenuItem menuItem = viewToItemMap.get(view);
                    if (isInAnimation)
                    {
                        menuItem.getView().setVisibility(View.VISIBLE);
                        menuItem.getCloneView().setVisibility(View.INVISIBLE);
                    }
                    else
                    {
                        menuItem.getCloneView().setVisibility(View.INVISIBLE);
                        menuItem.getView().setVisibility(View.VISIBLE);
                    }
                }
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation)
        {
        }

        @Override
        public void onAnimationEnd(Animation animation)
        {
            if (viewRef != null)
            {
                View view = viewRef.get();
                if (view != null)
                {
                    SatelliteMenuItem menuItem = viewToItemMap.get(view);

                    if (isInAnimation)
                    {
                        menuItem.getView().setVisibility(View.INVISIBLE);
                        menuItem.getCloneView().setVisibility(View.INVISIBLE);
                    }
                    else
                    {
                        menuItem.getCloneView().setVisibility(View.VISIBLE);
                        menuItem.getView().setVisibility(View.INVISIBLE);
                    }
                }
            }
        }
    }

    public Map<View, SatelliteMenuItem> getViewToItemMap()
    {
        return viewToItemMap;
    }

    private static FrameLayout.LayoutParams getLayoutParams(View view)
    {
        return (FrameLayout.LayoutParams) view.getLayoutParams();
    }

    private static class InternalSatelliteOnClickListener implements View.OnClickListener
    {
        private WeakReference<SatelliteMenu> menuRef;

        public InternalSatelliteOnClickListener(SatelliteMenu menu)
        {
            this.menuRef = new WeakReference<SatelliteMenu>(menu);
        }

        @Override
        public void onClick(View v)
        {
            SatelliteMenu menu = menuRef.get();
            if (menu != null)
            {
                SatelliteMenuItem menuItem = menu.getViewToItemMap().get(v);
                v.startAnimation(menuItem.getClickAnimation());
            }
        }
    }

    /**
     * Sets the click listener for satellite items.
     * 
     * @param itemClickedListener
     */
    public void setOnItemClickedListener(SateliteClickedListener itemClickedListener)
    {
        this.itemClickedListener = itemClickedListener;
    }

    /**
     * Sets the click listener for satellite main.
     * 
     * @param itemClickedListener
     */
    public void setOnLongPressedListener(SateliteLongPressedListener longPressedListener)
    {
        this.longPressedListener = longPressedListener;
    }

    /**
     * Defines the algorithm to define the gap between each item. 
     * Note: Calling before adding items is strongly recommended. 
     * 
     * @param gapDegreeProvider
     */
    public void setGapDegreeProvider(IDegreeProvider gapDegreeProvider)
    {
        this.gapDegreesProvider = gapDegreeProvider;
        resetItems();
    }

    /**
     * Defines the total space between the initial and final item in degrees.
     * Note: Calling before adding items is strongly recommended.
     * 
     * @param totalSpacingDegree The degree between initial and final items. 
     */
    public void setTotalSpacingDegree(float totalSpacingDegree)
    {
        this.totalSpacingDegree = totalSpacingDegree;
        resetItems();
    }

    /**
     * Sets the distance of items from the center in pixels.
     * Note: Calling before adding items is strongly recommended.
     * 
     * @param distance the distance of items to center in pixels.
     */
    public void setSatelliteDistance(int distance)
    {
        this.satelliteDistance = distance;
        resetItems();
    }

    /**
     * Sets the duration for expanding and collapsing the items in miliseconds. 
     * Note: Calling before adding items is strongly recommended.
     * 
     * @param expandDuration the duration for expanding and collapsing the items in miliseconds.
     */
    public void setExpandDuration(int expandDuration)
    {
        this.expandDuration = expandDuration;
        resetItems();
    }

    /**
     * Sets the image resource for the center button.
     * 
     * @param resource The image resource.
     */
    public void setMainImage(int resource)
    {
        this.imgMain.setImageResource(resource);
    }

    /**
     * Sets the image drawable for the center button.
     * 
     * @param resource The image drawable.
     */
    public void setMainImage(Drawable drawable)
    {
        this.imgMain.setImageDrawable(drawable);
    }

    /**
     * Defines if the menu shall collapse the items when an item is clicked. Default value is true. 
     * 
     * @param closeItemsOnClick
     */
    public void setCloseItemsOnClick(boolean closeItemsOnClick)
    {
        this.closeItemsOnClick = closeItemsOnClick;
    }

    /**
     * The listener class for item click event. 
     * @author Siyamed SINIR
     */
    public interface SateliteClickedListener
    {
        /**
         * When an item is clicked, informs with the id of the item, which is given while adding the items. 
         * 
         * @param id The id of the item. 
         */
        public void eventOccured(int id);
    }

    /**
     * The listener class for item click event. 
     * @author Siyamed SINIR
     */
    public interface SateliteLongPressedListener
    {
        public void onLongPressed();
    }

    /**
     * Expand the menu items. 
     */
    public void expand()
    {
        openItems();
    }

    /**
     * Collapse the menu items
     */
    public void close()
    {
        closeItems();
    }

    @Override
    protected Parcelable onSaveInstanceState()
    {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.rotated = opened;
        ss.totalSpacingDegree = totalSpacingDegree;
        ss.satelliteDistance = satelliteDistance;
        ss.measureDiff = measureDiff;
        ss.expandDuration = expandDuration;
        ss.closeItemsOnClick = closeItemsOnClick;
        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state)
    {
        SavedState ss = (SavedState) state;
        opened = ss.rotated;
        totalSpacingDegree = ss.totalSpacingDegree;
        satelliteDistance = ss.satelliteDistance;
        measureDiff = ss.measureDiff;
        expandDuration = ss.expandDuration;
        closeItemsOnClick = ss.closeItemsOnClick;

        super.onRestoreInstanceState(ss.getSuperState());
    }

    static class SavedState extends BaseSavedState
    {
        boolean rotated;
        private float totalSpacingDegree;
        private int satelliteDistance;
        private int measureDiff;
        private int expandDuration;
        private boolean closeItemsOnClick;

        SavedState(Parcelable superState)
        {
            super(superState);
        }

        public SavedState(Parcel in)
        {
            super(in);
            rotated = Boolean.valueOf(in.readString());
            totalSpacingDegree = in.readFloat();
            satelliteDistance = in.readInt();
            measureDiff = in.readInt();
            expandDuration = in.readInt();
            closeItemsOnClick = Boolean.valueOf(in.readString());

        }

        @Override
        public int describeContents()
        {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel out, int flags)
        {
            super.writeToParcel(out, flags);
            out.writeString(Boolean.toString(rotated));
            out.writeFloat(totalSpacingDegree);
            out.writeInt(satelliteDistance);
            out.writeInt(measureDiff);
            out.writeInt(expandDuration);
            out.writeString(Boolean.toString(closeItemsOnClick));
        }

        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>()
        {
            public SavedState createFromParcel(Parcel in)
            {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size)
            {
                return new SavedState[size];
            }
        };
    }

    public void resetExpandState(ExpandState expandState)
    {
        //        currentExpandState = expandState;
        resetItems();
        resetExpandButtonLayout();
    }

    private void resetExpandButtonLayout()
    {
        // TODO Auto-generated method stub
        int l = 0, t = 0;
        switch (currentExpandState.ordinal())
        {
            case 0 :
                l = 0;
                t = lpImgMain.topMargin - buttonOffset;
                break;
            case 1 :
                l = getWidth() - flExpandButtons.getWidth() - getPaddingLeft()
                    - getPaddingRight();
                t = lpImgMain.topMargin - buttonOffset;
                break;
            case 2 :
                l = 0;
                t = lpImgMain.topMargin + imgMain.getHeight()
                    - flExpandButtons.getHeight() + DEFAULT_EXPAND_LAYOUT_EXTRA_WIDTH;
                break;
            case 3 :
                l = getWidth() - flExpandButtons.getWidth() - getPaddingLeft()
                    - getPaddingRight();
                t = lpImgMain.topMargin + imgMain.getHeight()
                    - flExpandButtons.getHeight() + DEFAULT_EXPAND_LAYOUT_EXTRA_WIDTH;
                break;

            default :
                break;
        }
        lpFlExpandButtons.setMargins(l, t, 0, 0);
        flExpandButtons.setLayoutParams(lpFlExpandButtons);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        // TODO Auto-generated method stub
        // TODO Auto-generated method stub
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            if (imgMain.getLeft() <= event.getX() && imgMain.getRight() >= event.getX()
                && imgMain.getTop() <= event.getY()
                && imgMain.getBottom() >= event.getY())
            {
                pDown.x = event.getX();
                pDown.y = event.getY();
                isTouchBtn = true;
                lastTouchTime = System.currentTimeMillis();
                //                if (!opened)
                //                {
                //                    startPressAnim();
                //                }
            }
            else
            {
                isTouchBtn = false;
            }
        }
        if (isTouchBtn)
        {
            switch (event.getAction())
            {
                case MotionEvent.ACTION_MOVE :
                    if (((Math.abs(event.getX() - pDown.x) < touchSlop && Math.abs(event
                            .getY() - pDown.y) < touchSlop) || opened)
                        && !isMove)
                    {
                        break;
                    }
                    else
                    {
                        isMove = true;
                        closeItems();
                        int l, t;
                        l = (int) (event.getX() - imgMain.getWidth() / 2);
                        t = (int) (event.getY() - imgMain.getHeight() / 2);
                        if (l < buttonOffset)
                        {
                            l = buttonOffset;
                        }
                        if (l + imgMain.getWidth() > getWidth() - getPaddingLeft()
                            - getPaddingRight())
                        {
                            l = getWidth() - imgMain.getWidth() - getPaddingLeft()
                                - getPaddingRight() - buttonOffset;
                        }
                        if (t < buttonOffset)
                        {
                            t = buttonOffset;
                        }
                        if (t + imgMain.getHeight() > getHeight() - getPaddingTop()
                            - getPaddingBottom())
                        {
                            t = getHeight() - imgMain.getHeight() - getPaddingTop()
                                - getPaddingBottom() - buttonOffset;
                        }
                        lpImgMain.setMargins(l, t, 0, 0);
                        imgMain.setLayoutParams(lpImgMain);
                        break;
                    }
                case MotionEvent.ACTION_UP :

                    if (!isMove)
                    {
                        if (System.currentTimeMillis() - lastTouchTime > 10000
                            && null != longPressedListener)
                        {
                            Log.d("FloatButton", "Long Pressed!");
                            longPressedListener.onLongPressed();
                        }
                        else
                        {
                            Log.d("FloatButton", "Clicked!");
                            SatelliteMenu.this.onClick();
                        }

                        return true;
                    }

                    int l;
                    if (lpImgMain.leftMargin <= getWidth() - lpImgMain.leftMargin
                        - imgMain.getWidth() - getPaddingLeft() - getPaddingRight())
                    {
                        l = buttonOffset;
                    }
                    else
                    {
                        l = getWidth() - imgMain.getWidth() - getPaddingLeft()
                            - getPaddingRight() - buttonOffset;
                    }
                    lpImgMain.setMargins(l, lpImgMain.topMargin, 0, 0);
                    imgMain.setLayoutParams(lpImgMain);
                    if (lpImgMain.leftMargin == buttonOffset
                        && lpImgMain.topMargin + imgMain.getHeight() - getPaddingTop() < flExpandButtons
                                .getHeight())
                    {
                        currentExpandState = ExpandState.STATE_LEFT_TOP;
                    }
                    else if (lpImgMain.leftMargin > buttonOffset
                        && lpImgMain.topMargin + imgMain.getHeight() - getPaddingTop() < flExpandButtons
                                .getHeight())
                    {
                        currentExpandState = ExpandState.STATE_RIGHT_TOP;
                    }
                    else if (lpImgMain.leftMargin == buttonOffset
                        && lpImgMain.topMargin + imgMain.getHeight() - getPaddingTop() >= flExpandButtons
                                .getHeight())
                    {
                        currentExpandState = ExpandState.STATE_LEFT_BOTTOM;
                    }
                    else if (lpImgMain.leftMargin > buttonOffset
                        && lpImgMain.topMargin + imgMain.getHeight() - getPaddingTop() >= flExpandButtons
                                .getHeight())
                    {
                        currentExpandState = ExpandState.STATE_RIGHT_BOTTOM;
                    }
                    resetExpandState(currentExpandState);
                    isMove = false;
                    //                    if (opened)
                    //                    {
                    //                        startReverseAnim();
                    //                    }
                    break;

                default :
                    break;
            }
            return true;
        }
        else if (opened)
        {
            close();
            return true;
        }
        return false;

    }
}
