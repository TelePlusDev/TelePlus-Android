package org.telegram.ui.Components;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;
import in.teleplus.FeaturedSettings;
import in.teleplus.R;
import java.util.ArrayList;

public class TabsView extends FrameLayout implements NotificationCenter.NotificationCenterDelegate
{
    public static final int[] dialogTypes = new int[]
            {
                    MessagesController.DialogType.All,
                    MessagesController.DialogType.Users,
                    MessagesController.DialogType.Groups,
                    MessagesController.DialogType.SuperGroup,
                    MessagesController.DialogType.Channels,
                    MessagesController.DialogType.Bots,
                    MessagesController.DialogType.Favorites,
                    MessagesController.DialogType.Admin,
                    MessagesController.DialogType.Unread
            };

    private static final String[] tabTitles =
            {
                    LocaleController.getString("All", R.string.All),
                    LocaleController.getString("Users", R.string.Users),
                    LocaleController.getString("Groups", R.string.Groups),
                    LocaleController.getString("SuperGroups", R.string.SuperGroups),
                    LocaleController.getString("Channels", R.string.Channels),
                    LocaleController.getString("Bots", R.string.Bots),
                    LocaleController.getString("Favorites", R.string.Favorites),
                    LocaleController.getString("Admin", R.string.Admin),
                    LocaleController.getString("Unread", R.string.Unread)
            };

    private static final int[] tabIcons =
            {
                    R.drawable.tab_all,
                    R.drawable.tab_user,
                    R.drawable.tab_group,
                    R.drawable.tab_supergroup,
                    R.drawable.tab_channel,
                    R.drawable.tab_bot,
                    R.drawable.tab_favs,
                    R.drawable.tab_admin,
                    R.drawable.tab_unread
            };

    public enum TabIndex
    {
        All(0),
        Users(1),
        Groups(2),
        SuperGroup(3),
        Channels(4),
        Bots(5),
        Favorites(6),
        Admin(7),
        Unread(8);

        public final int value;

        TabIndex(int value)
        {
            this.value = value;
        }
    }

    private class Tab
    {
        public final TabIndex index;
        public final String title;
        public final int icon;

        Tab(TabIndex index)
        {
            this.index = index;
            this.title = tabTitles[index.value];
            this.icon = tabIcons[index.value];
        }
    }

    public interface Listener
    {
        void onPageSelected(int position, int tabIndex);
        void onTabClick();
    }

    private ArrayList<Tab> tabsArray = new ArrayList<>();
    private int[] indexToPosition = new int[] { -1, -1, -1, -1, -1, -1, -1, -1, -1 };
    private TabsPagerTitleStrip tabsPagerTitleStrip;
    private ViewPager pager;
    private int currentTabIndex = FeaturedSettings.tabSettings.currentTab;
    private Listener listener;
    private boolean saveSelectedTab = true;

    public ViewPager getPager()
    {
        return pager;
    }

    public void setListener(Listener value)
    {
        listener = value;
    }

    public void setCurrentTabIndex(TabIndex index)
    {
        this.currentTabIndex = index.value;
    }

    public void setSaveSelectedTab(boolean saveSelectedTab)
    {
        this.saveSelectedTab = saveSelectedTab;
    }


    public TabsView(Context context)
    {
        super(context);
        pager = new ViewPager(context)
        {
            @Override
            public boolean onInterceptTouchEvent(MotionEvent ev)
            {
                if (getParent() != null)
                    getParent().requestDisallowInterceptTouchEvent(true);

                return super.onInterceptTouchEvent(ev);
            }
        };
        loadTabs();
        addView(pager, 0, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));
        LinearLayout tabsContainer = new LinearLayout(context)
        {
            @Override
            public boolean onInterceptTouchEvent(MotionEvent ev)
            {
                if (getParent() != null)
                    getParent().requestDisallowInterceptTouchEvent(true);

                return super.onInterceptTouchEvent(ev);
            }
        };

