package org.telegram.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.support.widget.LinearLayoutManager;
import org.telegram.messenger.support.widget.RecyclerView;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.AlertDialog;
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
import org.telegram.ui.Components.NumberPicker;
import org.telegram.ui.Components.RecyclerListView;
import in.teleplus.FeaturedSettings;
import in.teleplus.R;

public class FeaturedSettingsActivity extends BaseFragment
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
        actionBar.setTitle(LocaleController.getString("FeaturedSettings", R.string.FeaturedSettings));
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
        listView.setVerticalScrollbarPosition(LocaleController.isRTL ?
                RecyclerListView.SCROLLBAR_POSITION_LEFT :
                RecyclerListView.SCROLLBAR_POSITION_RIGHT);
        frameLayout.addView(listView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));
        listView.setOnItemClickListener((view, position) ->
        {
            int viewType = listAdapter.getItemViewType(position);
            switch (viewType)
            {
                case 3:
                    if (position == listAdapter.tabsSettingsRow)
                        presentFragment(new TabsSettingsActivity(), false);
                    break;
                case 4:
                {
                    TextCheckCell cell = (TextCheckCell)view;
                    if (position == listAdapter.showFeaturesSettingsMenuIconRow)
                    {
                        boolean value = !FeaturedSettings.appSettings.showFeaturesSettingsMenuIcon;
                        FeaturedSettings.appSettings.showFeaturesSettingsMenuIcon = value;
                        FeaturedSettings.appSettings.setBool(FeaturedSettings.AppSettings.ShowFeaturesSettingsMenuIconKey, value);
                        cell.setChecked(value);
                        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.refreshMenuIcons);
                    }
                    else if (position == listAdapter.showClearCacheMenuIconRow)
                    {
                        boolean value = !cell.isChecked();
                        FeaturedSettings.appSettings.showClearCacheMenuIcon = value;
                        FeaturedSettings.appSettings.setBool(FeaturedSettings.AppSettings.ShowClearCacheMenuIconKey, value);
                        cell.setChecked(value);
                        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.refreshMenuIcons);
                    }
                    else if (position == listAdapter.showMarkAllAsReadMenuIconRow)
                    {
                        boolean value = !cell.isChecked();
                        FeaturedSettings.appSettings.showMarkAllAsReadMenuIcon = value;
                        FeaturedSettings.appSettings.setBool(FeaturedSettings.AppSettings.ShowMarkAllAsReadMenuIconKey, value);
                        cell.setChecked(value);
                        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.refreshMenuIcons);
                    }
                    else if (position == listAdapter.confirmSendAudioMessageRow)
                    {
                        boolean value = !cell.isChecked();
                        FeaturedSettings.chatsSettings.confirmSendVoiceMessage = value;
                        FeaturedSettings.chatsSettings.setBool(FeaturedSettings.ChatsSettings.ConfirmSendVoiceMessageKey, value);
                        cell.setChecked(value);
                    }
                    else if (position == listAdapter.showOnlineToastRow)
                    {
                        boolean value = !cell.isChecked();
                        FeaturedSettings.chatsSettings.showOnlineToast = value;
                        FeaturedSettings.chatsSettings.setBool(FeaturedSettings.ChatsSettings.ShowOnlineToast, value);
                        cell.setChecked(value);
                    }
                    else if (position == listAdapter.showOfflineToastRow)
                    {
                        boolean value = !cell.isChecked();
                        FeaturedSettings.chatsSettings.showOfflineToast = value;
                        FeaturedSettings.chatsSettings.setBool(FeaturedSettings.ChatsSettings.ShowOfflineToast, value);
                        cell.setChecked(value);
                    }
                    else if (position == listAdapter.showTypingToastRow)
                    {
                        boolean value = !cell.isChecked();
                        FeaturedSettings.chatsSettings.showTypingToast = value;
                        FeaturedSettings.chatsSettings.setBool(FeaturedSettings.ChatsSettings.ShowTypingToast, value);
                        cell.setChecked(value);
                    }
                    else if (position == listAdapter.useInternalProxyRow)
                    {
                       // boolean value = !cell.isChecked();
                       // cell.setChecked(value);
                    }
                }
                    break;
                case 5:
                {
                    TextDetailSettingsCell cell = (TextDetailSettingsCell) view;
                    if (position == listAdapter.maxPinnedDialogsCountRow)
                    {
                        if (getParentActivity() == null)
                            return;

                        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
                        builder.setTitle(LocaleController.getString("MaxPinnedDialogsCount", R.string.MaxPinnedDialogsCount));
                        final NumberPicker numberPicker = new NumberPicker(getParentActivity());
                        numberPicker.setMinValue(5);
                        numberPicker.setMaxValue(25);
                        numberPicker.setValue(FeaturedSettings.chatsSettings.maxPinnedDialogsCount);
                        builder.setView(numberPicker);
                        builder.setNegativeButton(LocaleController.getString("Done", R.string.Done), (dialog, which) ->
                        {
                            int value = numberPicker.getValue();
                            FeaturedSettings.chatsSettings.maxPinnedDialogsCount = value;
                            FeaturedSettings.chatsSettings.setInt(FeaturedSettings.ChatsSettings.MaxPinnedDialogsCountKey, value);
                            MessagesController.getInstance(currentAccount).maxPinnedDialogsCount = value;
                            cell.setValue(Integer.toString(value));
                        });
                        showDialog(builder.create());
                    }
                    else if (position == listAdapter.showToastForRow)
                    {
                        CharSequence[] items = new CharSequence[]
                                {
                                        LocaleController.getString("All", R.string.All),
                                        LocaleController.getString("Contacts", R.string.Contacts),
                                        LocaleController.getString("Favorites", R.string.Favorites),
                                        LocaleController.getString("ContactsAndFavorites", R.string.ContactsAndFavorites)
                                };
                        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                        dialog.setTitle(LocaleController.getString("ShowToastFor", R.string.ShowToastFor));
                        dialog.setItems(items, (dialog1, which) ->
                        {
                            FeaturedSettings.ChatsSettings.ShowToastModes mode = FeaturedSettings.ChatsSettings.ShowToastModes.All;
                            switch (which)
                            {
                                case 1:
                                    mode = FeaturedSettings.ChatsSettings.ShowToastModes.Contacts;
                                    break;
                                case 2:
                                    mode = FeaturedSettings.ChatsSettings.ShowToastModes.Favorites;
                                    break;
                                case 3:
                                    mode = FeaturedSettings.ChatsSettings.ShowToastModes.ContactsAndFavorites;
                                    break;
                            }
                            FeaturedSettings.chatsSettings.showToastMode = mode;
                            FeaturedSettings.chatsSettings.setInt(FeaturedSettings.ChatsSettings.ShowToastMode, mode.code);
                            cell.setValue(getShowToastForText());
                        });
                        showDialog(dialog.create());
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

        // interface
        private int interfaceHeaderRow;
        private int tabsSettingsRow;
        private int showFeaturesSettingsMenuIconRow;
        private int showClearCacheMenuIconRow;
        private int showMarkAllAsReadMenuIconRow;
        private int interfaceEndRow;

        // chats
        private int chatsHeaderRow;
        private int maxPinnedDialogsCountRow;
        private int confirmSendAudioMessageRow;
        private int chatsEndRow;

        // toast
        private int toastHeaderRow;
        private int showToastForRow;
        private int showOnlineToastRow;
        private int showOfflineToastRow;
        private int showTypingToastRow;
        private int toastEndRow;

        // system
        private int systemHeaderRow;
        private int useInternalProxyRow;

        private Context context;

        ListAdapter(Context context)
        {
            this.context = context;
            setRows();
        }

        private void setRows()
        {
            rowCount = 0;
            interfaceHeaderRow = rowCount++;
            tabsSettingsRow = rowCount++;
            showFeaturesSettingsMenuIconRow = rowCount++;
            showClearCacheMenuIconRow = rowCount++;
            showMarkAllAsReadMenuIconRow = rowCount++;
            interfaceEndRow = rowCount++;

            chatsHeaderRow = rowCount++;
            maxPinnedDialogsCountRow = rowCount++;
            confirmSendAudioMessageRow = rowCount++;
            chatsEndRow = rowCount++;

            toastHeaderRow = rowCount++;
            showToastForRow = rowCount++;
            showOnlineToastRow = rowCount++;
            showOfflineToastRow = rowCount++;
            showTypingToastRow = rowCount++;
            toastEndRow = rowCount++;

            systemHeaderRow = rowCount++;
            useInternalProxyRow = rowCount++;
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
            if (position == interfaceEndRow || position == chatsEndRow || position == toastEndRow)
                return 1;

            if (position == interfaceHeaderRow || position == chatsHeaderRow || position == toastHeaderRow || position == systemHeaderRow)
                return 2;

            if (position == tabsSettingsRow)
                return 3;

            if (position == showFeaturesSettingsMenuIconRow || position == showClearCacheMenuIconRow ||
                    position == showMarkAllAsReadMenuIconRow || position == confirmSendAudioMessageRow ||
                    position == showOnlineToastRow || position == showOfflineToastRow ||
                    position == showTypingToastRow || position == useInternalProxyRow)
                return 4;

            if (position == maxPinnedDialogsCountRow || position == showToastForRow)
                return 5;

            return 0;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            View view;
            switch (viewType)
            {
                case 0:
                    view = new EmptyCell(context);
                    break;
                case 1:
                    view = new ShadowSectionCell(context);
                    break;
                case 2:
                    view = new HeaderCell(context);
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                case 3:
                    view = new TextSettingsCell(context);
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                case 4:
                    view = new TextCheckCell(context);
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                case 5:
                    view = new TextDetailSettingsCell(context);
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                default:
                    view = new EmptyCell(context);
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
            }

            view.setLayoutParams(new RecyclerView.LayoutParams(
                    RecyclerView.LayoutParams.MATCH_PARENT,
                    RecyclerView.LayoutParams.WRAP_CONTENT));

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
                    HeaderCell cell = (HeaderCell) holder.itemView;
                    if (position == interfaceHeaderRow)
                        cell.setText(LocaleController.getString("Interface", R.string.Interface));
                    else if (position == chatsHeaderRow)
                        cell.setText(LocaleController.getString("Chats", R.string.Chats));
                    else if (position == toastHeaderRow)
                        cell.setText(LocaleController.getString("Toast", R.string.Toast));
                    else if (position == systemHeaderRow)
                        cell.setText(LocaleController.getString("System", R.string.System));
                }
                    break;
                case 3:
                {
                    TextSettingsCell settingsCell = (TextSettingsCell) holder.itemView;
                    if (position == tabsSettingsRow)
                        settingsCell.setText(LocaleController.getString("TabsSettings", R.string.TabsSettings), true);
                }
                    break;
                case 4:
                {
                    TextCheckCell checkCell = (TextCheckCell) holder.itemView;
                    if (position == showFeaturesSettingsMenuIconRow)
                    {
                        checkCell.setTextAndCheck(LocaleController.getString("ShowFeaturesSettingsMenuIcon", R.string.ShowFeaturesSettingsMenuIcon),
                                FeaturedSettings.appSettings.showFeaturesSettingsMenuIcon, true);
                    }
                    else if (position == showClearCacheMenuIconRow)
                    {
                        checkCell.setTextAndCheck(LocaleController.getString("ShowClearCacheMenuIcon", R.string.ShowClearCacheMenuIcon),
                                FeaturedSettings.appSettings.showClearCacheMenuIcon, true);
                    }
                    else if (position == showMarkAllAsReadMenuIconRow)
                    {
                        checkCell.setTextAndCheck(LocaleController.getString("ShowMarkAllAsReadMenuIcon", R.string.ShowMarkAllAsReadMenuIcon),
                                FeaturedSettings.appSettings.showMarkAllAsReadMenuIcon, false);
                    }
                    else if (position == confirmSendAudioMessageRow)
                    {
                        checkCell.setTextAndCheck(LocaleController.getString("ConfirmSendVoiceMessage", R.string.ConfirmSendVoiceMessage),
                                FeaturedSettings.chatsSettings.confirmSendVoiceMessage, false);
                    }
                    else if (position == showOnlineToastRow)
                    {
                        checkCell.setTextAndCheck(LocaleController.getString("ShowOnlineToast", R.string.ShowOnlineToast),
                                FeaturedSettings.chatsSettings.showOnlineToast, true);
                    }
                    else if (position == showOfflineToastRow)
                    {
                        checkCell.setTextAndCheck(LocaleController.getString("ShowOfflineToast", R.string.ShowOfflineToast),
                                FeaturedSettings.chatsSettings.showOfflineToast, true);
                    }
                    else if (position == showTypingToastRow)
                    {
                        checkCell.setTextAndCheck(LocaleController.getString("ShowTypingToast", R.string.ShowTypingToast),
                                FeaturedSettings.chatsSettings.showTypingToast, false);
                    }
                    else if (position == useInternalProxyRow)
                    {
                        checkCell.setTextAndCheck(LocaleController.getString("UseInternalProxy", R.string.UseInternalProxy),
                                false, false);
                    }
                }
                    break;
                case 5:
                {
                    TextDetailSettingsCell cell = (TextDetailSettingsCell) holder.itemView;
                    if (position == maxPinnedDialogsCountRow)
                    {
                        cell.setTextAndValue(LocaleController.getString("MaxPinnedDialogsCount", R.string.MaxPinnedDialogsCount),
                                Integer.toString(FeaturedSettings.chatsSettings.maxPinnedDialogsCount), true);
                    }
                    else if (position == showToastForRow)
                    {
                        cell.setTextAndValue(LocaleController.getString("ShowToastFor", R.string.ShowToastFor),
                                getShowToastForText(), true);
                    }
                }
                    break;
            }
        }
    }

    private String getShowToastForText()
    {
        if (FeaturedSettings.chatsSettings.showToastMode == FeaturedSettings.ChatsSettings.ShowToastModes.Contacts)
            return LocaleController.getString("Contacts", R.string.Contacts);

        if (FeaturedSettings.chatsSettings.showToastMode == FeaturedSettings.ChatsSettings.ShowToastModes.Favorites)
            return LocaleController.getString("Favorites", R.string.Favorites);

        if (FeaturedSettings.chatsSettings.showToastMode == FeaturedSettings.ChatsSettings.ShowToastModes.ContactsAndFavorites)
            return LocaleController.getString("ContactsAndFavorites", R.string.ContactsAndFavorites);

        return LocaleController.getString("All", R.string.All);
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
