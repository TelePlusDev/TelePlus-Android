package in.teleplus;

import android.content.Context;
import android.content.SharedPreferences;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;

@SuppressWarnings({"WeakerAccess", "unused"})
public class FeaturedSettings
{
    public static final AppSettings appSettings = new AppSettings();
    public static final ChatsSettings chatsSettings = new ChatsSettings();
    public static final TabSettings tabSettings = new TabSettings();

    public static class BaseSettings
    {
        protected SharedPreferences storage;


        private BaseSettings(String name)
        {
            storage = ApplicationLoader.applicationContext.getSharedPreferences(name, Context.MODE_PRIVATE);
        }

        public boolean getBool(String key, boolean defaultValue)
        {
            return storage.getBoolean(key, defaultValue);
        }

        public void setBool(String key, boolean value)
        {
            storage.edit().putBoolean(key, value).apply();
        }

        public int getInt(String key, int defaultValue)
        {
            return storage.getInt(key, defaultValue);
        }

        public void setInt(String key, int value)
        {
            storage.edit().putInt(key, value).apply();
        }

        public long getLong(String key, long defaultValue)
        {
            return storage.getLong(key, defaultValue);
        }

        public void setLong(String key, long value)
        {
            storage.edit().putLong(key, value).apply();
        }

        public String getString(String key, String defaultValue)
        {
            return storage.getString(key, defaultValue);
        }

        public void setString(String key, String value)
        {
            storage.edit().putString(key, value).apply();
        }
    }

    public static class AppSettings extends BaseSettings
    {
        public enum RecordingVoiceEffects
        {
            normal(0),
            thick(1),
            robot(2),
            thin(3),
            baby(4),
            funny(5);

            public final int code;

            RecordingVoiceEffects(int code)
            {
                this.code = code;
            }

            static RecordingVoiceEffects find(int code)
            {
                for (RecordingVoiceEffects item : RecordingVoiceEffects.values())
                {
                    if (item.code == code)
                        return item;
                }

                return normal;
            }
        }

        public static final int MIN_SECURITY_PASSWORD = 4;

        public static final String SecurityPasswordKey = "SecurityPassword";
        public static final String RecordingVoiceEffectKey = "RecordingVoiceEffect";
        public static final String ShowFeaturesSettingsMenuIconKey = "ShowFeaturesSettingsMenuIcon";
        public static final String ShowClearCacheMenuIconKey = "ShowClearCacheMenuIcon";
        public static final String ShowMarkAllAsReadMenuIconKey = "ShowMarkAllAsReadMenuIcon";

        public String securityPassword;
        public RecordingVoiceEffects recordingVoiceEffect;
        public boolean showFeaturesSettingsMenuIcon;
        public boolean showClearCacheMenuIcon;
        public boolean showMarkAllAsReadMenuIcon;

        private AppSettings()
        {
            super(AppSettings.class.getName());
            securityPassword = storage.getString(SecurityPasswordKey, null);
            recordingVoiceEffect = RecordingVoiceEffects.find(storage.getInt(RecordingVoiceEffectKey, RecordingVoiceEffects.normal.code));
            showFeaturesSettingsMenuIcon = storage.getBoolean(ShowFeaturesSettingsMenuIconKey, true);
            showClearCacheMenuIcon = storage.getBoolean(ShowClearCacheMenuIconKey, true);
            showMarkAllAsReadMenuIcon = storage.getBoolean(ShowMarkAllAsReadMenuIconKey, true);
        }
    }

    public static class ChatsSettings extends BaseSettings
    {
        public enum ShowToastModes
        {
            All(0),
            Contacts(1),
            Favorites(2),
            ContactsAndFavorites(3),;

            public final int code;

            ShowToastModes(int code)
            {
                this.code = code;
            }

            public static ShowToastModes find(int code)
            {
                for (ShowToastModes value : ShowToastModes.values())
                {
                    if (value.code == code)
                        return value;
                }

                return All;
            }
        }

        public static final String ShowChatStatusIndicatorKey = "ShowChatStatusIndicator";
        public static final String ShowTypingKey = "ShowTyping";
        public static final String ShowTypingToast = "ShowTypingToast";
        public static final String ShowOnlineToast = "ShowOnlineToast";
        public static final String ShowOfflineToast = "ShowOfflineToast";
        public static final String ShowToastMode = "ShowToastMode";
        public static final String ShowProxyAdsChannelKey = "ShowProxyAdsChannel";
        public static final String ConfirmSendVoiceMessageKey = "ConfirmSendVoiceMessage";
        public static final String MaxPinnedDialogsCountKey = "MaxPinnedDialogsCount";

