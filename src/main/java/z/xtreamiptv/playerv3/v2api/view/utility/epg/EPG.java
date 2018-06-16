package z.xtreamiptv.playerv3.v2api.view.utility.epg;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.Tab;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;
import com.google.common.collect.Maps;
import z.xtreamiptv.playerv3.R;
import z.xtreamiptv.playerv3.miscelleneious.common.AppConst;
import z.xtreamiptv.playerv3.miscelleneious.common.Utils;
import z.xtreamiptv.playerv3.v2api.view.utility.epg.domain.EPGChannel;
import z.xtreamiptv.playerv3.v2api.view.utility.epg.domain.EPGEvent;
import z.xtreamiptv.playerv3.v2api.view.utility.epg.domain.EPGState;
import z.xtreamiptv.playerv3.v2api.view.utility.epg.misc.EPGDataImpl;
import z.xtreamiptv.playerv3.v2api.view.utility.epg.misc.EPGUtil;
import com.squareup.picasso.Picasso.LoadedFrom;
import com.squareup.picasso.Target;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import org.joda.time.LocalDateTime;
import tv.danmaku.ijk.media.player.IjkMediaCodecInfo;

public class EPG extends ViewGroup {
    public static final int DAYS_BACK_MILLIS = 86400000;
    public static final int DAYS_FORWARD_MILLIS = 86400000;
    public static int HOURS_IN_VIEWPORT_MILLIS = 7200000;
    public static final int TIME_LABEL_SPACING_MILLIS = 1800000;
    private static ImageView programImage = null;
    public static int screenHeight;
    public static int screenWidth;
    public final String TAG;
    private PopupWindow changeSortPopUp;
    Context context1;
    private TextView currentEventDescriptionTextView;
    private TextView currentEventTextView;
    private TextView currentEventTimeTextView;
    private EPGData epgData;
    public EPGChannel epgDataFirstChannelID;
    public EPGChannel epgDataLastChannelID;
    private AsyncTask loadProgramImage;
    private SharedPreferences loginPreferencesAfterLogin;
    private SharedPreferences loginPreferencesSharedPref;
    private SharedPreferences loginPreferencesSharedPref_time_format;
    private final Map<String, Bitmap> mChannelImageCache;
    private final Map<String, Target> mChannelImageTargetCache;
    private final int mChannelLayoutBackground;
    private final int mChannelLayoutHeight;
    private final int mChannelLayoutLeftMargin;
    private final int mChannelLayoutMargin;
    private final int mChannelLayoutMaxLength;
    private final int mChannelLayoutPadding;
    private final int mChannelLayoutWidth;
    private EPGClickListener mClickListener;
    private final Rect mClipRect;
    private final Rect mDrawingRect;
    private final int mEPGBackground;
    private final int mEventLayoutBackground;
    private final int mEventLayoutBackgroundCurrent;
    private final int mEventLayoutBackgroundNoProg;
    private final int mEventLayoutBackgroundSelected;
    private final int mEventLayoutTextColor;
    private final int mEventLayoutTextSize;
    private final GestureDetector mGestureDetector;
    private long mMargin;
    private int mMaxHorizontalScroll;
    private int mMaxVerticalScroll;
    private final Rect mMeasuringRect;
    private long mMillisPerPixel;
    private final Paint mPaint;
    private final Bitmap mResetButtonIcon;
    private final int mResetButtonMargin;
    private final int mResetButtonSize;
    private final Scroller mScroller;
    private final int mTimeBarHeight;
    private final int mTimeBarLineColor;
    private final int mTimeBarLineWidth;
    private final int mTimeBarTextSize;
    private long mTimeLowerBoundary;
    private long mTimeOffset;
    private long mTimeUpperBoundary;
    private int orientation;
    private SimpleDateFormat programTimeFormat;
    private SimpleDateFormat programTimeFormatLong;
    private EPGEvent selectedEvent;

    class C17415 implements OnClickListener {
        C17415() {
        }

        public void onClick(View view) {
            EPG.this.changeSortPopUp.dismiss();
        }
    }

    private static class AsyncLoadProgramImage extends AsyncTask<EPGEvent, Void, Bitmap> {
        private final EPG epg;
        EPGEvent epgEvent;

        public AsyncLoadProgramImage(EPG epg, EPGEvent epgEvent) {
            this.epg = epg;
            this.epgEvent = epgEvent;
        }

        protected android.graphics.Bitmap doInBackground(EPGEvent... epgEvent) {
            /* TODO */
            return null;
        }