        tabsContainer.setOrientation(LinearLayout.HORIZONTAL);
        tabsContainer.setBackgroundColor(Theme.getColor(Theme.key_actionBarDefault));
        addView(tabsContainer, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        tabsPagerTitleStrip = new TabsPagerTitleStrip(context);
        tabsPagerTitleStrip.setShouldExpand(FeaturedSettings.tabSettings.tabsShouldExpand);
        tabsPagerTitleStrip.setViewPager(pager);
        tabsPagerTitleStrip.setIndicatorHeight(AndroidUtilities.dp(3));
        tabsPagerTitleStrip.setDividerColor(0x00000000);
        tabsPagerTitleStrip.setUnderlineHeight(0);
        tabsPagerTitleStrip.setUnderlineColor(0x00000000);
        tabsPagerTitleStrip.setDelegate(new TabsPagerTitleStrip.PlusScrollSlidingTabStripDelegate()
        {
            @Override
            public void onTabsUpdated()
            {
                unreadCount();
            }

            @Override
            public void onTabClick()
            {
                if (listener != null)
                {
                    listener.onTabClick();
                }
            }
        });
        tabsPagerTitleStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            private boolean loop;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position)
            {
                currentTabIndex = tabsArray.get(position).index.value;
                saveCurrentTab();
                if (listener != null)
                    listener.onPageSelected(position, currentTabIndex);
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {
                int currentPage = indexToPosition[currentTabIndex];
                if (state == ViewPager.SCROLL_STATE_IDLE)
                {
                    if (loop && pager.getAdapter() != null)
                    {
                        AndroidUtilities.runOnUIThread(() -> pager.setCurrentItem(currentPage == 0 ?
                                pager.getAdapter().getCount() - 1 : 0), 100);
                        loop = false;
                    }
                }
                else if (state == 1 && pager.getAdapter() != null)
                    loop = currentPage == 0 || currentPage == pager.getAdapter().getCount() - 1;
                else if (state == 2)
                    loop = false;
            }
        });

        tabsContainer.addView(tabsPagerTitleStrip, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 1.0f));
        unreadCount();
    }

    private void loadTabs()
    {
        tabsArray.clear();
        TabIndex[] items = TabIndex.values();
        int position = 0;
        boolean isValidCurrentTabIndex = false;

        for (TabIndex tabIndex : items)
        {
            if (tabIndex == TabIndex.All && FeaturedSettings.tabSettings.hideALl)
                continue;
            else if (tabIndex == TabIndex.Users && FeaturedSettings.tabSettings.hideUsers)
                continue;
            else if (tabIndex == TabIndex.Groups && FeaturedSettings.tabSettings.hideGroups)
                continue;
            else if (tabIndex == TabIndex.SuperGroup && FeaturedSettings.tabSettings.hideSuperGroups)
                continue;
            else if (tabIndex == TabIndex.Channels && FeaturedSettings.tabSettings.hideChannels)
                continue;
            else if (tabIndex == TabIndex.Bots && FeaturedSettings.tabSettings.hideBots)
                continue;
            else if (tabIndex == TabIndex.Favorites && FeaturedSettings.tabSettings.hideFavorites)
                continue;
            else if (tabIndex == TabIndex.Admin && FeaturedSettings.tabSettings.hideAdmins)
                continue;
            else if (tabIndex == TabIndex.Unread && FeaturedSettings.tabSettings.hideUnreads)
                continue;

            indexToPosition[tabIndex.value] = position;
            tabsArray.add(position, new Tab(tabIndex));

            if (currentTabIndex == tabIndex.value)
                isValidCurrentTabIndex = true;

            position++;
        }

        if (!isValidCurrentTabIndex && tabsArray.size() > 0)
        {
            currentTabIndex = tabsArray.get(0).index.value;
            saveCurrentTab();
        }

        pager.setAdapter(null);
        pager.setOffscreenPageLimit(tabsArray.size());
        pager.setAdapter(new TabsAdapter());
        post(() -> pager.setCurrentItem(indexToPosition[currentTabIndex]));
    }

    private void saveCurrentTab()
    {
        if (!saveSelectedTab)
            return;

        FeaturedSettings.tabSettings.currentTab = currentTabIndex;
        FeaturedSettings.tabSettings.setInt(FeaturedSettings.TabSettings.CurrentTab, currentTabIndex);
    }

    public int getVisibleTabsCount()
    {
        return tabsArray.size();
    }

    public void reloadTabs()
    {
        loadTabs();
        if (pager.getAdapter() != null)
            pager.getAdapter().notifyDataSetChanged();
    }

    @Override
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.refreshTabsCounters);
    }

    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.refreshTabsCounters);
    }

    @Override
    public void didReceivedNotification(int id, int accountId, Object... args)
    {
        if (id == NotificationCenter.refreshTabsCounters)
        {
            if (tabsArray != null && tabsArray.size() > 1)
                unreadCount();
        }
    }

    private void unreadCount()
    {
        if (!FeaturedSettings.tabSettings.hideUnreads)
            unreadCount(MessagesController.getInstance(UserConfig.selectedAccount).dialogsUnread,
                    indexToPosition[TabIndex.Unread.value]);

        if (!FeaturedSettings.tabSettings.hideAdmins)
            unreadCount(MessagesController.getInstance(UserConfig.selectedAccount).dialogsAdmin,
                    indexToPosition[TabIndex.Admin.value]);

        if (!FeaturedSettings.tabSettings.hideFavorites)
            unreadCount(MessagesController.getInstance(UserConfig.selectedAccount).dialogsFavorites,
                    indexToPosition[TabIndex.Favorites.value]);

        if (!FeaturedSettings.tabSettings.hideBots)
            unreadCount(MessagesController.getInstance(UserConfig.selectedAccount).dialogsBots,
                    indexToPosition[TabIndex.Bots.value]);

        if (!FeaturedSettings.tabSettings.hideChannels)
            unreadCount(MessagesController.getInstance(UserConfig.selectedAccount).dialogsChannels,
                    indexToPosition[TabIndex.Channels.value]);

        if (!FeaturedSettings.tabSettings.hideGroups)
            unreadCount(MessagesController.getInstance(UserConfig.selectedAccount).dialogsGroups,
                    indexToPosition[TabIndex.Groups.value]);

        if (!FeaturedSettings.tabSettings.hideSuperGroups)
            unreadCount(MessagesController.getInstance(UserConfig.selectedAccount).dialogsSuperGroups,
                    indexToPosition[TabIndex.SuperGroup.value]);

        if (!FeaturedSettings.tabSettings.hideUsers)
            unreadCount(MessagesController.getInstance(UserConfig.selectedAccount).dialogsUsers,
                    indexToPosition[TabIndex.Users.value]);

        if (!FeaturedSettings.tabSettings.hideALl)
            unreadCount(MessagesController.getInstance(UserConfig.selectedAccount).dialogsAll,
                    indexToPosition[TabIndex.All.value]);
    }

    private void unreadCount(final ArrayList<TLRPC.TL_dialog> dialogs, int position)
    {
        if (position == -1)
            return;

        boolean allMuted = true;
        int unreadCount = 0;

        if (dialogs != null && !dialogs.isEmpty())
        {
            for (int a = 0; a < dialogs.size(); a++)
            {
                TLRPC.TL_dialog dialog = dialogs.get(a);
                if (dialog != null && dialog.unread_count > 0)
                {
                    boolean isMuted = MessagesController.getInstance(UserConfig.selectedAccount).isDialogMuted(dialog.id);
                    if (!isMuted || !FeaturedSettings.tabSettings.tabsCountersCountNotMuted)
                    {
                        int i = dialog.unread_count;
                        if (i == 0 && FeaturedSettings.tabSettings.getInt("unread_" + dialog.id, 0) == 1)
                            i = 1;

                        if (i > 0)
                        {
                            if (FeaturedSettings.tabSettings.tabsCountersCountChats)
                                unreadCount = unreadCount + 1;
                            else
                                unreadCount = unreadCount + i;

                            if (!isMuted)
                                allMuted = false;
                        }
                    }
                }
            }
        }

        tabsPagerTitleStrip.updateCounter(position, unreadCount, allMuted);
    }

    private class TabsAdapter extends PagerAdapter implements TabsPagerTitleStrip.IconTabProvider
    {
        @Override
        public int getCount()
        {
            return tabsArray.size();
        }

        @Override
        public void notifyDataSetChanged()
        {
            super.notifyDataSetChanged();
            if (tabsPagerTitleStrip != null)
                tabsPagerTitleStrip.notifyDataSetChanged();
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup viewGroup, int position)
        {
            View view = new View(viewGroup.getContext());
            viewGroup.addView(view);
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup viewGroup, int position, @NonNull Object object)
        {
            viewGroup.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object)
        {
            return view == object;
        }

        @Override
        public void unregisterDataSetObserver(@NonNull DataSetObserver observer)
        {
            super.unregisterDataSetObserver(observer);
        }

        @Override
        public int getPageIconResId(int position)
        {
            return tabsArray.get(position).icon;
        }

        @Override
        public String getPageTitle(int position)
        {
            return tabsArray.get(position).title;
        }
    }
}