        public boolean showChatStatusIndicator;
        public boolean showTyping;
        public boolean showTypingToast;
        public boolean showOnlineToast;
        public boolean showOfflineToast;
        public ShowToastModes showToastMode;
        public boolean showProxyAdsChannel;
        public boolean confirmSendVoiceMessage;
        public int maxPinnedDialogsCount;

        private ChatsSettings()
        {
            super(ChatsSettings.class.getName());
            showChatStatusIndicator = storage.getBoolean(ShowChatStatusIndicatorKey, true);
            showTyping = storage.getBoolean(ShowTypingKey, true);
            showTypingToast = storage.getBoolean(ShowTypingToast, true);
            showOnlineToast = storage.getBoolean(ShowOnlineToast, true);
            showOfflineToast = storage.getBoolean(ShowOfflineToast, true);
            showToastMode = ShowToastModes.find(storage.getInt(ShowToastMode, ShowToastModes.ContactsAndFavorites.code));
            showProxyAdsChannel = storage.getBoolean(ShowProxyAdsChannelKey, false);
            confirmSendVoiceMessage = storage.getBoolean(ConfirmSendVoiceMessageKey, true);
            maxPinnedDialogsCount = storage.getInt(MaxPinnedDialogsCountKey, 10);
        }
    }

    public static class TabSettings extends BaseSettings
    {
        public static final String HideAll = "HideAll";
        public static final String HideUsers = "HideUsers";
        public static final String HideGroups = "HideGroups";
        public static final String HideSuperGroups = "HideSuperGroups";
        public static final String HideChannels = "HideChannels";
        public static final String HideBots = "HideBots";
        public static final String HideFavorites = "HideFavorites";
        public static final String HideAdmins = "HideAdmins";
        public static final String HideUnreads = "HideUnreads";
        public static final String CurrentTab = "CurrentTab";
        public static final String TabsCountersCountNotMuted = "TabsCountersCountNotMuted";
        public static final String TabsCountersCountChats = "TabsCountersCountChats";
        public static final String HideTabsCounters = "HideTabsCounters";
        public static final String HideTabs = "HideTabs";
        public static final String LimitTabsCounters = "LimitTabsCounters";
        public static final String TabsShouldExpand = "TabsShouldExpand";
        public static final String ChatsTabCounterSize = "ChatsTabCounterSize";
        public static final String TabsTextSize = "TabsTextSize";
        public static final String TabsToBottom = "TabsToBottom";
        public static final String TabsHeight = "TabsHeight";
        public static final String DisableTabsScrolling = "DisableTabsScrolling";
        public static final String ActionBarCastShadowsKey = "ActionBarCastShadows";

        public boolean hideTabs;
        public boolean tabsToBottom;
        public boolean hideALl;
        public boolean hideUsers;
        public boolean hideGroups;
        public boolean hideSuperGroups;
        public boolean hideChannels;
        public boolean hideBots;
        public boolean hideFavorites;
        public boolean hideAdmins;
        public boolean hideUnreads;
        public boolean hideTabsCounters;
        public boolean tabsCountersCountNotMuted;
        public boolean tabsCountersCountChats;
        public boolean limitTabsCounters;
        public boolean tabsShouldExpand;
        public int chatsTabCounterSize;
        public int tabsTextSize;
        public int tabsHeight;
        public boolean disableTabsScrolling;
        public boolean actionBarCastShadows;
        public int currentTab;


        private TabSettings()
        {
            super(TabSettings.class.getName());
            tabsToBottom = getBool(TabsToBottom, false);
            hideTabs = getBool(HideTabs, false);
            hideALl = getBool(HideAll, false);
            hideUsers = getBool(HideUsers, false);
            hideGroups = getBool(HideGroups, false);
            hideSuperGroups = getBool(HideSuperGroups, false);
            hideChannels = getBool(HideChannels, false);
            hideBots = getBool(HideBots, false);
            hideFavorites = getBool(HideFavorites, false);
            hideAdmins = getBool(HideAdmins, true);
            hideUnreads = getBool(HideUnreads, true);
            hideTabsCounters = getBool(HideTabsCounters, false);
            tabsCountersCountNotMuted = getBool(TabsCountersCountNotMuted, true);
            tabsCountersCountChats = getBool(TabsCountersCountChats, false);
            limitTabsCounters = getBool(LimitTabsCounters, false);
            tabsShouldExpand = getBool(TabsShouldExpand, true);
            chatsTabCounterSize = getInt(ChatsTabCounterSize, AndroidUtilities.isTablet() ? 13 : 11);
            tabsTextSize = getInt(TabsTextSize, 14);
            tabsHeight = getInt(TabsHeight, 40);
            disableTabsScrolling = getBool(DisableTabsScrolling, false);
            actionBarCastShadows = storage.getBoolean(ActionBarCastShadowsKey, true);
            currentTab = getInt(CurrentTab, 0);
        }
    }
}