        protected void onPostExecute(Bitmap bmp) {
            if (bmp != null) {
                try {
                    if (EPG.programImage != null) {
                        EPG.programImage.setImageBitmap(bmp);
                        EPG.updateImageCropping(EPG.programImage);
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class OnGestureListener extends SimpleOnGestureListener {
        private OnGestureListener() {
        }

        public boolean onSingleTapUp(MotionEvent e) {
            int x = (int) e.getX();
            int y = (int) e.getY();
            int scrollX = EPG.this.getScrollX() + x;
            int scrollY = EPG.this.getScrollY() + y;
            int channelPosition = EPG.this.getChannelPosition(scrollY);
            if (!(channelPosition == -1 || EPG.this.mClickListener == null)) {
                if (EPG.this.calculateResetButtonHitArea().contains(scrollX, scrollY)) {
                    EPG.this.mClickListener.onResetButtonClicked();
                } else if (EPG.this.calculateChannelsHitArea().contains(x, y)) {
                    EPG.this.mClickListener.onChannelClicked(channelPosition, EPG.this.epgData.getChannel(channelPosition));
                } else if (EPG.this.calculateProgramsHitArea().contains(x, y)) {
                    int programPosition = EPG.this.getProgramPosition(channelPosition, EPG.this.getTimeFrom((EPG.this.getScrollX() + x) - EPG.this.calculateProgramsHitArea().left));
                    if (programPosition != -1) {
                        EPG.this.mClickListener.onEventClicked(channelPosition, programPosition, EPG.this.epgData.getEvent(channelPosition, programPosition));
                    }
                }
            }
            return true;
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            int dx = (int) distanceX;
            int dy = (int) distanceY;
            int x = EPG.this.getScrollX();
            int y = EPG.this.getScrollY();
            if (x + dx < 0) {
                dx = 0 - x;
            }
            if (y + dy < 0) {
                dy = 0 - y;
            }
            if (x + dx > EPG.this.mMaxHorizontalScroll) {
                dx = EPG.this.mMaxHorizontalScroll - x;
            }
            if (y + dy > EPG.this.mMaxVerticalScroll) {
                dy = EPG.this.mMaxVerticalScroll - y;
            }
            EPG.this.scrollBy(dx, dy);
            return true;
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float vX, float vY) {
            EPG.this.mScroller.fling(EPG.this.getScrollX(), EPG.this.getScrollY(), -((int) vX), -((int) vY), 0, EPG.this.mMaxHorizontalScroll, 0, EPG.this.mMaxVerticalScroll);
            EPG.this.redraw();
            return true;
        }

        public boolean onDown(MotionEvent e) {
            if (!EPG.this.mScroller.isFinished()) {
                EPG.this.mScroller.forceFinished(true);
            }
            return true;
        }
    }

    public EPG(Context context) {
        this(context, null);
        this.context1 = context;
    }

    public EPG(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.context1 = context;
    }

    public EPG(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.TAG = getClass().getSimpleName();
        this.mMargin = 200000;
        this.epgData = null;
        this.epgDataFirstChannelID = null;
        this.epgDataLastChannelID = null;
        this.selectedEvent = null;
        this.loadProgramImage = null;
        this.context1 = context;
        setWillNotDraw(false);
        resetBoundaries();
        if ((getResources().getConfiguration().screenLayout & 15) == 3) {
            HOURS_IN_VIEWPORT_MILLIS = 14400000;
        }
        this.mDrawingRect = new Rect();
        this.mClipRect = new Rect();
        this.mMeasuringRect = new Rect();
        this.mPaint = new Paint(1);
        this.mGestureDetector = new GestureDetector(context, new OnGestureListener());
        this.mChannelImageCache = Maps.newHashMap();
        this.mChannelImageTargetCache = Maps.newHashMap();
        this.mScroller = new Scroller(context);
        this.mScroller.setFriction(0.2f);
        this.mEPGBackground = getResources().getColor(R.color.epg_background);
        this.mChannelLayoutMargin = getResources().getDimensionPixelSize(R.dimen.epg_channel_layout_margin);
        this.mChannelLayoutPadding = getResources().getDimensionPixelSize(R.dimen.epg_channel_layout_padding);
        this.mChannelLayoutHeight = getResources().getDimensionPixelSize(R.dimen.epg_channel_layout_height);
        this.mChannelLayoutLeftMargin = getResources().getDimensionPixelSize(R.dimen.epg_channel_layout_margin_left);
        this.mChannelLayoutMaxLength = getResources().getDimensionPixelSize(R.dimen.epg_channel_maximum_length);
        this.mChannelLayoutWidth = getResources().getDimensionPixelSize(R.dimen.epg_channel_layout_width);
        this.mChannelLayoutBackground = getResources().getColor(R.color.epg_channel_layout_background);
        this.mEventLayoutBackground = getResources().getColor(R.color.epg_event_layout_background);
        this.mEventLayoutBackgroundCurrent = getResources().getColor(R.color.epg_event_layout_background_current);
        this.mEventLayoutBackgroundSelected = getResources().getColor(R.color.epg_event_layout_background_selected);
        this.mEventLayoutBackgroundNoProg = getResources().getColor(R.color.epg_event_layout_background_no_prog);
        this.mEventLayoutTextColor = getResources().getColor(R.color.epg_event_layout_text);
        this.mEventLayoutTextSize = getResources().getDimensionPixelSize(R.dimen.epg_event_layout_text);
        this.mTimeBarHeight = getResources().getDimensionPixelSize(R.dimen.epg_time_bar_height);
        this.mTimeBarTextSize = getResources().getDimensionPixelSize(R.dimen.epg_time_bar_text);
        this.mTimeBarLineWidth = getResources().getDimensionPixelSize(R.dimen.epg_time_bar_line_width);
        this.mTimeBarLineColor = getResources().getColor(R.color.epg_time_bar);
        this.mResetButtonSize = getResources().getDimensionPixelSize(R.dimen.epg_reset_button_size);
        this.mResetButtonMargin = getResources().getDimensionPixelSize(R.dimen.epg_reset_button_margin);
        Options options = new Options();
        options.outWidth = this.mResetButtonSize;
        options.outHeight = this.mResetButtonSize;
        this.mResetButtonIcon = BitmapFactory.decodeResource(getResources(), R.drawable.reset, options);
        Options backOptions = new Options();
        backOptions.outWidth = this.mResetButtonSize;
        backOptions.outHeight = this.mResetButtonSize;
    }

    public Parcelable onSaveInstanceState() {
        EPGState epgState = new EPGState(super.onSaveInstanceState());
        epgState.setCurrentEvent(this.selectedEvent);
        return epgState;
    }

    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof EPGState) {
            EPGState epgState = (EPGState) state;
            super.onRestoreInstanceState(epgState.getSuperState());
            this.selectedEvent = epgState.getCurrentEvent();
            return;
        }
        super.onRestoreInstanceState(state);
    }

    private int getChannelAreaWidth() {
        return (this.mChannelLayoutWidth + this.mChannelLayoutPadding) + this.mChannelLayoutMargin;
    }

    private int getProgramAreaWidth() {
        return getWidth() - getChannelAreaWidth();
    }

    protected void onDraw(Canvas canvas) {
        if (this.epgData != null && this.epgData.hasData()) {
            this.mTimeLowerBoundary = getTimeFrom(getScrollX());
            this.mTimeUpperBoundary = getTimeFrom(getScrollX() + getWidth());
            Rect drawingRect = this.mDrawingRect;
            drawingRect.left = getScrollX();
            drawingRect.top = getScrollY();
            drawingRect.right = drawingRect.left + getWidth();
            drawingRect.bottom = drawingRect.top + getHeight();
            drawChannelListItems(canvas, drawingRect);
            drawEvents(canvas, drawingRect);
            drawTimebar(canvas, drawingRect);
            drawTimeLine(canvas, drawingRect);
            drawResetButton(canvas, drawingRect);
            if (this.mScroller.computeScrollOffset()) {
                scrollTo(this.mScroller.getCurrX(), this.mScroller.getCurrY());
            }
        }
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        recalculateAndRedraw(this.selectedEvent, false, null, null);
    }

    public boolean onTouchEvent(MotionEvent event) {
        return this.mGestureDetector.onTouchEvent(event);
    }

    public boolean dispatchGenericMotionEvent(MotionEvent event) {
        return false;
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        return false;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return super.onKeyUp(keyCode, event);
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
    }

    private void drawResetButton(Canvas canvas, Rect drawingRect) {
        if (((long) Math.abs(getXPositionStart() - getScrollX())) > ((long) (getWidth() / 3))) {
            drawingRect = calculateResetButtonHitArea();
            this.mPaint.setColor(this.mTimeBarLineColor);
            canvas.drawCircle((float) (drawingRect.right - (this.mResetButtonSize / 2)), (float) (drawingRect.bottom - (this.mResetButtonSize / 2)), (float) (Math.min(drawingRect.width(), drawingRect.height()) / 2), this.mPaint);
            drawingRect.left += this.mResetButtonMargin;
            drawingRect.right -= this.mResetButtonMargin;
            drawingRect.top += this.mResetButtonMargin;
            drawingRect.bottom -= this.mResetButtonMargin;
            canvas.drawBitmap(this.mResetButtonIcon, null, drawingRect, this.mPaint);
        }
    }

    private void drawTimebarBottomStroke(Canvas canvas, Rect drawingRect) {
        drawingRect.left = getScrollX();
        drawingRect.top = getScrollY() + this.mTimeBarHeight;
        drawingRect.right = drawingRect.left + getWidth();
        drawingRect.bottom = drawingRect.top + this.mChannelLayoutMargin;
        this.mPaint.setColor(this.mEPGBackground);
        canvas.drawRect(drawingRect, this.mPaint);
    }

    private void drawTimebar(Canvas canvas, Rect drawingRect) {
        drawingRect.left = (getScrollX() + this.mChannelLayoutWidth) + this.mChannelLayoutMargin;
        drawingRect.top = getScrollY();
        drawingRect.right = drawingRect.left + getWidth();
        drawingRect.bottom = drawingRect.top + this.mTimeBarHeight;
        this.mClipRect.left = (getScrollX() + this.mChannelLayoutWidth) + this.mChannelLayoutMargin;
        this.mClipRect.top = getScrollY();
        this.mClipRect.right = getScrollX() + getWidth();
        this.mClipRect.bottom = this.mClipRect.top + this.mTimeBarHeight;
        canvas.save();
        canvas.clipRect(this.mClipRect);
        this.mPaint.setColor(this.mChannelLayoutBackground);
        canvas.drawRect(drawingRect, this.mPaint);
        this.mPaint.setColor(this.mEventLayoutTextColor);
        this.mPaint.setTextSize((float) this.mTimeBarTextSize);
        for (int i = 0; i < HOURS_IN_VIEWPORT_MILLIS / 1800000; i++) {
            long time = 1800000 * (((this.mTimeLowerBoundary + ((long) (1800000 * i))) + 900000) / 1800000);
            canvas.drawText(EPGUtil.getShortTime(this.context1, time), (float) getXFrom(time), (float) (drawingRect.top + (((drawingRect.bottom - drawingRect.top) / 2) + (this.mTimeBarTextSize / 2))), this.mPaint);
        }
        canvas.restore();
        drawTimebarDayIndicator(canvas, drawingRect);
        drawTimebarBottomStroke(canvas, drawingRect);
    }

    private void drawTimebarDayIndicator(Canvas canvas, Rect drawingRect) {
        drawingRect.left = getScrollX();
        drawingRect.top = getScrollY();
        drawingRect.right = drawingRect.left + this.mChannelLayoutWidth;
        drawingRect.bottom = drawingRect.top + this.mTimeBarHeight;
        this.mPaint.setColor(this.mChannelLayoutBackground);
        canvas.drawRect(drawingRect, this.mPaint);
        this.mPaint.setColor(this.mEventLayoutTextColor);
        this.mPaint.setTextSize((float) this.mTimeBarTextSize);
        canvas.drawText(EPGUtil.getEPGdayName(this.mTimeLowerBoundary), (float) (drawingRect.left + ((drawingRect.right - drawingRect.left) / 2)), (float) (drawingRect.top + (((drawingRect.bottom - drawingRect.top) / 2) + (this.mTimeBarTextSize / 2))), this.mPaint);
    }

    private void drawTimeLine(Canvas canvas, Rect drawingRect) {
        long now = System.currentTimeMillis() + ((long) getTimeShiftMilliSeconds());
        if (shouldDrawTimeLine(now)) {
            drawingRect.left = getXFrom(now);
            drawingRect.top = getScrollY();
            drawingRect.right = drawingRect.left + this.mTimeBarLineWidth;
            drawingRect.bottom = drawingRect.top + getHeight();
            this.mPaint.setColor(this.mTimeBarLineColor);
            canvas.drawRect(drawingRect, this.mPaint);
        }
    }

    public int getTimeShiftMilliSeconds() {
        this.loginPreferencesAfterLogin = getContext().getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
        return Utils.getMilliSeconds(this.loginPreferencesAfterLogin.getString(AppConst.LOGIN_PREF_SELECTED_EPG_SHIFT, ""));
    }

    private void selectEvent() {
    }

    private void drawEvent(Canvas canvas, int channelPosition, EPGEvent event, Rect drawingRect) {
        setEventDrawingRectangle(channelPosition, event.getStart(), event.getEnd(), drawingRect);
        String title = event.getTitle();
        if (event.isSelected()) {
            this.mPaint.setColor(this.mEventLayoutBackgroundSelected);
        } else if (event.isCurrent(getTimeShiftMilliSeconds())) {
            this.mPaint.setColor(this.mEventLayoutBackgroundCurrent);
        } else if (title.equals(getContext().getResources().getString(R.string.no_information))) {
            this.mPaint.setColor(this.mEventLayoutBackgroundNoProg);
        } else {
            this.mPaint.setColor(this.mEventLayoutBackground);
        }
        canvas.drawRect(drawingRect, this.mPaint);
        drawingRect.left += this.mChannelLayoutPadding + 16;
        drawingRect.right -= this.mChannelLayoutPadding;
        this.mPaint.setColor(this.mEventLayoutTextColor);
        this.mPaint.setTextSize((float) this.mEventLayoutTextSize);
        this.mPaint.getTextBounds(event.getTitle(), 0, event.getTitle().length(), this.mMeasuringRect);
        drawingRect.top += ((drawingRect.bottom - drawingRect.top) / 2) + (this.mMeasuringRect.height() / 2);
        canvas.drawText(title.substring(0, this.mPaint.breakText(title, true, (float) (drawingRect.right - drawingRect.left), null)), (float) drawingRect.left, (float) drawingRect.top, this.mPaint);
    }

    private void setEventDrawingRectangle(int channelPosition, long start, long end, Rect drawingRect) {
        drawingRect.left = getXFrom(start);
        drawingRect.top = getTopFrom(channelPosition);
        drawingRect.right = getXFrom(end) - this.mChannelLayoutMargin;
        drawingRect.bottom = drawingRect.top + this.mChannelLayoutHeight;
    }

    private void drawChannelListItems(Canvas canvas, Rect drawingRect) {
        this.mMeasuringRect.left = getScrollX();
        this.mMeasuringRect.top = getScrollY();
        this.mMeasuringRect.right = drawingRect.left + this.mChannelLayoutWidth;
        this.mMeasuringRect.bottom = this.mMeasuringRect.top + getHeight();
        this.mPaint.setColor(this.mChannelLayoutBackground);
        canvas.drawRect(this.mMeasuringRect, this.mPaint);
        int firstPos = getFirstVisibleChannelPosition();
        int lastPos = getLastVisibleChannelPosition();
        for (int pos = firstPos; pos <= lastPos; pos++) {
            drawChannelItem(canvas, pos, drawingRect);
        }
    }

    private void drawEvents(Canvas canvas, Rect drawingRect) {
        int firstPos = getFirstVisibleChannelPosition();
        int lastPos = getLastVisibleChannelPosition();
        for (int channelPos = firstPos; channelPos <= lastPos; channelPos++) {
            this.mClipRect.left = (getScrollX() + this.mChannelLayoutWidth) + this.mChannelLayoutMargin;
            this.mClipRect.top = getTopFrom(channelPos);
            this.mClipRect.right = getScrollX() + getWidth();
            this.mClipRect.bottom = this.mClipRect.top + this.mChannelLayoutHeight;
            canvas.save();
            canvas.clipRect(this.mClipRect);
            boolean foundFirst = false;
            for (EPGEvent event : this.epgData.getEvents(channelPos)) {
                if (isEventVisible(event.getStart(), event.getEnd())) {
                    drawEvent(canvas, channelPos, event, drawingRect);
                    foundFirst = true;
                } else if (foundFirst) {
                    break;
                }
            }
            canvas.restore();
        }
    }

    private void drawChannelItem(Canvas canvas, int position, Rect drawingRect) {
        drawingRect.left = getScrollX();
        drawingRect.top = getTopFrom(position);
        drawingRect.right = drawingRect.left + this.mChannelLayoutLeftMargin;
        drawingRect.bottom = drawingRect.top + this.mChannelLayoutHeight;
        final String imageURL = this.epgData.getChannel(position).getImageURL();
        String channelName = this.epgData.getChannel(position).getName();
        if (this.mChannelImageCache.containsKey(imageURL)) {
            Bitmap image = (Bitmap) this.mChannelImageCache.get(imageURL);
            drawingRect = getDrawingRectForChannelImage(drawingRect, image);
            canvas.drawBitmap(image, null, drawingRect, null);
        } else {
            int smallestSide = Math.min(this.mChannelLayoutHeight, this.mChannelLayoutWidth);
            if (!this.mChannelImageTargetCache.containsKey(imageURL)) {
                this.mChannelImageTargetCache.put(imageURL, new Target() {
                    public void onBitmapLoaded(Bitmap bitmap, LoadedFrom from) {
                        EPG.this.mChannelImageCache.put(imageURL, bitmap);
                        EPG.this.redraw();
                        EPG.this.mChannelImageTargetCache.remove(imageURL);
                    }

                    public void onBitmapFailed(Drawable errorDrawable) {
                    }

                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                    }
                });
                EPGUtil.loadImageInto(getContext(), imageURL, smallestSide, smallestSide, (Target) this.mChannelImageTargetCache.get(imageURL));
            }
        }
        this.mPaint.setColor(this.mEventLayoutTextColor);
        int oldLength = channelName.length();
        channelName = channelName.substring(0, this.mPaint.breakText(channelName, true, (float) this.mChannelLayoutMaxLength, null));
        String dotConcat = "";
        if (channelName.length() < oldLength) {
            dotConcat = "..";
        }
        canvas.drawText(channelName + dotConcat, (float) (drawingRect.right + 10), (float) (drawingRect.centerY() + 10), this.mPaint);
    }

    private Rect getDrawingRectForChannelImage(Rect drawingRect, Bitmap image) {
        drawingRect.left += this.mChannelLayoutPadding;
        drawingRect.top += this.mChannelLayoutPadding;
        drawingRect.right -= this.mChannelLayoutPadding;
        drawingRect.bottom -= this.mChannelLayoutPadding;
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        float imageRatio = ((float) imageHeight) / ((float) imageWidth);
        int rectWidth = drawingRect.right - drawingRect.left;
        int rectHeight = drawingRect.bottom - drawingRect.top;
        int padding;
        if (imageWidth > imageHeight) {
            padding = ((int) (((float) rectHeight) - (((float) rectWidth) * imageRatio))) / 2;
            drawingRect.top += padding;
            drawingRect.bottom -= padding;
        } else if (imageWidth <= imageHeight) {
            padding = ((int) (((float) rectWidth) - (((float) rectHeight) / imageRatio))) / 2;
            drawingRect.left += padding;
            drawingRect.right -= padding;
        }
        return drawingRect;
    }

    private boolean shouldDrawTimeLine(long now) {
        return now >= this.mTimeLowerBoundary && now < this.mTimeUpperBoundary;
    }

    private boolean isEventVisible(long start, long end) {
        return (start >= this.mTimeLowerBoundary && start <= this.mTimeUpperBoundary) || ((end >= this.mTimeLowerBoundary && end <= this.mTimeUpperBoundary) || (start <= this.mTimeLowerBoundary && end >= this.mTimeUpperBoundary));
    }

    private long calculatedBaseLine() {
        return LocalDateTime.now().toDateTime().minusMillis(86400000).getMillis();
    }

    private int getFirstVisibleChannelPosition() {
        int position = ((getScrollY() - this.mChannelLayoutMargin) - this.mTimeBarHeight) / (this.mChannelLayoutHeight + this.mChannelLayoutMargin);
        if (position < 0) {
            return 0;
        }
        return position;
    }

    private int getLastVisibleChannelPosition() {
        int y = getScrollY();
        int totalChannelCount = this.epgData.getChannelCount();
        int screenHeight = getHeight();
        int position = (((y + screenHeight) + this.mTimeBarHeight) - this.mChannelLayoutMargin) / (this.mChannelLayoutHeight + this.mChannelLayoutMargin);
        if (position > totalChannelCount - 1) {
            position = totalChannelCount - 1;
        }
        return (y + screenHeight <= this.mChannelLayoutHeight * position || position >= totalChannelCount - 1) ? position : position + 1;
    }

    private EPGChannel getFirstChannelData() {
        return this.epgDataFirstChannelID;
    }

    private EPGChannel getFirstLastChannelData() {
        return this.epgDataLastChannelID;
    }

    private void calculateMaxHorizontalScroll() {
        this.mMaxHorizontalScroll = (int) (((long) (172800000 - HOURS_IN_VIEWPORT_MILLIS)) / this.mMillisPerPixel);
    }

    private void calculateMaxVerticalScroll() {
        int maxVerticalScroll = getTopFrom(this.epgData.getChannelCount() - 1) + this.mChannelLayoutHeight;
        this.mMaxVerticalScroll = maxVerticalScroll < getHeight() ? 0 : maxVerticalScroll - getHeight();
    }

    private int getXFrom(long time) {
        return ((((int) ((time - this.mTimeOffset) / this.mMillisPerPixel)) + this.mChannelLayoutMargin) + this.mChannelLayoutWidth) + this.mChannelLayoutMargin;
    }

    private int getTopFrom(int position) {
        return (((this.mChannelLayoutHeight + this.mChannelLayoutMargin) * position) + this.mChannelLayoutMargin) + this.mTimeBarHeight;
    }

    private long getTimeFrom(int x) {
        return (((long) x) * this.mMillisPerPixel) + this.mTimeOffset;
    }

    private long calculateMillisPerPixel() {
        return (long) (HOURS_IN_VIEWPORT_MILLIS / ((getResources().getDisplayMetrics().widthPixels - this.mChannelLayoutWidth) - this.mChannelLayoutMargin));
    }

    private int getXPositionStart() {
        return getXFrom((System.currentTimeMillis() + ((long) getTimeShiftMilliSeconds())) - ((long) (HOURS_IN_VIEWPORT_MILLIS / 2)));
    }

    private void resetBoundaries() {
        this.mMillisPerPixel = calculateMillisPerPixel();
        this.mTimeOffset = calculatedBaseLine();
        this.mTimeLowerBoundary = getTimeFrom(0);
        this.mTimeUpperBoundary = getTimeFrom(getWidth());
    }

    private Rect calculateChannelsHitArea() {
        this.mMeasuringRect.top = this.mTimeBarHeight;
        int visibleChannelsHeight = this.epgData.getChannelCount() * (this.mChannelLayoutHeight + this.mChannelLayoutMargin);
        Rect rect = this.mMeasuringRect;
        if (visibleChannelsHeight >= getHeight()) {
            visibleChannelsHeight = getHeight();
        }
        rect.bottom = visibleChannelsHeight;
        this.mMeasuringRect.left = 0;
        this.mMeasuringRect.right = this.mChannelLayoutWidth;
        return this.mMeasuringRect;
    }

    private Rect calculateProgramsHitArea() {
        this.mMeasuringRect.top = this.mTimeBarHeight;
        int visibleChannelsHeight = this.epgData.getChannelCount() * (this.mChannelLayoutHeight + this.mChannelLayoutMargin);
        Rect rect = this.mMeasuringRect;
        if (visibleChannelsHeight >= getHeight()) {
            visibleChannelsHeight = getHeight();
        }
        rect.bottom = visibleChannelsHeight;
        this.mMeasuringRect.left = this.mChannelLayoutWidth;
        this.mMeasuringRect.right = getWidth();
        return this.mMeasuringRect;
    }

    private Rect calculateResetButtonHitArea() {
        this.mMeasuringRect.left = ((getScrollX() + getWidth()) - this.mResetButtonSize) - this.mResetButtonMargin;
        this.mMeasuringRect.top = ((getScrollY() + getHeight()) - this.mResetButtonSize) - this.mResetButtonMargin;
        this.mMeasuringRect.right = this.mMeasuringRect.left + this.mResetButtonSize;
        this.mMeasuringRect.bottom = this.mMeasuringRect.top + this.mResetButtonSize;
        return this.mMeasuringRect;
    }

    private int getChannelPosition(int y) {
        return this.epgData.getChannelCount() == 0 ? -1 : (this.mChannelLayoutMargin + (y - this.mTimeBarHeight)) / (this.mChannelLayoutHeight + this.mChannelLayoutMargin);
    }

    private int getProgramPosition(int channelPosition, long time) {
        List<EPGEvent> events = this.epgData.getEvents(channelPosition);
        if (events != null) {
            for (int eventPos = 0; eventPos < events.size(); eventPos++) {
                EPGEvent event = (EPGEvent) events.get(eventPos);
                if (event.getStart() <= time && event.getEnd() >= time) {
                    return eventPos;
                }
            }
        }
        return -1;
    }

    private EPGEvent getProgramAtTime(int channelPosition, long time) {
        List<EPGEvent> events = this.epgData.getEvents(channelPosition);
        if (events != null) {
            for (int eventPos = 0; eventPos < events.size(); eventPos++) {
                EPGEvent event = (EPGEvent) events.get(eventPos);
                if (event.getStart() <= time && event.getEnd() >= time) {
                    return event;
                }
            }
        }
        return null;
    }

    public void setEPGClickListener(EPGClickListener epgClickListener) {
        this.mClickListener = epgClickListener;
    }

    public void setEPGData(EPGData epgData) {
        this.epgData = mergeEPGData(this.epgData, epgData);
        if (this.epgData != null && this.epgData.getChannelCount() > 0) {
            this.epgDataFirstChannelID = this.epgData.getChannel(0);
            this.epgDataLastChannelID = this.epgData.getChannel(this.epgData.getChannelCount() - 1);
        }
    }

    private EPGData mergeEPGData(EPGData oldData, EPGData newData) {
        if (oldData == null) {
            try {
                oldData = new EPGDataImpl(Maps.<EPGChannel, List<EPGEvent>>newLinkedHashMap());
            } catch (Throwable e) {
                RuntimeException runtimeException = new RuntimeException("Could not merge EPG data: " + e.getClass().getSimpleName() + " " + e.getMessage(), e);
            }
        }
        if (newData != null) {
            for (int i = 0; i < newData.getChannelCount(); i++) {
                EPGChannel newChannel = newData.getChannel(i);
                EPGChannel oldChannel = oldData.getOrCreateChannel(newChannel.getName(), newChannel.getImageURL(), newChannel.getStreamID(), newChannel.getNum(), newChannel.getEpgChannelID());
                for (int j = 0; j < newChannel.getEvents().size(); j++) {
                    oldChannel.addEvent((EPGEvent) newChannel.getEvents().get(j));
                }
            }
        }
        return oldData;
    }

    public void recalculateAndRedraw(EPGEvent selectedEvent, boolean withAnimation, RelativeLayout epgFragment, EPG epg) {
        if (this.epgData != null && this.epgData.hasData()) {
            resetBoundaries();
            calculateMaxVerticalScroll();
            calculateMaxHorizontalScroll();
            Boolean eventsOccured = Boolean.valueOf(false);
            if (selectedEvent != null) {
                selectEvent(selectedEvent, withAnimation);
            } else if (getProgramPosition(0, getTimeFrom(getXPositionStart() + (getWidth() / 2))) != -1) {
                eventsOccured = Boolean.valueOf(true);
                selectEvent(this.epgData.getEvent(0, getProgramPosition(0, getTimeFrom(getXPositionStart() + (getWidth() / 2)))), withAnimation);
            } else if (this.epgData != null) {
                int i = 0;
                while (i < this.epgData.getChannelCount()) {
                    List<EPGEvent> thisEvents = this.epgData.getChannel(i).getEvents();
                    if (thisEvents == null || thisEvents.size() == 0) {
                        i++;
                    } else {
                        eventsOccured = Boolean.valueOf(true);
                        int selectedChannelID = this.epgData.getChannel(i).getChannelID();
                        int programPosition = getProgramPosition(selectedChannelID, getTimeFrom(getXPositionStart() + (getWidth() / 2)));
                        if (programPosition != -1) {
                            selectEvent(this.epgData.getEvent(selectedChannelID, programPosition), withAnimation);
                        }
                    }
                }
            }
            this.mScroller.startScroll(getScrollX(), getScrollY(), getXPositionStart() - getScrollX(), 0, withAnimation ? IjkMediaCodecInfo.RANK_LAST_CHANCE : 0);
            if (eventsOccured.equals(Boolean.valueOf(true)) && epgFragment != null) {
                epgFragment.setFocusable(true);
                epgFragment.setNextFocusDownId(R.id.epg);
            }
            redraw();
        }
    }

    public void redraw() {
        invalidate();
        requestLayout();
    }

    public void clearEPGImageCache() {
        this.mChannelImageCache.clear();
    }

    private void loadProgramDetails(EPGEvent epgEvent) {
        this.loginPreferencesSharedPref_time_format = this.context1.getSharedPreferences(AppConst.LOGIN_PREF_TIME_FORMAT, 0);
        String timeFormat = this.loginPreferencesSharedPref_time_format.getString(AppConst.LOGIN_PREF_TIME_FORMAT, "");
        this.programTimeFormatLong = new SimpleDateFormat(timeFormat);
        this.programTimeFormat = new SimpleDateFormat(timeFormat);
        if (!(this.loadProgramImage == null || this.loadProgramImage.getStatus() == Status.FINISHED)) {
            this.loadProgramImage.cancel(true);
        }
        this.loadProgramImage = new AsyncLoadProgramImage(this, epgEvent).execute(new EPGEvent[0]);
        this.currentEventTextView.setText(epgEvent.getTitle());
        this.currentEventTimeTextView.setText(this.programTimeFormatLong.format(Long.valueOf(epgEvent.getStart())) + " - " + this.programTimeFormat.format(Long.valueOf(epgEvent.getEnd())));
        this.currentEventDescriptionTextView.setText(epgEvent.getDesc());
    }

    public void selectEvent(EPGEvent epgEvent, boolean withAnimation) {
        if (this.selectedEvent != null) {
            this.selectedEvent.selected = false;
        }
        epgEvent.selected = true;
        this.selectedEvent = epgEvent;
        optimizeVisibility(epgEvent, withAnimation);
        loadProgramDetails(epgEvent);
        redraw();
    }

    public void setProgramImageView(ImageView aProgramImage) {
        programImage = aProgramImage;
        updateImageCropping(programImage);
    }

    public void setCurrentEventTextView(TextView currentEventTextView) {
        this.currentEventTextView = currentEventTextView;
    }

    public void setCurrentEventDescriptionTextView(TextView currentEventDescriptionTextView) {
        this.currentEventDescriptionTextView = currentEventDescriptionTextView;
    }

    public void setCurrentEventTimeTextView(TextView currentEventTimeTextView) {
        this.currentEventTimeTextView = currentEventTimeTextView;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
        DisplayMetrics dm = Resources.getSystem().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
    }

    private static void updateImageCropping(ImageView imageView) {
        Matrix matrix = new Matrix();
        float imageHeight = (float) imageView.getDrawable().getIntrinsicHeight();
        float scaleRatio = ((float) screenWidth) / ((float) imageView.getDrawable().getIntrinsicWidth());
        matrix.postScale(scaleRatio, scaleRatio);
        matrix.postTranslate(0.0f, (-1.0f * imageHeight) * 0.3f);
        imageView.setImageMatrix(matrix);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        this.mTimeLowerBoundary = getTimeFrom(getScrollX());
        this.mTimeUpperBoundary = getTimeFrom(getScrollX() + getWidth());
        long eventMiddleTime;
        if (!(event.getKeyCode() == 4 || this.selectedEvent == null)) {
            if (event.getKeyCode() == 22) {
                if (this.selectedEvent.getNextEvent() != null) {
                    this.selectedEvent.selected = false;
                    this.selectedEvent = this.selectedEvent.getNextEvent();
                    this.selectedEvent.selected = true;
                    optimizeVisibility(this.selectedEvent, true);
                }
            } else if (event.getKeyCode() == 21) {
                if (this.selectedEvent.getPreviousEvent() != null) {
                    this.selectedEvent.selected = false;
                    this.selectedEvent = this.selectedEvent.getPreviousEvent();
                    this.selectedEvent.selected = true;
                    optimizeVisibility(this.selectedEvent, true);
                }
            } else if (event.getKeyCode() == 19) {
                if (this.selectedEvent.getChannel().getPreviousChannel() == null || this.selectedEvent.getChannel().getPreviousChannel().getChannelID() == this.epgDataLastChannelID.getChannelID()) {
                    super.requestFocus();
                    super.requestFocusFromTouch();
                } else {
                    eventMiddleTime = (Math.max(this.mTimeLowerBoundary, this.selectedEvent.getStart()) + Math.min(this.mTimeUpperBoundary, this.selectedEvent.getEnd())) / 2;
                    EPGEvent previousChannelEvent = getProgramAtTime(this.selectedEvent.getChannel().getPreviousChannel().getChannelID(), eventMiddleTime);
                    if (previousChannelEvent != null) {
                        this.selectedEvent.selected = false;
                        this.selectedEvent = previousChannelEvent;
                        this.selectedEvent.selected = true;
                    } else {
                        checkPreviousChannel(this.selectedEvent.getChannel().getPreviousChannel().getChannelID(), eventMiddleTime);
                    }
                    optimizeVisibility(this.selectedEvent, true);
                }
            } else if (event.getKeyCode() == 20) {
                if (this.selectedEvent.getChannel().getNextChannel() != null) {
                    eventMiddleTime = (Math.max(this.mTimeLowerBoundary, this.selectedEvent.getStart()) + Math.min(this.mTimeUpperBoundary, this.selectedEvent.getEnd())) / 2;
                    EPGEvent nextChannelEvent = getProgramAtTime(this.selectedEvent.getChannel().getNextChannel().getChannelID(), eventMiddleTime);
                    if (nextChannelEvent != null) {
                        this.selectedEvent.selected = false;
                        this.selectedEvent = nextChannelEvent;
                        this.selectedEvent.selected = true;
                    } else {
                        checkNextChannel(this.selectedEvent.getChannel().getNextChannel().getChannelID(), eventMiddleTime);
                    }
                    optimizeVisibility(this.selectedEvent, true);
                }
            } else if (event.getKeyCode() == 103 || event.getKeyCode() == 90) {
                gotoNextDay(this.selectedEvent);
            } else if (event.getKeyCode() == 102 || event.getKeyCode() == 89) {
                gotoPreviousDay(this.selectedEvent);
            } else if (event.getKeyCode() == 66 || event.getKeyCode() == 23) {
                Context context = getContext();
                String str = AppConst.LOGIN_PREF_SELECTED_PLAYER;
                getContext();
                this.loginPreferencesSharedPref = context.getSharedPreferences(str, 0);
                EPGPlayPopUp(getContext(), this.loginPreferencesSharedPref.getString(AppConst.LOGIN_PREF_SELECTED_PLAYER, ""), Integer.parseInt(this.selectedEvent.getChannel().getStreamID()), this.selectedEvent.getChannel().getNum(), this.selectedEvent.getChannel().getName(), this.selectedEvent.getChannel().getEpgChannelID(), this.selectedEvent.getChannel().getImageURL());
            }
            loadProgramDetails(this.selectedEvent);
            redraw();
        }
        return true;
    }

    private void EPGPlayPopUp(Context context, String selectedPlayer, int streamID, String num, String name, String epgChannelId, String epgChannelLogo) {
        View layout = ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(R.layout.epg_popup_layout, (RelativeLayout) findViewById(R.id.rl_epg_layout));
        this.changeSortPopUp = new PopupWindow(context);
        this.changeSortPopUp.setContentView(layout);
        this.changeSortPopUp.setWidth(-1);
        this.changeSortPopUp.setHeight(-1);
        this.changeSortPopUp.setFocusable(true);
        this.changeSortPopUp.setBackgroundDrawable(new BitmapDrawable());
        this.changeSortPopUp.showAtLocation(layout, 17, 0, 0);
        RelativeLayout playLayout = (RelativeLayout) layout.findViewById(R.id.ll_play);
        RelativeLayout NextCatLayout = (RelativeLayout) layout.findViewById(R.id.ll_move_to_next_cat);
        RelativeLayout PrevLayout = (RelativeLayout) layout.findViewById(R.id.ll_move_to_prev_cat);
        RelativeLayout closeLayout = (RelativeLayout) layout.findViewById(R.id.ll_close);
        TabLayout tabhost = (TabLayout) getRootView().findViewById(R.id.sliding_tabs);
        int tabCount = 0;
        if (tabhost != null) {
            tabCount = tabhost.getTabCount();
        }
        if (!(tabhost == null || tabCount == 0)) {
            int selectedTabPosition = tabhost.getSelectedTabPosition();
            if (selectedTabPosition == tabCount - 1) {
                NextCatLayout.setVisibility(8);
            }
            if (selectedTabPosition == 0) {
                PrevLayout.setVisibility(8);
            }
        }
        final String str = selectedPlayer;
        final int i = streamID;
        final String str2 = num;
        final String str3 = name;
        final String str4 = epgChannelId;
        final String str5 = epgChannelLogo;
        playLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                EPG.this.changeSortPopUp.dismiss();
                Utils.playWithPlayer(EPG.this.getContext(), str, i, "live", str2, str3, str4, str5);
            }
        });
        final int finalTabCount = tabCount;
        TabLayout tabLayout = tabhost;
        final TabLayout finalTabLayout1 = tabLayout;
        NextCatLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                EPG.this.changeSortPopUp.dismiss();
                if (finalTabLayout1 != null && finalTabCount != 0) {
                    int selectedTabPosition = finalTabLayout1.getSelectedTabPosition();
                    if (selectedTabPosition != finalTabCount - 1) {
                        Tab tab = finalTabLayout1.getTabAt(selectedTabPosition + 1);
                        if (tab != null) {
                            tab.select();
                        }
                        finalTabLayout1.requestFocus();
                    }
                }
            }
        });
        tabLayout = tabhost;
        final TabLayout finalTabLayout = tabLayout;
        PrevLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                EPG.this.changeSortPopUp.dismiss();
                if (finalTabLayout != null && finalTabCount != 0) {
                    int selectedTabPosition = finalTabLayout.getSelectedTabPosition();
                    if (selectedTabPosition != 0) {
                        Tab tab = finalTabLayout.getTabAt(selectedTabPosition - 1);
                        if (tab != null) {
                            tab.select();
                        }
                        finalTabLayout.requestFocus();
                    }
                }
            }
        });
        closeLayout.setOnClickListener(new C17415());
    }

    private void checkPreviousChannel(int previousChannelID, long eventMiddleTime) {
        int previousMinusOne = previousChannelID - 1;
        if (previousMinusOne >= 0) {
            EPGEvent previousChannelEvent = getProgramAtTime(previousMinusOne, eventMiddleTime);
            if (previousChannelEvent != null) {
                this.selectedEvent.selected = false;
                this.selectedEvent = previousChannelEvent;
                this.selectedEvent.selected = true;
                return;
            }
            checkPreviousChannel(previousMinusOne, eventMiddleTime);
            return;
        }
        super.requestFocus();
    }

    private void checkNextChannel(int nextChannelID, long eventMiddleTime) {
        int nextPlusOne = nextChannelID + 1;
        EPGChannel lastChannelID = this.epgDataLastChannelID;
        if (nextChannelID == lastChannelID.getChannelID()) {
            nextPlusOne = 0;
        }
        if (nextPlusOne < 0 || nextPlusOne > lastChannelID.getChannelID()) {
            super.requestFocus();
            return;
        }
        EPGEvent nextChannelEvent = getProgramAtTime(nextPlusOne, eventMiddleTime);
        if (nextChannelEvent != null) {
            this.selectedEvent.selected = false;
            this.selectedEvent = nextChannelEvent;
            this.selectedEvent.selected = true;
            return;
        }
        checkNextChannel(nextPlusOne, eventMiddleTime);
    }

    private void gotoPreviousDay(EPGEvent currentEvent) {
    }

    private void gotoNextDay(EPGEvent currentEvent) {
    }

    public void optimizeVisibility(EPGEvent epgEvent, boolean withAnimation) {
        int dX = 0;
        int dY = 0;
        int minYVisible = getScrollY();
        int maxYVisible = minYVisible + getHeight();
        int currentChannelTop = this.mTimeBarHeight + ((this.mChannelLayoutHeight + this.mChannelLayoutMargin) * epgEvent.getChannel().getChannelID());
        int currentChannelBottom = currentChannelTop + this.mChannelLayoutHeight;
        if (currentChannelTop < minYVisible) {
            dY = (currentChannelTop - minYVisible) - this.mTimeBarHeight;
        } else if (currentChannelBottom > maxYVisible) {
            dY = currentChannelBottom - maxYVisible;
        }
        this.mTimeLowerBoundary = getTimeFrom(getScrollX());
        this.mTimeUpperBoundary = getTimeFrom(getScrollX() + getProgramAreaWidth());
        if (epgEvent.getEnd() > this.mTimeUpperBoundary) {
            dX = Math.round((float) ((((this.mTimeUpperBoundary - epgEvent.getEnd()) - this.mMargin) * -1) / this.mMillisPerPixel));
        }
        this.mTimeLowerBoundary = getTimeFrom(getScrollX());
        this.mTimeUpperBoundary = getTimeFrom(getScrollX() + getWidth());
        if (epgEvent.getStart() < this.mTimeLowerBoundary) {
            dX = Math.round((float) (((this.selectedEvent.getStart() - this.mTimeLowerBoundary) - this.mMargin) / this.mMillisPerPixel));
        }
        if (dX != 0 || dY != 0) {
            this.mScroller.startScroll(getScrollX(), getScrollY(), dX, dY, withAnimation ? IjkMediaCodecInfo.RANK_LAST_CHANCE : 0);
        }
    }
}
