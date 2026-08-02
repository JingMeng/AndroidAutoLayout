package com.zhy.autolayout.widget;

import android.content.Context;
import android.content.res.TypedArray;
import com.google.android.material.tabs.TabLayout;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhy.autolayout.utils.AutoUtils;
import com.zhy.autolayout.utils.DimenUtils;

/**
 * Created by zhy on 16/3/3.
 */
public class AutoTabLayout extends TabLayout
{
    private static final int NO_VALID = -1;
    private int mTextSize;
    private boolean mTextSizeBaseWidth = false;

    public AutoTabLayout(Context context)
    {
        this(context, null);
    }

    public AutoTabLayout(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public AutoTabLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);

        initTextSizeBaseWidth(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, com.google.android.material.R.styleable.TabLayout,
                defStyleAttr, com.google.android.material.R.style.Widget_Design_TabLayout);
        int tabTextAppearance = a.getResourceId(com.google.android.material.R.styleable.TabLayout_tabTextAppearance,
                com.google.android.material.R.style.TextAppearance_Design_Tab);

        mTextSize = loadTextSizeFromTextAppearance(tabTextAppearance);
        a.recycle();
    }

    private void initTextSizeBaseWidth(Context context, AttributeSet attrs)
    {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AutoTabLayout);
        mTextSizeBaseWidth = a.getBoolean(R.styleable.AutoTabLayout_auto_textSize_base_width, false);
        a.recycle();
    }

    private int loadTextSizeFromTextAppearance(int textAppearanceResId)
    {
        TypedArray a = getContext().obtainStyledAttributes(textAppearanceResId,
                androidx.appcompat.R.styleable.TextAppearance);

        try
        {
            if (!DimenUtils.isPxVal(a.peekValue(androidx.appcompat.R.styleable.TextAppearance_android_textSize)))
                return NO_VALID;
            return a.getDimensionPixelSize(androidx.appcompat.R.styleable.TextAppearance_android_textSize, NO_VALID);
        } finally
        {
            a.recycle();
        }
    }

    @Override
    public void addTab(Tab tab, int position, boolean setSelected)
    {
        super.addTab(tab, position, setSelected);
        setUpTabTextSize(tab);
    }

    @Override
    public void addTab(Tab tab, boolean setSelected)
    {
        super.addTab(tab, setSelected);
        setUpTabTextSize(tab);
    }

    private void setUpTabTextSize(Tab tab)
    {
        if (mTextSize == NO_VALID || tab.getCustomView() != null) return;

        ViewGroup tabGroup = (ViewGroup) getChildAt(0);
        ViewGroup tabContainer = (ViewGroup) tabGroup.getChildAt(tab.getPosition());
        TextView textView = (TextView) tabContainer.getChildAt(1);


        if (AutoUtils.autoed(textView))
        {
            return;
        }
        int autoTextSize = 0 ;
        if (mTextSizeBaseWidth)
        {
            autoTextSize = AutoUtils.getPercentWidthSize(mTextSize);
        } else
        {
            autoTextSize = AutoUtils.getPercentHeightSize(mTextSize);
        }


        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, autoTextSize);
    }


}

