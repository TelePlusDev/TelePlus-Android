package org.telegram.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.support.widget.LinearLayoutManager;
import org.telegram.messenger.support.widget.RecyclerView;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.EmptyCell;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextDetailSettingsCell;
import org.telegram.ui.Cells.TextInfoCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;
import in.teleplus.FeaturedSettings;
import in.teleplus.R;

public class TabsSettingsActivity extends BaseFragment
{
    private RecyclerListView listView;
    private ListAdapter listAdapter;


    @Override
    public boolean onFragmentCreate()
    {
        return super.onFragmentCreate();
    }

    @Override
    public void onFragmentDestroy()
    {
        super.onFragmentDestroy();
    }

    @Override
    public View createView(Context context)
    {
        actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        actionBar.setAllowOverlayTitle(true);
        actionBar.setTitle(LocaleController.getString("TabsSettings", R.string.TabsSettings));
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick()
        {
            @Override
            public void onItemClick(int id)
            {
                if (id == -1)
                    finishFragment();
            }
        });

        fragmentView = new FrameLayout(context);
        fragmentView.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
        FrameLayout frameLayout = (FrameLayout) fragmentView;
        listView = new RecyclerListView(context);
        listView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        listView.setAdapter(listAdapter = new ListAdapter(context));
        listView.setVerticalScrollbarPosition(LocaleController.isRTL ? RecyclerListView.SCROLLBAR_POSITION_LEFT : RecyclerListView.SCROLLBAR_POSITION_RIGHT);
        frameLayout.addView(listView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));
        listView.setOnItemClickListener((view, position) ->
        {
            int viewType = listAdapter.getItemViewType(position);
            switch (viewType)
            {
                case 2:
                {
                    TextCheckCell cell = (TextCheckCell)view;
                    if (position == listAdapter.actionBarCastShadows)
                    {
                        boolean value = !FeaturedSettings.tabSettings.actionBarCastShadows;
                        FeaturedSettings.tabSettings.actionBarCastShadows = value;
                        FeaturedSettings.tabSettings.setBool(FeaturedSettings.TabSettings.ActionBarCastShadowsKey, value);
                        cell.setChecked(value);
                        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.refreshTabs, 2);
                    }
                    if (position == listAdapter.tabsSettingsTabsToBottomRow)
                    {
                        boolean value = !cell.isChecked();
                        FeaturedSettings.tabSettings.tabsToBottom = value;
                        FeaturedSettings.tabSettings.setBool(FeaturedSettings.TabSettings.TabsToBottom, value);
                        cell.setChecked(value);
                        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.refreshTabs, 1);
                    }
                    else if (position == listAdapter.tabsSettingsHideTabsRow)
                    {
                        boolean value = !cell.isChecked();
                        FeaturedSettings.tabSettings.hideTabs = value;
                        FeaturedSettings.tabSettings.setBool(FeaturedSettings.TabSettings.HideTabs, value);
                        cell.setChecked(value);
                        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.refreshTabs, 2);
                    }
                    else if (position == listAdapter.tabsSettingsHideAllRow)
                    {
                        boolean value = !cell.isChecked();
                        FeaturedSettings.tabSettings.hideALl = value;
                        FeaturedSettings.tabSettings.setBool(FeaturedSettings.TabSettings.HideAll, value);
                        cell.setChecked(value);
                        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.refreshTabs, 3);
                    }
                    else if (position == listAdapter.tabsSettingsHideUsersRow)
                    {
                        boolean value = !cell.isChecked();
                        FeaturedSettings.tabSettings.hideUsers = value;
                        FeaturedSettings.tabSettings.setBool(FeaturedSettings.TabSettings.HideUsers, value);
                        cell.setChecked(value);
                        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.refreshTabs, 3);
                    }
                    else if (position == listAdapter.tabsSettingsHideGroupsRow)
                    {
                        boolean value = !cell.isChecked();
                        FeaturedSettings.tabSettings.hideGroups = value;
                        FeaturedSettings.tabSettings.setBool(FeaturedSettings.TabSettings.HideGroups, value);
                        cell.setChecked(value);
                        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.refreshTabs, 3);
                    }
                    else if (position == listAdapter.tabsSettingsHideSuperGroupsRow)
                    {
                        boolean value = !cell.isChecked();
                        FeaturedSettings.tabSettings.hideSuperGroups = value;
                        FeaturedSettings.tabSettings.setBool(FeaturedSettings.TabSettings.HideSuperGroups, value);
                        cell.setChecked(value);
                        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.refreshTabs, 3);
                    }
                    else if (position == listAdapter.tabsSettingsHideChannelsRow)
                    {
                        boolean value = !cell.isChecked();
                        FeaturedSettings.tabSettings.hideChannels = value;
                        FeaturedSettings.tabSettings.setBool(FeaturedSettings.TabSettings.HideChannels, value);
                        cell.setChecked(value);
                        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.refreshTabs, 3);
                    }
                    else if (position == listAdapter.tabsSettingsHideBotsRow)
                    {
                        boolean value = !cell.isChecked();
                        FeaturedSettings.tabSettings.hideBots = value;
                        FeaturedSettings.tabSettings.setBool(FeaturedSettings.TabSettings.HideBots, value);
                        cell.setChecked(value);
                        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.refreshTabs, 3);
                    }
                    else if (position == listAdapter.tabsSettingsHideFavoritesRow)
                    {
                        boolean value = !cell.isChecked();
                        FeaturedSettings.tabSettings.hideFavorites = value;
                        FeaturedSettings.tabSettings.setBool(FeaturedSettings.TabSettings.HideFavorites, value);
                        cell.setChecked(value);
                        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.refreshTabs, 3);
                    }
                    else if (position == listAdapter.tabsSettingsHideAdminsRow)
                    {
                        boolean value = !cell.isChecked();
                        FeaturedSettings.tabSettings.hideAdmins = value;
                        FeaturedSettings.tabSettings.setBool(FeaturedSettings.TabSettings.HideAdmins, value);
                        cell.setChecked(value);
                        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.refreshTabs, 3);
                    }
                    else if (position == listAdapter.tabsSettingsHideUnreadsRow)
                    {
                        boolean value = !cell.isChecked();
                        FeaturedSettings.tabSettings.hideUnreads = value;
                        FeaturedSettings.tabSettings.setBool(FeaturedSettings.TabSettings.HideUnreads, value);
                        cell.setChecked(value);
                        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.refreshTabs, 3);
                    }
                }
                break;
            }
        });

        return fragmentView;
    }

    private class ListAdapter extends RecyclerListView.SelectionAdapter
    {
        private int rowCount;
        private int actionBarCastShadows;
        private int tabsSettingsTabsToBottomRow;
        private int tabsSettingsHideTabsRow;
        private int tabsSettingsHideAllRow;
        private int tabsSettingsHideUsersRow;
        private int tabsSettingsHideGroupsRow;
        private int tabsSettingsHideSuperGroupsRow;
        private int tabsSettingsHideChannelsRow;
        private int tabsSettingsHideBotsRow;
        private int tabsSettingsHideFavoritesRow;
        private int tabsSettingsHideAdminsRow;
        private int tabsSettingsHideUnreadsRow;
        private int tabsSettingsEndRow;

        private Context context;


        ListAdapter(Context context)
        {
            this.context = context;
            setRows();
        }

        private void setRows()
        {
            rowCount = 0;
            actionBarCastShadows = rowCount++;
            tabsSettingsTabsToBottomRow = rowCount++;
            tabsSettingsHideTabsRow = rowCount++;
            tabsSettingsHideAllRow = rowCount++;
            tabsSettingsHideUsersRow = rowCount++;
            tabsSettingsHideGroupsRow = rowCount++;
            tabsSettingsHideSuperGroupsRow = rowCount++;
            tabsSettingsHideChannelsRow = rowCount++;
            tabsSettingsHideBotsRow = rowCount++;
            tabsSettingsHideFavoritesRow = rowCount++;
            tabsSettingsHideAdminsRow = rowCount++;
            tabsSettingsHideUnreadsRow = rowCount++;
            tabsSettingsEndRow = rowCount++;
        }

        @Override
        public boolean isEnabled(RecyclerView.ViewHolder holder)
        {
            return true;
        }

        @Override
        public int getItemCount()
        {
            return rowCount;
        }

        @Override
        public int getItemViewType(int position)
        {
            if (position == tabsSettingsEndRow)
                return 1;

            if (position == actionBarCastShadows || position == tabsSettingsHideTabsRow || position == tabsSettingsTabsToBottomRow ||
                    position == tabsSettingsHideAllRow || position == tabsSettingsHideUsersRow || position == tabsSettingsHideGroupsRow ||
                    position == tabsSettingsHideSuperGroupsRow || position == tabsSettingsHideChannelsRow || position == tabsSettingsHideBotsRow ||
                    position == tabsSettingsHideFavoritesRow || position == tabsSettingsHideAdminsRow || position == tabsSettingsHideUnreadsRow)
                return 2;

            return 0;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            View view;
            switch (viewType)
            {
                case 1:
                    view = new ShadowSectionCell(context);
                    break;
                case 2:
                    view = new TextCheckCell(context);
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                default:
                    view = new EmptyCell(context);
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
            }
            view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
            return new RecyclerListView.Holder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
        {
            switch (holder.getItemViewType())
            {
                case 0:
                    ((EmptyCell) holder.itemView).setHeight(AndroidUtilities.dp(16));
                    break;
                case 2:
                {
                    TextCheckCell textCell = (TextCheckCell) holder.itemView;
                    if (position == actionBarCastShadows)
                    {
                        textCell.setTextAndCheck(LocaleController.getString("ActionBarCastShadows", R.string.ActionBarCastShadows),
                            FeaturedSettings.tabSettings.actionBarCastShadows, true);
                    }
                    else if (position == tabsSettingsHideTabsRow)
                    {
                        textCell.setTextAndCheck(LocaleController.getString("HideTabs", R.string.HideTabs),
                            FeaturedSettings.tabSettings.hideTabs, true);
                    }
                    else if (position == tabsSettingsTabsToBottomRow)
                        textCell.setTextAndCheck(LocaleController.getString("TabsToBottom", R.string.TabsToBottom), FeaturedSettings.tabSettings.tabsToBottom, true);
                    else if (position == tabsSettingsHideAllRow)
                        textCell.setTextAndCheck(LocaleController.getString("TabsHideAll", R.string.TabsHideAll), FeaturedSettings.tabSettings.hideALl, true);
                    else if (position == tabsSettingsHideUsersRow)
                        textCell.setTextAndCheck(LocaleController.getString("TabsHideUsers", R.string.TabsHideUsers), FeaturedSettings.tabSettings.hideUsers, true);
                    else if (position == tabsSettingsHideGroupsRow)
                        textCell.setTextAndCheck(LocaleController.getString("TabsHideGroups", R.string.TabsHideGroups), FeaturedSettings.tabSettings.hideGroups, true);
                    else if (position == tabsSettingsHideSuperGroupsRow)
                        textCell.setTextAndCheck(LocaleController.getString("TabsHideSuperGroups", R.string.TabsHideSuperGroups), FeaturedSettings.tabSettings.hideSuperGroups, true);
                    else if (position == tabsSettingsHideChannelsRow)
                        textCell.setTextAndCheck(LocaleController.getString("TabsHideChannels", R.string.TabsHideChannels), FeaturedSettings.tabSettings.hideChannels, true);
                    else if (position == tabsSettingsHideBotsRow)
                        textCell.setTextAndCheck(LocaleController.getString("TabsHideBots", R.string.TabsHideBots), FeaturedSettings.tabSettings.hideBots, true);
                    else if (position == tabsSettingsHideFavoritesRow)
                        textCell.setTextAndCheck(LocaleController.getString("TabsHideFavorites", R.string.TabsHideFavorites), FeaturedSettings.tabSettings.hideFavorites, true);
                    else if (position == tabsSettingsHideAdminsRow)
                        textCell.setTextAndCheck(LocaleController.getString("TabsHideAdmins", R.string.TabsHideAdmins), FeaturedSettings.tabSettings.hideAdmins, true);
                    else if (position == tabsSettingsHideUnreadsRow)
                        textCell.setTextAndCheck(LocaleController.getString("TabsHideUnread", R.string.TabsHideUnread), FeaturedSettings.tabSettings.hideUnreads, false);
                }
                break;
            }
        }
    }

    @Override
    public ThemeDescription[] getThemeDescriptions()
    {
        return new ThemeDescription[]
                {
                        new ThemeDescription(actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_actionBarDefault),
                        new ThemeDescription(actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon),
                        new ThemeDescription(actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle),
                        new ThemeDescription(actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector),
                        new ThemeDescription(listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{EmptyCell.class, TextSettingsCell.class, TextCheckCell.class, HeaderCell.class, TextInfoCell.class, TextDetailSettingsCell.class}, null, null, null, Theme.key_windowBackgroundWhite),
                        new ThemeDescription(listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, Theme.key_avatar_backgroundActionBarBlue),
                        new ThemeDescription(listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector),
                        new ThemeDescription(listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, Theme.key_divider),
                        new ThemeDescription(listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, null, null, null, Theme.key_windowBackgroundGrayShadow),
                        new ThemeDescription(listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText),
                        new ThemeDescription(listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"valueTextView"}, null, null, null, Theme.key_windowBackgroundWhiteValueText),
                        new ThemeDescription(listView, 0, new Class[]{TextCheckCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText),
                        new ThemeDescription(listView, 0, new Class[]{TextCheckCell.class}, new String[]{"valueTextView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayText2),
                        new ThemeDescription(listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, null, null, null, Theme.key_switchThumb),
                        new ThemeDescription(listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, null, null, null, Theme.key_switchTrack),
                        new ThemeDescription(listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, null, null, null, Theme.key_switchThumbChecked),
                        new ThemeDescription(listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, null, null, null, Theme.key_switchTrackChecked),
                        new ThemeDescription(listView, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlueHeader),
                        new ThemeDescription(listView, 0, new Class[]{TextDetailSettingsCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText),
                        new ThemeDescription(listView, 0, new Class[]{TextDetailSettingsCell.class}, new String[]{"valueTextView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayText2),
                        new ThemeDescription(listView, 0, new Class[]{TextInfoCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayText5),
                };
    }
}
