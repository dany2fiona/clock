package com.dany.circleringview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by dan.y on 2017/10/30 10:49.
 */

public class CircleRingView extends View {
    private Paint mRingPaint;//圆弧paint..
    private Paint mSwipePaint;//进度圆弧paint..
    private Paint mPointPaint;//圆弧上的圆paint..
    private Paint mTextPaint;//进度值paint..
    private Paint mPercentPaint;//百分比paint..
    private float mCircleX;//中心点
    private float mCircleY;
    private int mArcDis;//view magin位置
    private double mSwipeAngle = 45;
    private double mPointAngle;
//    private int curr = 100;      ..test.所有百分比.

    private final static int MSG_DRAW_CIRCLE_RING = 0x001;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(mSwipeAngle == mPointAngle){
                mHandler.removeMessages(MSG_DRAW_CIRCLE_RING);
                //测试所有百分比显示是否正常..
//                mSwipeAngle = 45;
//                mPointAngle = 0;
//                if(curr >0){
//                    curr --;
//                    try {
//                        Thread.sleep(200);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    setProgress(curr);
//                }else{
//                    mHandler.removeMessages(MSG_DRAW_CIRCLE_RING);
//                }
            }else{
                mSwipeAngle = Arith.add(mSwipeAngle,2.7d);
                postInvalidate();
            }
        }
    };


    public CircleRingView(Context context) {
        this(context,null);
    }

    public CircleRingView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CircleRingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mRingPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRingPaint.setStyle(Paint.Style.STROKE);
        mRingPaint.setStrokeWidth(UnitUtils.dip2px(getContext(),3));
        mRingPaint.setColor(Color.WHITE);
        mSwipePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSwipePaint.setStrokeWidth(UnitUtils.dip2px(getContext(),3));
        mSwipePaint.setStyle(Paint.Style.STROKE);
        mSwipePaint.setColor(Color.CYAN);
        mPointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPointPaint.setStyle(Paint.Style.FILL);
        mPointPaint.setStrokeWidth(UnitUtils.dip2px(getContext(),1));
        mPointPaint.setColor(Color.GREEN);
        mArcDis = UnitUtils.dip2px(getContext(),20);
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setStrokeWidth(UnitUtils.dip2px(getContext(),1));
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setColor(Color.GREEN);
        mTextPaint.setTextSize(UnitUtils.sp2px(getContext(),40));
        mPercentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPercentPaint.setStrokeWidth(UnitUtils.dip2px(getContext(),1));
        mPercentPaint.setStyle(Paint.Style.FILL);
        mPercentPaint.setColor(Color.GREEN);
        mPercentPaint.setTextSize(UnitUtils.sp2px(getContext(),25));

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        RectF rectF = new RectF();
        rectF.left = mArcDis;
        rectF.top = mArcDis;
        rectF.right = width - mArcDis;
        rectF.bottom = height - mArcDis;
        canvas.rotate(90,width/2,height/2);
        canvas.drawArc(rectF,45,270,false,mRingPaint);
//        if(mSwipeAngle >=0 && mSwipeAngle <= 45){
//            mSwipeAngle = 45;
//            mPointAngle = 45;
//        }else if(mSwipeAngle > 315 && mSwipeAngle <=360){
//            mSwipeAngle = 315;
//            mPointAngle = 315;
//        }
        mCircleX = width/2;
        mCircleY = height/2;
        if(mSwipeAngle <= mPointAngle){
            float radius = (float) ((width - mArcDis - mArcDis) / 2);
            float pointX = (float) (mCircleX + radius*Math.cos(mSwipeAngle*Math.PI/180));
            float pointY = (float) (mCircleY + radius*Math.sin(mSwipeAngle*Math.PI/180));
            //进度圆弧。模拟进度条
            canvas.drawArc(rectF,45, (float) (mSwipeAngle - 45),false,mSwipePaint);
            //圆弧上的圆
            canvas.drawCircle(pointX, pointY, UnitUtils.dip2px(getContext(), 10), mPointPaint);
            //文本
            canvas.rotate(-90, width / 2, height / 2);
            String text = String.valueOf((int)Math.floor(Arith.mul(Arith.div(Arith.sub(mSwipeAngle,45),270) , 100)));
            float v = mTextPaint.measureText(text);
            canvas.drawText(text,mCircleX - (int)v/2,mCircleY + UnitUtils.dip2px(getContext(),10),mTextPaint);
            canvas.drawText("%",mCircleX+(int)v/2,mCircleY+UnitUtils.dip2px(getContext(),10),mPercentPaint);
            mHandler.sendEmptyMessageDelayed(MSG_DRAW_CIRCLE_RING, 10);
        }

    }

    //需要画的进度值
    public void setProgress(int value){
//        int swipeAngle = (int) (((float)value/100) * 360);
//        curr = value;
        double swipeAngle =  Arith.add(Arith.mul(Arith.div((double) value,100) , 270) ,45);
        mPointAngle = swipeAngle;
        mHandler.sendEmptyMessageDelayed(MSG_DRAW_CIRCLE_RING,10);
    }


}
