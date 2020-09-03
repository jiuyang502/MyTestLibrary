package com.pannygirlstudio.necly.testlibrary.View;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

public class DataShowView_Circle extends View {

    public static final int TextType_String = 0;
    public static final int TextType_Int = 1;
    public static final int TextType_Float = 2;

    private Context mContext;

    private int mInnerRoundColor = 0xffe4e4e4;          // 内圆环颜色 背景圆环
    private int mOuterRoundColor = 0xff4fd8f5;          // 外圆环颜色 进度圆环
    private int mOuterGradientColor = 0xff5e9fff;      // 外圆环渐变的颜色
    private int mMainTextColor   = 0xff56a9ff;          // 主文本字体颜色
    private int mExtraTextColor  = 0xff56a9ff;          // 附加文本字体颜色
    //private int mExtraTextColor  = 0xff56a9ff;            // 附加文本字体颜色

    private Paint mPaint;                                 // 绘制背景圆环的画笔
    private Paint mProgressPaint;                        // 绘制外面进度的圆环的画笔
    private Paint mMainTextPaint;                        // 绘制主文本文字的画笔
    private Paint mExtraTextPaint;                       // 绘制附加文本文字的画笔

    private int mRoundWidth = 10;                        // 背景圆弧的绘制的宽度
    private int mProgressRoundWidth =  15;              // 进度圆环的宽度

    private int mMainTextSize = 20;                      // 主文本文字的大小 单位 sp
    private int mExtraTextSize = 10;                     // 附加文本文字大小 单位 sp

    private int mTextType_MainText = TextType_Float;  //主文本数据类型
    private int mTextType_ExtraText = TextType_Float; //附加文本数据类型

    private String mMainText = "0";                       // 主文本文字
    private String mExtraText = "0";                      // 附加文本文字
    private float mMainTextData = 0.00f;                  // 主文本数据
    private float mExtraTextData = 0.00f;                 // 附加文本数据
    private String mMainTextUnit  = "";                   // 主文本文字单位
    private String mExtraTextUnit = "";                   // 附加文本文字单位

    private boolean mIsExtraTextAbove = true;           // 附加文本是否在主文字上方 true 在上方，false在下方

    //private int mMaxStep = 10000;                         // 圆环最大进度
    private float mMaxStep = 10000.0f;                         // 圆环最大进度
    private float  mCurrentStep = 0;                     // 圆环当前进度

    private float mTextPadding = 16;                     //附加文本与主文本文字之间的间隔
    private float mTextMoveSize = 16;                    //主文本主动向下移动的像素量

    
    private boolean mIsNeedDraw_Circle = true;         // 是否需要重绘圆环标志 ，设置为false就是不绘制
    private boolean mIsNeedDraw_MainText = true;      // 是否需要重绘主文本标志，设置为false就是不绘制
    private boolean mIsNeedDraw_ExtraText = true;     // 是否需要重绘主文本标志，设置为false就是不绘制

    private boolean mIsNeedShowSymbol_MainText = true;      //是否需要给主文本绘制正数符号 ：“+”
    private boolean mIsNeedShowSymbol_ExtraText = true;     //是否需要给附件文本绘制正数符号 ：“+”

    private int mDecimalDigit_MainText = 1;             //主文本小数位数
    private int mDecimalDigit_ExtraText = 1;            //附件文本小数位数

    public DataShowView_Circle(Context context) {
        this(context, null);
    }
    public DataShowView_Circle(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public DataShowView_Circle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;

//        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SportStepView);
//        innerRoundColor = typedArray.getColor(R.styleable.SportStepView_innerRoundColor, ContextCompat.getColor(mContext, R.color.color_e4e4e4));
//        outerRoundColor = typedArray.getColor(R.styleable.SportStepView_outerRoundColor, ContextCompat.getColor(mContext, R.color.color_4fd8f5));


        InitPaint();
    }

