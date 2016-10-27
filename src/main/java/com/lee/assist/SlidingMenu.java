package com.lee.assist;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.lee.words.R;
import com.nineoldandroids.view.ViewHelper;

/**
 * Created by Administrator on 2016/9/10.
 */
public class SlidingMenu extends HorizontalScrollView{
    private LinearLayout wapper;
    private ViewGroup menu;
    private ViewGroup content;
    private int rightPadding;
    private int screenWidth;
    private int menuWidth;
    private int contentWidth;
    private boolean once=true;
    private boolean isOpen=false;
    public SlidingMenu(Context context) {
        this(context, null);
    }

    public SlidingMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array=context.obtainStyledAttributes(attrs, R.styleable.SlidingMenu,defStyleAttr,0);
        int count=array.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr=array.getIndex(i);
            switch (attr){
                case R.styleable.SlidingMenu_rightpadding:
                    rightPadding=array.getDimensionPixelSize(attr, (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,50,context.getResources().getDisplayMetrics()));
                    break;
            }
        }
        array.recycle();
        WindowManager wm= (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics=new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth=displayMetrics.widthPixels;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(once){
            wapper= (LinearLayout) getChildAt(0);
            menu= (ViewGroup) wapper.getChildAt(0);
            content= (ViewGroup) wapper.getChildAt(1);
            menuWidth=menu.getLayoutParams().width=screenWidth-rightPadding;
            content.getLayoutParams().width=screenWidth;
            once=false;
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(!changed){
            scrollTo(menuWidth,0);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action=ev.getAction();
        switch (action){
            case MotionEvent.ACTION_UP:
                int hide=getScrollX();
                if(hide>=menuWidth/2){
                    smoothScrollTo(menuWidth,0);
                    isOpen=false;

                }else {
                    smoothScrollTo(0,0);
                    isOpen=true;
                }
                return true;
            case MotionEvent.ACTION_DOWN:
                float i=ev.getX();
                if(i>menuWidth){
                    smoothScrollTo(menuWidth,0);
                }
                return true;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        ViewHelper.setTranslationX(menu, l);

    }

}
