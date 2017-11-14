package com.dany.clock;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Arrays;
import java.util.Calendar;

/**
 * Created by dan.y on 2017/11/10 16:15.
 */

public class ClockView extends View {
    private Paint mOutCirclePaint;//外部装饰圆盘画笔
    private int mOutCircleStrokeWidth;
    private Paint mCirclePaint;//圆盘画笔
    private int mCircleX;
    private int mCircleY;
    private int mCircleRadius;
    private int mCircleStrokeWidth;
    private Paint mTScalePaint;//十二分刻度画笔
    private int mTScaleWidth;
    private int mTScaleLength;
    private Paint mTScaleTextPaint;//十二点画笔
//    private String[] tScales = new String[]{"3","4","5","6","7","8","9","10","11","12","1","2"};
    private String[] tScales = new String[]{"Ⅲ","Ⅳ","Ⅴ","Ⅵ","Ⅶ","Ⅷ","Ⅸ","Ⅹ","Ⅺ","Ⅻ","Ⅰ","Ⅱ"};
    private Paint mSScalePaint;//六十分刻度画笔
    private int mSScaleStrokeWidth;
    private Paint mCenterPointPaint;//指针中心点画笔
    private int mPointRadius;
    private Paint mSecondPaint;//秒针画笔
    private int mSecondLineWidth;
    private Paint mMinutePaint;//分针画笔
    private int mMinuteLineWidth;
    private Paint mHourPaint;//时针画笔
    private int mHourLineWidth;
    private Paint mLogoPaint;
    private String logo = "fiona'ck";

    private float[] times;
    private float[] degrees;

