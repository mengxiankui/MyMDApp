package com.widgets;


import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.widgets.photoview.PhotoView;
import com.widgets.photoview.PhotoViewAttacher;


/**
 * @author zhy
 *         博客地址：http://blog.csdn.net/lmj623565791/article/details/39474553
 */
public class ZoomImageView extends PhotoView {

    // The current bitmap being displayed.

    private static final float MIN_SCALE = 0.25f;

    private static final float MAX_SCALE = 4.0f;

    private static final int ZOOM_STEP = 2;

    private float fCurrentScale = 1.0f;

    private Runnable mOnLayoutRunnable = null;

    private int horizontalMinistance = 20;            //水平最小识别距离
    private int minVelocity = 10;

    private IPreNextListener preNextListener;

    private PhotoViewAttacher.OnSingleFlingListener singleFlingListener = new PhotoViewAttacher.OnSingleFlingListener() {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1.getX() - e2.getX() > horizontalMinistance && Math.abs(velocityX) > minVelocity) {

                if (null!= preNextListener)
                    preNextListener.next();

                return true;

            } else if (e2.getX() - e1.getX() > horizontalMinistance && Math.abs(velocityX) > minVelocity) {
                if (null!= preNextListener)
                    preNextListener.pre();
                return true;
            }
            return false;
        }
    };

    public ZoomImageView(Context context) {
        super(context);
    }

    public ZoomImageView(Context context, AttributeSet attr) {
        super(context, attr);
    }

    public ZoomImageView(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
    }

    @Override
    protected void init() {
        super.init();
        setOnSingleFlingListener(singleFlingListener);
        setMinimumScale(MIN_SCALE);
        setMaximumScale(MAX_SCALE);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Runnable r = mOnLayoutRunnable;
        if (r != null) {
            mOnLayoutRunnable = null;
            r.run();
        }
    }

    @Override
    public void setImageBitmap(final Bitmap bitmap) {
//		final int viewWidth = getWidth();
//		if (viewWidth <= 0)  {
//			mOnLayoutRunnable = new Runnable() {
//				public void run() {
//					setImageBitmap(bitmap);
//				}
//			};
//			return;
//		}
        if (bitmap != null) {
            super.setImageBitmap(bitmap);
            fCurrentScale = 1f;
        } else {
            super.setImageBitmap(null);
        }

    }

    // Setup the base matrix so that the image is centered and scaled properly.
    private float getProperBaseScale(Bitmap bitmap) {
        float viewWidth = getWidth();
        float viewHeight = getHeight();

        float w = bitmap.getWidth();
        float h = bitmap.getHeight();

        // We limit up-scaling to 3x otherwise the result may look bad if it's
        // a small icon.
        float widthScale = Math.min(viewWidth / w, 3.0f);
        float heightScale = Math.min(viewHeight / h, 3.0f);
        float scale = Math.min(widthScale, heightScale);
        return scale;
    }

    public boolean zoomIn() {
        if (fCurrentScale == MAX_SCALE)
            return false;
        if (fCurrentScale * ZOOM_STEP <= MAX_SCALE) {
            fCurrentScale *= ZOOM_STEP;
        } else {
            fCurrentScale = MAX_SCALE;
        }
        setScale(fCurrentScale);
        return true;
    }

    public boolean zoomOut() {
        if (fCurrentScale == MIN_SCALE)
            return false;
        if (fCurrentScale / ZOOM_STEP >= MIN_SCALE) {
            fCurrentScale /= ZOOM_STEP;
        } else {
            fCurrentScale = MIN_SCALE;
        }
        setScale(fCurrentScale);
        return true;
    }

    public void rotateTo(int rotateDegree) {
        setRotationTo(rotateDegree);
        fCurrentScale = 1f;
    }

    public void rotate() {
        setRotationBy(90);
        fCurrentScale = 1f;
    }

    @Override
    public void setOnMatrixChangeListener(PhotoViewAttacher.OnMatrixChangedListener listener) {
        super.setOnMatrixChangeListener(listener);
    }

    @Override
    public void setOnPhotoTapListener(PhotoViewAttacher.OnPhotoTapListener listener) {
        super.setOnPhotoTapListener(listener);
    }

    @Override
    public void setOnViewTapListener(PhotoViewAttacher.OnViewTapListener listener) {
        super.setOnViewTapListener(listener);
    }

    public void setPreNextListener(IPreNextListener preNextListener) {
        this.preNextListener = preNextListener;
    }

    public interface IPreNextListener {
        void pre();

        void next();
    }
}