    private void InitPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);                                  // 设置是否使用抗锯齿功能，会消耗较大资源，绘制图形速度会变慢。,true为无锯齿,即抗锯齿
        mPaint.setStyle(Paint.Style.STROKE);                        // 设置画笔的样式，为FILL，FILL_OR_STROKE，或STROKE 空心或者实心 （STROKE是设置为描边属性？？？）
        mPaint.setColor(mInnerRoundColor);                        //  设置绘制的颜色，使用颜色值来表示，该颜色值包括透明度和RGB颜色
        mPaint.setStrokeCap(Paint.Cap.ROUND);                       //  圆形笔头 即结束的地方是圆形的，好看点
        mPaint.setStrokeWidth(mRoundWidth);                        //  当画笔样式为STROKE或FILL_OR_STROKE时，设置笔刷的粗细度

        mProgressPaint = new Paint();
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setStyle(Paint.Style.STROKE);
        mProgressPaint.setColor(mOuterRoundColor);
        mProgressPaint.setStrokeCap(Paint.Cap.ROUND);
        mProgressPaint.setStrokeWidth(mProgressRoundWidth);

        mMainTextPaint = new Paint();
        mMainTextPaint.setAntiAlias(true);
        mMainTextPaint.setStyle(Paint.Style.STROKE);
        mMainTextPaint.setColor(mMainTextColor);
        mMainTextPaint.setTextSize(sp2px(mMainTextSize));

        mExtraTextPaint = new Paint();
        mExtraTextPaint.setAntiAlias(true);
        mExtraTextPaint.setStyle(Paint.Style.STROKE);
        mExtraTextPaint.setColor(mExtraTextColor);
        mExtraTextPaint.setTextSize(sp2px(mExtraTextSize));
    }
    private void ReloadPaintParam() {

        mPaint.setAntiAlias(true);                                  // 设置是否使用抗锯齿功能，会消耗较大资源，绘制图形速度会变慢。,true为无锯齿,即抗锯齿
        mPaint.setStyle(Paint.Style.STROKE);                        // 设置画笔的样式，为FILL，FILL_OR_STROKE，或STROKE 空心或者实心 （STROKE是设置为描边属性？？？）
        mPaint.setColor(mInnerRoundColor);                        //  设置绘制的颜色，使用颜色值来表示，该颜色值包括透明度和RGB颜色
        mPaint.setStrokeCap(Paint.Cap.ROUND);                       //  圆形笔头 即结束的地方是圆形的，好看点
        mPaint.setStrokeWidth(mRoundWidth);                        //  当画笔样式为STROKE或FILL_OR_STROKE时，设置笔刷的粗细度

        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setStyle(Paint.Style.STROKE);
        mProgressPaint.setColor(mOuterRoundColor);
        mProgressPaint.setStrokeCap(Paint.Cap.ROUND);
        mProgressPaint.setStrokeWidth(mProgressRoundWidth);

        mMainTextPaint.setAntiAlias(true);
        mMainTextPaint.setStyle(Paint.Style.STROKE);
        mMainTextPaint.setColor(mMainTextColor);
        mMainTextPaint.setTextSize(sp2px(mMainTextSize));

        mExtraTextPaint.setAntiAlias(true);
        mExtraTextPaint.setStyle(Paint.Style.STROKE);
        mExtraTextPaint.setColor(mExtraTextColor);
        mExtraTextPaint.setTextSize(sp2px(mExtraTextSize));
    }

    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取宽的模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        //获取宽的尺寸
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        //对wrap_content这种模式进行处理
        if (heightMode == MeasureSpec.AT_MOST) {
            heightSize = widthSize;
        }
        //绘制圆环以宽度为标准，保存丈量结果
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas)                                                          // 背景圆环+进度圆环的效果
    {                                                                                               // 利用drawCircle绘制背景圆环，drawArc()绘制进度圆环
        super.onDraw(canvas);

        ReloadPaintParam();
        if (mIsNeedDraw_Circle)
            onDrawCircle(canvas);
        if (mIsNeedDraw_MainText)
            onDrawMainText(canvas);
        if (mIsNeedDraw_ExtraText)
            onDrawExtraText(canvas);

    }

    private void onDrawCircle(Canvas canvas)
    {
        int m_TempWidth = mRoundWidth;
        if (m_TempWidth<mProgressRoundWidth)
            m_TempWidth = mProgressRoundWidth;

        mPaint.setStrokeWidth(m_TempWidth);                                                         //Necly自己添加的，不然还是会有 没完全覆盖的情况
        mProgressPaint.setStrokeWidth(m_TempWidth);                                                //Necly自己添加的，不然还是会有 没完全覆盖的情况


        /**
         *  处理渐变色
         */
        //默认的渐变颜色数组:
        int[] mGradientColorArray = new int[]{mOuterRoundColor, mOuterRoundColor,mOuterGradientColor, mOuterRoundColor};
        int count = mGradientColorArray.length;

        float[] positions = new float[count];
        float v = (360f / 270);
        positions[0] = 0.0f;
        positions[1] = 0.33f * v;
        positions[2] = 0.67f * v;
        positions[3] = 1.0f;

        SweepGradient shader = new SweepGradient(getWidth() / 2 - m_TempWidth / 2, getWidth() / 2 - m_TempWidth / 2, mGradientColorArray, positions);
        mProgressPaint.setShader(shader);




        //*************** 绘制两个圆环
        RectF oval = new RectF(0 + m_TempWidth / 2, 0 + m_TempWidth / 2, getWidth() - m_TempWidth / 2, getWidth() - m_TempWidth / 2);
        canvas.drawArc(oval, 135, 270, false, mPaint);
//        if (mCurrentStep>mMaxStep)
//            mCurrentStep = mMaxStep;
        canvas.drawArc(oval, 135, mCurrentStep * 1f / mMaxStep * 270, false, mProgressPaint);

    }
    private void onDrawMainText(Canvas canvas)
    {
        //*************** 绘制中间的主文本文字
        Rect mTempMainTextRect = new Rect();
        String mTempMainText = mMainText + mMainTextUnit;
        if (mTextType_MainText == TextType_Int) {
            if (mMainTextData>0)
                mTempMainText = (int)(mMainTextData + 0.5f) + mMainTextUnit;
            else
                mTempMainText = (int)(mMainTextData - 0.5f) + mMainTextUnit;

            if (mMainTextData>0 && mIsNeedShowSymbol_MainText)
                mTempMainText = "+" + mTempMainText;
        }
        else if (mTextType_MainText == TextType_Float) {
            if (mDecimalDigit_MainText==1)
                mTempMainText = String.format("%.1f", mMainTextData) + mMainTextUnit;
           else if (mDecimalDigit_MainText==2)
                mTempMainText = String.format("%.2f", mMainTextData) + mMainTextUnit;
            else if (mDecimalDigit_MainText==3)
                mTempMainText = String.format("%.3f", mMainTextData) + mMainTextUnit;
            else
                mTempMainText = String.format("%.2f", mMainTextData) + mMainTextUnit;

            if (mMainTextData > 0 && mIsNeedShowSymbol_MainText)
                mTempMainText = "+" + mTempMainText;
        }

        mMainTextPaint.setTextSize(sp2px(mMainTextSize));
        mMainTextPaint.getTextBounds(mTempMainText, 0, mTempMainText.length(), mTempMainTextRect);

        canvas.drawText(mTempMainText, getWidth() / 2 - mTempMainTextRect.width() / 2,
                                       getHeight() / 2 + mTempMainTextRect.height() / 2 + mTextMoveSize, mMainTextPaint);              //主动向下移动了mTextMoveSize个像素

    }
    private void onDrawExtraText(Canvas canvas)
    {
        //*************** 绘制中间的主文本文字
        Rect mTempMainTextRect = new Rect();
        String mShowText = "000" + mMainTextUnit;
        mMainTextPaint.setTextSize(sp2px(mMainTextSize));
        mMainTextPaint.getTextBounds(mShowText, 0, mShowText.length(), mTempMainTextRect);
        
        
        //*************** 绘制附加文本，在主文本的上方或者下方
        Rect mTempExtraTextRect = new Rect();
        String mTempExtraText = mExtraText + mExtraTextUnit;

        if (mTextType_ExtraText == TextType_Int) {
            if (mExtraTextData>0)
                mTempExtraText = (int)(mExtraTextData + 0.5f) + mExtraTextUnit;
            else
                mTempExtraText = (int)(mExtraTextData - 0.5f) + mExtraTextUnit;

            if (mExtraTextData>0 && mIsNeedShowSymbol_ExtraText)
                mTempExtraText = "+" + mTempExtraText;
        }
        else if (mTextType_ExtraText == TextType_Float) {

            if (mDecimalDigit_ExtraText==1)
                mTempExtraText = String.format("%.1f", mExtraTextData) + mExtraTextUnit;
            else if (mDecimalDigit_ExtraText==2)
                mTempExtraText = String.format("%.2f", mExtraTextData) + mExtraTextUnit;
            else if (mDecimalDigit_ExtraText==3)
                mTempExtraText = String.format("%.3f", mExtraTextData) + mExtraTextUnit;
            else
                mTempExtraText = String.format("%.2f", mExtraTextData) + mExtraTextUnit;

            if (mExtraTextData > 0 && mIsNeedShowSymbol_ExtraText)
                mTempExtraText = "+" + mTempExtraText;
        }
        
        
        mExtraTextPaint.setTextSize(sp2px(mExtraTextSize));
        mExtraTextPaint.getTextBounds(mTempExtraText, 0, mTempExtraText.length(), mTempExtraTextRect);
        
       
        if (mIsExtraTextAbove)
            canvas.drawText(mTempExtraText, getWidth() / 2 - mTempExtraTextRect.width() / 2,
                                            getHeight() / 2 + mTempExtraTextRect.height() / 2 - (mTempMainTextRect.height() + mTextPadding) +mTextMoveSize, mExtraTextPaint);  //主动向下移动了16个像素
        else
            canvas.drawText(mTempExtraText, getWidth() / 2 - mTempExtraTextRect.width() / 2,
                                            getHeight() / 2 + mTempExtraTextRect.height() / 2 + (mTempMainTextRect.height() + mTextPadding) +mTextMoveSize, mExtraTextPaint);

    }

    /**
     * 将sp转换成px
     *
     * @param sp
     * @return
     */
    private int sp2px(int sp)
    {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }


    //************************* 开始绘制圆环
    public void StartAnimation_Circle(float StartStep, float finalStep,int Duration) {
        //方法一：开一个分线程，动态改变进度的值，不断绘制达到进度变化的效果
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                setCurrentStep(0);
//                float changeProgress = currentStep;
//                for (float i = 0; i < changeProgress; i++) {
//                    setCurrentStep(getCurrentStep() + rate);
//                    SystemClock.sleep(20);
////                  invalidate();//invalidate()必须在主线程中执行，此处不能使用
////                  postInvalidate();//强制重绘，postInvalidate()可以在主线程也可以在分线程中执行
//                    changeProgress = changeProgress - rate;
//                }
//                //由于上面的循环结束时，可能计算后最终无法到达mCurrentProgress的值，所以在循环结束后，将mCurrentProgress重新设置
//                setCurrentStep(currentStep);
//            }
//        }).start();


       // ************************ 方法二 使用属性动画和差值器实现

        mIsNeedDraw_Circle = true;
        if (finalStep>mMaxStep)
            finalStep = mMaxStep;

        ValueAnimator valueAnimator = ObjectAnimator.ofFloat(StartStep, finalStep);
        valueAnimator.setDuration(Duration);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentStep = (float) animation.getAnimatedValue();
                SetCurrentStep( currentStep);

                postInvalidate();                   //强制重绘，postInvalidate()可以在主线程也可以在分线程中执行
            }
        });
        valueAnimator.start();
    }
    //************************* 开始绘制圆环 直接绘制
    public void SetCircle_Directly() {
        SetCurrentStep(270);
        SetMaxStep(270);

        mIsNeedDraw_Circle = true;
        postInvalidate();
    }

    //************************* 开始绘制主文本 Int型
    public void StartAnimation_MainText(int Data_Old, int Data_New,int Duration) {
        mIsNeedDraw_MainText = true;

        ValueAnimator valueAnimator = ObjectAnimator.ofFloat(Data_Old, Data_New);
        valueAnimator.setDuration(Duration);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentData = (float) animation.getAnimatedValue();

                SetTextType_MainText(TextType_Int);
                SetMainTextData(currentData);

                postInvalidate();                   //强制重绘，postInvalidate()可以在主线程也可以在分线程中执行
            }
        });
        valueAnimator.start();
    }
    //************************* 开始绘制主文本 Float型
    public void StartAnimation_MainText(float Data_Old, float Data_New,int Duration) {
        mIsNeedDraw_MainText = true;

        ValueAnimator valueAnimator = ObjectAnimator.ofFloat(Data_Old, Data_New);
        valueAnimator.setDuration(Duration);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentData = (float) animation.getAnimatedValue();

                SetTextType_MainText(TextType_Float);
                SetMainTextData(currentData);

                postInvalidate();                   //强制重绘，postInvalidate()可以在主线程也可以在分线程中执行
            }
        });
        valueAnimator.start();
    }
    //************************* 开始绘制主文本 String型 直接绘制
    public void SetMainText_Directly(String  mainText)
    {
        SetTextType_MainText(TextType_String);
        SetMainText(mainText);

        mIsNeedDraw_MainText = true;
        postInvalidate();
    }


    //************************* 开始绘制附加文本 Int型
    public void StartAnimation_ExtraText(int Data_Old, int Data_New,int Duration) {

        mIsNeedDraw_ExtraText = true;

        ValueAnimator valueAnimator = ObjectAnimator.ofFloat(Data_Old, Data_New);
        valueAnimator.setDuration(Duration);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentData = (float) animation.getAnimatedValue();

                SetTextType_ExtraText(TextType_Int);
                SetExtraTextData(currentData);

                postInvalidate();                   //强制重绘，postInvalidate()可以在主线程也可以在分线程中执行
            }
        });
        valueAnimator.start();
    }
    //************************* 开始绘制附加文本 Float型
    public void StartAnimation_ExtraText(float Data_Old, float Data_New,int Duration) {

        mIsNeedDraw_ExtraText = true;

        ValueAnimator valueAnimator = ObjectAnimator.ofFloat(Data_Old, Data_New);
        valueAnimator.setDuration(Duration);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentData = (float) animation.getAnimatedValue();

                SetTextType_ExtraText(TextType_Float);
                SetExtraTextData(currentData);

                postInvalidate();                   //强制重绘，postInvalidate()可以在主线程也可以在分线程中执行
            }
        });
        valueAnimator.start();
    }
    //************************* 开始绘制附加文本 String型 直接绘制
    public void SetExtraText_Directly(String  extraText)
    {
        SetTextType_ExtraText(TextType_String);
        SetExtraText(extraText);

        mIsNeedDraw_ExtraText = true;
        postInvalidate();
    }


    public void SetInnerRoundColor(int innerRoundColor){mInnerRoundColor = innerRoundColor;}
    public int  GetInnerRoundColor() {
        return mInnerRoundColor;
    }
    public void SetOuterRoundColor(int outerRoundColor){mOuterRoundColor = outerRoundColor;}
    public int  GetOuterRoundColor() {
        return mOuterRoundColor;
    }
    public void SetOuterGradientColor(int outerGradientColor){mOuterGradientColor = outerGradientColor;}
    public int  GetOuterGradientColor() {
        return mOuterGradientColor;
    }
    public void SetMainTextColor(int mainTextColor){mMainTextColor = mainTextColor;}
    public int  GetMainTextColor() {
        return mMainTextColor;
    }
    public void SetExtraTextColor(int extraTextColor){mExtraTextColor = extraTextColor;}
    public int  GetExtraTextColor() {
        return mExtraTextColor;
    }

    public void SetRoundWidth(int roundWidth){mRoundWidth = roundWidth;}
    public int  GetRoundWidth() {
        return mRoundWidth;
    }
    public void SetProgressRoundWidth(int progressRoundWidth){mProgressRoundWidth = progressRoundWidth;}
    public int  GetProgressRoundWidth() {
        return mProgressRoundWidth;
    }

    public void SetMainTextSize(int mainTextSize){mMainTextSize = mainTextSize;}
    public int  GetMainTextSize() {
        return mMainTextSize;
    }
    public void SetExtraTextSize(int extraTextSize){mExtraTextSize = extraTextSize;}
    public int  GetExtraTextSize() {
        return mExtraTextSize;
    }

    private void SetTextType_MainText(int textType_MainText){mTextType_MainText = textType_MainText;}
    public int   GetTextType_MainText() {
        return mTextType_MainText;
    }
    private void SetTextType_ExtraText(int textType_ExtraText){mTextType_ExtraText = textType_ExtraText;}
    public int   GetTextType_ExtraText() {
        return mTextType_ExtraText;
    }

    private void  SetMainText(String mainText){mMainText = mainText;}
    public String  GetMainText() {
        return mMainText;
    }
    private void  SetExtraText(String extraText){mExtraText = extraText;}
    public String  GetExtraText() {
        return mExtraText;
    }
    private void  SetMainTextData(float  mainTextData) {
        this.mMainTextData = mainTextData;
    }
    public float  GetMainTextData() {
        return mMainTextData;
    }
    private void  SetExtraTextData(float  extraTextData) {
        this.mExtraTextData = extraTextData;
    }
    public float  GetExtraTextData() {
        return mExtraTextData;
    }
    public void   SetMainTextUnit(String mainTextUnit){mMainTextUnit = mainTextUnit;}
    public String  GetMainTextUnit() {
        return mMainTextUnit;
    }
    public void   SetExtraTextUnit(String extraTextUnit){mExtraTextUnit = extraTextUnit;}
    public String  GetExtraTextUnit() {
        return mExtraTextUnit;
    }

    public void SetIsExtraTextAbove(boolean isExtraTextAbove){mIsExtraTextAbove = isExtraTextAbove;}
    public boolean  GetIsExtraTextAbove() {
        return mIsExtraTextAbove;
    }

    private void  SetCurrentStep(float  currentStep) {
        this.mCurrentStep = currentStep;
    }
    public float  GetCurrentStep() {
        return mCurrentStep;
    }

    public void SetMaxStep(float maxStep) {
        this.mMaxStep = maxStep;
    }
    public float GetMaxStep() {
        return mMaxStep;
    }

