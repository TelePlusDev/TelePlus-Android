package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;
import org.telegram.ui.ActionBar.Theme;
import in.teleplus.FeaturedSettings;
import java.util.Locale;

public class TabsPagerTitleStrip extends HorizontalScrollView
{
    public interface IconTabProvider
    {
        int getPageIconResId(int position);
    }

    public interface PlusScrollSlidingTabStripDelegate
    {
        void onTabsUpdated();
        void onTabClick();
    }

    private PlusScrollSlidingTabStripDelegate delegate;
    private LinearLayout.LayoutParams defaultTabLayoutParams;
    private LinearLayout.LayoutParams expandedTabLayoutParams;
    private final PageListener pageListener = new PageListener();
    public ViewPager.OnPageChangeListener delegatePageListener;

    private LinearLayout tabsContainer;
    private ViewPager pager;
    private int tabCount;
    private int currentPosition = 0;
    private float currentPositionOffset = 0f;
    private Paint rectPaint;
    private Paint dividerPaint;
    private int underlineColor = 0x1A000000;
    private int dividerColor = 0x1A000000;
    private int scrollOffset = AndroidUtilities.dp(/*52*/20);
    private int indicatorHeight = AndroidUtilities.dp(8);
    private int underlineHeight = AndroidUtilities.dp(2);
    private int dividerPadding = AndroidUtilities.dp(12);
    private int tabPadding = AndroidUtilities.dp(15);
    private int dividerWidth = AndroidUtilities.dp(1);
    private int btnBgRes;
    private int lastScrollX = 0;
    private int currentPage = 0;
    private int tabTextIconUnselectedColor;
    private int tabTextIconSelectedColor;

    public void setDelegate(PlusScrollSlidingTabStripDelegate scrollSlidingTabStripDelegate)
    {
        delegate = scrollSlidingTabStripDelegate;
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener)
    {
        this.delegatePageListener = listener;
    }


    public TabsPagerTitleStrip(Context context)
    {
        super(context);
        setFillViewport(true);
        setWillNotDraw(false);
        setHorizontalScrollBarEnabled(false);

        tabsContainer = new LinearLayout(context);
        tabsContainer.setOrientation(LinearLayout.HORIZONTAL);
        tabsContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(tabsContainer);

        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Paint.Style.FILL);

        dividerPaint = new Paint();
        dividerPaint.setAntiAlias(true);
        dividerPaint.setStrokeWidth(dividerWidth);

        defaultTabLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        expandedTabLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 1.0F);
        TypedValue outValue = new TypedValue();
        getContext().getTheme().resolveAttribute(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ?
                android.R.attr.selectableItemBackgroundBorderless :
                android.R.attr.selectableItemBackground, outValue, true);

        btnBgRes = outValue.resourceId;
        tabTextIconUnselectedColor = AndroidUtilities.getIntAlphaColor(Theme.getColor(Theme.key_actionBarDefaultIcon), 0.35f);
        tabTextIconSelectedColor = Theme.getColor(Theme.key_actionBarDefaultIcon);
    }

    public void setViewPager(ViewPager pager)
    {
        this.pager = pager;
        //noinspection deprecation
        pager.setOnPageChangeListener(pageListener);
        notifyDataSetChanged();
    }

    public void notifyDataSetChanged()
    {
        if (pager.getAdapter() == null)
            return;

        tabsContainer.removeAllViews();
        tabCount = pager.getAdapter().getCount();
        if (tabCount < 2)
            return;

        for (int i = 0; i < tabCount; i++)
            addIconTabWithCounter(i, ((IconTabProvider) pager.getAdapter()).getPageIconResId(i));

        updateTabStyles();
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
        {
            @Override
            public void onGlobalLayout()
            {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                currentPosition = pager.getCurrentItem();
                currentPage = currentPosition;
                scrollToChild(currentPosition, 0);
            }
        });
    }

    private void addIconTabWithCounter(final int position, int resId)
    {
        ImageButton tab = new ImageButton(getContext());
        tab.setImageResource(resId);
        tab.setColorFilter(position == pager.getCurrentItem() ? tabTextIconSelectedColor : tabTextIconUnselectedColor, PorterDuff.Mode.SRC_IN);
        tab.setScaleType(ImageView.ScaleType.CENTER);
        addTabWithCounter(position, tab);
    }

    public void addTabWithCounter(final int position, View view)
    {
        RelativeLayout tab = new RelativeLayout(getContext());
        tab.setFocusable(true);

        tabsContainer.addView(tab, FeaturedSettings.tabSettings.tabsShouldExpand ? expandedTabLayoutParams : defaultTabLayoutParams);
        view.setBackgroundResource(btnBgRes);
        view.setOnClickListener(v ->
        {
            if (position == pager.getCurrentItem())
            {
                if (delegate != null)
                    delegate.onTabClick();
            }
            else
            {
                if (pager != null)
                    pager.setCurrentItem(position);
            }
        });

        tab.addView(view, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));
        tab.setSelected(position == currentPosition);

        TextView textView = new TextView(getContext());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, FeaturedSettings.tabSettings.chatsTabCounterSize);
        textView.setTextColor(Theme.getColor(Theme.key_dialogBadgeText));
        textView.setGravity(Gravity.CENTER);

        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadius(AndroidUtilities.dp(32));
        //noinspection deprecation
        textView.setBackgroundDrawable(shape);
        textView.setMinWidth(AndroidUtilities.dp(18));

        textView.setPadding(AndroidUtilities.dp(FeaturedSettings.tabSettings.chatsTabCounterSize > 10 ? FeaturedSettings.tabSettings.chatsTabCounterSize - 7 : 4), 0,
                AndroidUtilities.dp(FeaturedSettings.tabSettings.chatsTabCounterSize > 10 ? FeaturedSettings.tabSettings.chatsTabCounterSize - 7 : 4), 0);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(AndroidUtilities.dp(3), AndroidUtilities.dp(5), AndroidUtilities.dp(3), AndroidUtilities.dp(5));
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        tab.addView(textView, params);
    }

    public void changeTabsColor(int position)
    {
        RelativeLayout frame = (RelativeLayout) tabsContainer.getChildAt(currentPage);
        if (frame != null)
        {
            try
            {
                View view = ((RelativeLayout) tabsContainer.getChildAt(position)).getChildAt(0);
                if (view instanceof ImageButton)
                {
                    ((ImageButton) frame.getChildAt(0)).setColorFilter(tabTextIconUnselectedColor, PorterDuff.Mode.SRC_IN); // Previous
                    ((ImageButton) view).setColorFilter(tabTextIconSelectedColor, PorterDuff.Mode.SRC_IN); // Selected
                }
                else if (view instanceof TextView)
                {
                    ((TextView) frame.getChildAt(0)).setTextColor(tabTextIconUnselectedColor); // Previous
                    ((TextView) view).setTextColor(tabTextIconSelectedColor); // Selected
                }
            }
            catch (Exception e)
            {
                FileLog.e(e);
            }
        }
    }

    public void updateCounter(int position, int count, boolean allMuted)
    {
        RelativeLayout frame = (RelativeLayout) tabsContainer.getChildAt(position);
        if (frame != null && frame.getChildCount() > 1)
        {
            TextView tv = (TextView) frame.getChildAt(1);
            if (tv != null)
            {
                if (count > 0 && !FeaturedSettings.tabSettings.hideTabsCounters)
                {
                    tv.setVisibility(VISIBLE);
                    tv.setText(count >= 10000 && FeaturedSettings.tabSettings.limitTabsCounters ? "+9999" : String.format(Locale.getDefault(), "%d", count));
                    tv.getBackground().setColorFilter(allMuted ?
                            Theme.getColor(Theme.key_chats_unreadCounterMuted) :
                            Theme.getColor(Theme.key_chats_unreadCounter), PorterDuff.Mode.SRC_IN);
                }
                else
                {
                    tv.setVisibility(INVISIBLE);
                }

                tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
                tv.setTextColor(Theme.getColor(Theme.key_chats_unreadCounterText));
                tv.setPadding(AndroidUtilities.dp(FeaturedSettings.tabSettings.chatsTabCounterSize > 10 ? FeaturedSettings.tabSettings.chatsTabCounterSize - 7 : 4), 0,
                        AndroidUtilities.dp(FeaturedSettings.tabSettings.chatsTabCounterSize > 10 ? FeaturedSettings.tabSettings.chatsTabCounterSize - 7 : 4), 0);
            }
        }
    }

    private void updateTabStyles()
    {
        for (int i = 0; i < tabCount; i++)
        {
            View tab = tabsContainer.getChildAt(i);
            tab.setPadding(0, 0, 0, 0);
            if (FeaturedSettings.tabSettings.tabsShouldExpand)
            {
                if (tab.getLayoutParams() != expandedTabLayoutParams)
                    tab.setLayoutParams(expandedTabLayoutParams);
            }
            else
            {
                if (tab.getLayoutParams() != defaultTabLayoutParams)
                    tab.setLayoutParams(defaultTabLayoutParams);

                View view = ((RelativeLayout) tabsContainer.getChildAt(i)).getChildAt(0);
                if (view != null)
                    view.setPadding(tabPadding, 0, tabPadding, 0);
            }
        }

        if (delegate != null)
            delegate.onTabsUpdated();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!FeaturedSettings.tabSettings.tabsShouldExpand || MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.UNSPECIFIED)
            return;

        int myWidth = getMeasuredWidth();
        tabsContainer.measure(MeasureSpec.EXACTLY | myWidth, heightMeasureSpec);
    }

    private void scrollToChild(int position, int offset)
    {
        if (tabCount == 0)
            return;

        if (position >= tabsContainer.getChildCount())
            return;

        int newScrollX = tabsContainer.getChildAt(position).getLeft() + offset;
        if (position > 0 || offset > 0)
            newScrollX -= scrollOffset;

        if (newScrollX != lastScrollX)
        {
            lastScrollX = newScrollX;
            scrollTo(newScrollX, 0);
        }
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        if (isInEditMode() || tabCount == 0 || currentPosition >= tabCount)
            return;

        final int height = getHeight();
        rectPaint.setColor(Theme.getColor(Theme.key_actionBarDefaultIcon));
        canvas.drawRect(0, height - underlineHeight, tabsContainer.getWidth(), height, rectPaint);

        View currentTab = tabsContainer.getChildAt(currentPosition);
        float lineLeft = currentTab.getLeft();
        float lineRight = currentTab.getRight();

        if (currentPositionOffset > 0f && currentPosition < tabCount - 1)
        {
            View nextTab = tabsContainer.getChildAt(currentPosition + 1);
            final float nextTabLeft = nextTab.getLeft();
            final float nextTabRight = nextTab.getRight();
            lineLeft = (currentPositionOffset * nextTabLeft + (1f - currentPositionOffset) * lineLeft);
            lineRight = (currentPositionOffset * nextTabRight + (1f - currentPositionOffset) * lineRight);
        }

        rectPaint.setColor(Theme.getColor(Theme.key_actionBarDefaultIcon));
        canvas.drawRect(lineLeft, height - indicatorHeight, lineRight, height, rectPaint);

        dividerPaint.setColor(dividerColor);
        for (int i = 0; i < tabCount - 1; i++)
        {
            View tab = tabsContainer.getChildAt(i);
            canvas.drawLine(tab.getRight(), dividerPadding, tab.getRight(), height - dividerPadding, dividerPaint);
        }
    }

    private class PageListener implements ViewPager.OnPageChangeListener
    {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
        {
            currentPosition = position;
            currentPositionOffset = positionOffset;
            scrollToChild(position, (int) (positionOffset * tabsContainer.getChildAt(position).getWidth()));
            invalidate();
            if (delegatePageListener != null)
                delegatePageListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }

        @Override
        public void onPageScrollStateChanged(int state)
        {
            if (state == ViewPager.SCROLL_STATE_IDLE)
                scrollToChild(pager.getCurrentItem(), 0);

            if (delegatePageListener != null)
                delegatePageListener.onPageScrollStateChanged(state);
        }

        @Override
        public void onPageSelected(int position)
        {
            if (delegatePageListener != null)
                delegatePageListener.onPageSelected(position);

            changeTabsColor(position);
            currentPage = position;
        }

    }

    public void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
        if (!FeaturedSettings.tabSettings.tabsShouldExpand)
            post(this::notifyDataSetChanged);
    }

    public void setIndicatorHeight(int indicatorLineHeightPx)
    {
        this.indicatorHeight = indicatorLineHeightPx;
        invalidate();
    }

    public void setUnderlineColor(int underlineColor)
    {
        this.underlineColor = underlineColor;
        invalidate();
    }

    public void setUnderlineHeight(int underlineHeightPx)
    {
        this.underlineHeight = underlineHeightPx;
        invalidate();
    }

    public void setDividerColor(int dividerColor)
    {
        this.dividerColor = dividerColor;
        invalidate();
    }

    public void setShouldExpand(boolean shouldExpand)
    {
        if (FeaturedSettings.tabSettings.tabsShouldExpand != shouldExpand)
        {
            FeaturedSettings.tabSettings.tabsShouldExpand = shouldExpand;
            FeaturedSettings.tabSettings.setBool(FeaturedSettings.TabSettings.TabsShouldExpand, shouldExpand);
            requestLayout();
        }
    }

    public void setTextSize(int textSizePx)
    {
        if (FeaturedSettings.tabSettings.tabsTextSize != textSizePx)
        {
            FeaturedSettings.tabSettings.tabsTextSize = textSizePx;
            updateTabStyles();
        }
    }

    public int getTextSize()
    {
        return FeaturedSettings.tabSettings.tabsTextSize;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state)
    {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        currentPosition = savedState.currentPosition;
        requestLayout();
    }

    @Override
    public Parcelable onSaveInstanceState()
    {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.currentPosition = currentPosition;
        return savedState;
    }

    static class SavedState extends BaseSavedState
    {
        int currentPosition;

        public SavedState(Parcelable superState)
        {
            super(superState);
        }

        private SavedState(Parcel in)
        {
            super(in);
            currentPosition = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags)
        {
            super.writeToParcel(dest, flags);
            dest.writeInt(currentPosition);
        }

        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>()
        {
            @Override
            public SavedState createFromParcel(Parcel in)
            {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size)
            {
                return new SavedState[size];
            }
        };
    }
}