    private final static int MSG_SECOND = 0x001;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_SECOND:
                    getCurrentTime();
                    postInvalidate();
                    mHandler.sendEmptyMessageDelayed(MSG_SECOND,1000);
                    break;
            }
        }
    };

    public ClockView(Context context) {
        this(context,null);
    }

    public ClockView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ClockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.setLayerType(LAYER_TYPE_SOFTWARE,null);
        mOutCircleStrokeWidth = 1;
        mOutCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mOutCirclePaint.setColor(Color.parseColor("#e0e0e0"));
        mOutCirclePaint.setStyle(Paint.Style.STROKE);
        mOutCirclePaint.setStrokeWidth(mOutCircleStrokeWidth);
        mCircleStrokeWidth = 3;
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setColor(Color.parseColor("#7fe4b999"));
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(mCircleStrokeWidth);
        mCirclePaint.setMaskFilter(new BlurMaskFilter(mCircleStrokeWidth*4, BlurMaskFilter.Blur.SOLID));

        mTScaleWidth = 6;
        mTScalePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTScalePaint.setColor(Color.BLACK);
        mTScalePaint.setStyle(Paint.Style.STROKE);
        mTScalePaint.setStrokeWidth(mTScaleWidth);
        mTScaleLength = 15;


        mTScaleTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTScaleTextPaint.setColor(Color.BLACK);
        mTScaleTextPaint.setStyle(Paint.Style.FILL);
        mTScalePaint.setTextAlign(Paint.Align.CENTER);
        mTScaleTextPaint.setTextSize(32);
        mTScaleTextPaint.setFakeBoldText(true);//加粗

        mSScaleStrokeWidth = 3;
        mSScalePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSScalePaint.setColor(Color.parseColor("#7fe4b999"));
        mSScalePaint.setStyle(Paint.Style.STROKE);
        mSScalePaint.setStrokeWidth(mSScaleStrokeWidth);

        mCenterPointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCenterPointPaint.setColor(Color.parseColor("#e4b999"));
        mCenterPointPaint.setStyle(Paint.Style.FILL);
        mPointRadius = 10;

        mSecondLineWidth = 5;
        mSecondPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSecondPaint.setColor(Color.parseColor("#e4b999"));
        mSecondPaint.setStyle(Paint.Style.FILL);
        mSecondPaint.setStrokeWidth(mSecondLineWidth);

        mMinuteLineWidth = 10;
        mMinutePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMinutePaint.setColor(Color.BLACK);
        mMinutePaint.setStyle(Paint.Style.FILL);
        mMinutePaint.setStrokeWidth(mMinuteLineWidth);
        mMinutePaint.setStrokeCap(Paint.Cap.ROUND);

        mHourLineWidth = 15;
        mHourPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHourPaint.setColor(Color.BLACK);
        mHourPaint.setStyle(Paint.Style.FILL);
        mHourPaint.setStrokeWidth(mHourLineWidth);
        mHourPaint.setStrokeCap(Paint.Cap.ROUND);

        mLogoPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLogoPaint.setColor(Color.parseColor("#e4b999"));
        mLogoPaint.setStyle(Paint.Style.FILL);
        mLogoPaint.setTextAlign(Paint.Align.CENTER);
        mLogoPaint.setTextSize(32);
        mLogoPaint.setFakeBoldText(true);//加粗

        getCurrentTime();
        mHandler.sendEmptyMessageDelayed(MSG_SECOND,1000);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mCircleX = getWidth()/2;
        mCircleY = getHeight()/2;
        mCircleRadius = Math.min(mCircleX-2*mTScaleLength,mCircleY-2*mTScaleLength);
        canvas.drawCircle(mCircleX,mCircleY,mCircleRadius,mCirclePaint);
        canvas.drawCircle(mCircleX,mCircleY,mCircleRadius + mTScaleLength,mOutCirclePaint);

        Rect bounds0 = new Rect();
        String text0 = logo;
        mLogoPaint.getTextBounds(text0, 0, text0.length(), bounds0);
        Paint.FontMetricsInt fontMetrics0 = mLogoPaint.getFontMetricsInt();
        int tx0 = mCircleX;
        int ty0 = mCircleY + (int) ((mCircleRadius - mTScaleLength*10) * Math.sin( -90 * Math.PI/180));
        int baseline0 = ty0 - (fontMetrics0.bottom + fontMetrics0.top) / 2;
        canvas.drawText(text0,tx0, baseline0, mLogoPaint);

        for (int i=0;i<60;i++){//60分 60*6
            int degree = i * 6;
            int ex = mCircleX + (int) (mCircleRadius * Math.cos(degree * Math.PI/180));
            int ey = mCircleY + (int) (mCircleRadius * Math.sin(degree * Math.PI/180));
            int sx = mCircleX + (int) ((mCircleRadius - mTScaleLength) * Math.cos(degree * Math.PI/180));
            int sy = mCircleY + (int) ((mCircleRadius - mTScaleLength) * Math.sin(degree * Math.PI/180));
            if(degree % 30 != 0){
                canvas.drawLine(sx,sy,ex,ey,mSScalePaint);
            }else{//360均12分
                canvas.drawLine(sx,sy,ex,ey,mTScalePaint);
                int tx = mCircleX + (int) ((mCircleRadius - mTScaleLength*3) * Math.cos( degree * Math.PI/180));
                int ty = mCircleY + (int) ((mCircleRadius - mTScaleLength*3) * Math.sin( degree * Math.PI/180));

                Rect bounds = new Rect();
                String text = tScales[degree/30];
                mTScaleTextPaint.getTextBounds(text, 0, text.length(), bounds);
                Paint.FontMetricsInt fontMetrics = mTScaleTextPaint.getFontMetricsInt();
                int baseline = ty - (fontMetrics.bottom + fontMetrics.top) / 2;
                canvas.drawText(text,tx - bounds.width() / 2, baseline, mTScaleTextPaint);
            }
        }


        int startX = mCircleX;
        int startY = mCircleY;
        int startSX = mCircleX - (int) ((mTScaleLength*2) * Math.cos(degrees[0] * Math.PI/180));
        int startSY = mCircleY - (int) ((mTScaleLength*2) * Math.sin(degrees[0] * Math.PI/180));
        int endSX = mCircleX + (int) ((mCircleRadius) * Math.cos(degrees[0] * Math.PI/180));
        int endSY = mCircleY + (int) ((mCircleRadius) * Math.sin(degrees[0] * Math.PI/180));
        int endMX = mCircleX + (int) ((mCircleRadius - mTScaleLength*2) * Math.cos(degrees[1] * Math.PI/180));
        int endMY = mCircleY + (int) ((mCircleRadius - mTScaleLength*2) * Math.sin(degrees[1] * Math.PI/180));
        int endHX = mCircleX + (int) ((mCircleRadius - mTScaleLength*4) * Math.cos(degrees[2] * Math.PI/180));
        int endHY = mCircleY + (int) ((mCircleRadius - mTScaleLength*4) * Math.sin(degrees[2] * Math.PI/180));
        canvas.drawLine(startX,startY,endHX,endHY,mHourPaint);
        canvas.drawLine(startX,startY,endMX,endMY,mMinutePaint);
        canvas.drawLine(startSX,startSY,endSX,endSY,mSecondPaint);

        canvas.drawCircle(mCircleX,mCircleY,mPointRadius,mCenterPointPaint);
    }

    private void getCurrentTime(){
        times = new float[3];
        degrees = new float[3];
        Calendar calendar = Calendar.getInstance();
        times[0] = calendar.get(Calendar.SECOND);
        times[1] = calendar.get(Calendar.MINUTE);
        times[2] = calendar.get(Calendar.HOUR);
        degrees[0] = times[0] * 6 - 90;//一圈60秒；360的60等分
        degrees[1] = (times[1] + times[0]/60) * 6 - 90;//一圈60分；360的60等分
        degrees[2] = (times[2] + times[1]/60 + times[0]/3600) * 30 - 90;//一圈12时；360的12等分
        Log.d("dan.y","getCurrentSeconds: " + Arrays.toString(times));
    }



}