//    public void SetMaxStep(int maxStep) {
//        this.mMaxStep = maxStep;
//    }
//    public int GetMaxStep() {
//        return mMaxStep;
//    }

    public void  SetTextPadding(float  textPadding) {
        this.mTextPadding = textPadding;
    }
    public float GetTextPadding() {
        return mTextPadding;
    }

    public void  SetIsNeedShowSymbol_MainText(boolean  isNeedShowSymbol_MainText) {
        this.mIsNeedShowSymbol_MainText = isNeedShowSymbol_MainText;
    }
    public boolean GetIsNeedShowSymbol_MainText() {
        return mIsNeedShowSymbol_MainText;
    }

    public void  SetIsNeedShowSymbol_ExtraText(boolean  isNeedShowSymbol_ExtraText) {
        this.mIsNeedShowSymbol_ExtraText = isNeedShowSymbol_ExtraText;
    }
    public boolean GetIsNeedShowSymbol_ExtraText() {
        return mIsNeedShowSymbol_ExtraText;
    }

    public void SetDecimalDigit_MainText(int decimalDigit_MainText) {
        this.mDecimalDigit_MainText = decimalDigit_MainText;
    }
    public int GetDecimalDigit_MainText() {
        return mDecimalDigit_MainText;
    }

    public void SetDecimalDigit_ExtraText(int decimalDigit_ExtraText) {
        this.mDecimalDigit_ExtraText = decimalDigit_ExtraText;
    }
    public int GetDecimalDigit_ExtraText() {
        return mDecimalDigit_ExtraText;
    }


    public void  SetDrawEnabled_MainText(boolean  isNeedDraw_MainText) {
        this.mIsNeedDraw_MainText = isNeedDraw_MainText;
    }
    public boolean GetDrawEnabled_MainText() {
        return mIsNeedDraw_MainText;
    }

    public void  SetDrawEnabled_ExtraText(boolean  isNeedDraw_ExtraText) {
        this.mIsNeedDraw_ExtraText = isNeedDraw_ExtraText;
    }
    public boolean GetDrawEnabled_ExtraText() {
        return mIsNeedDraw_ExtraText;
    }
}
