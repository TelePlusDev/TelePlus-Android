package org.telegram.messenger;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class Favorite
{
    private static Favorite Instance = null;

    private ArrayList<Long> favoriteChatsList;
    private ArrayList<Long> hiddenChatsList;
    private ArrayList<Integer> usersNotificationsExceptions;

    public static Favorite getInstance()
    {
        Favorite localInstance = Instance;
        if (localInstance == null)
        {
            Instance = localInstance = new Favorite();
        }
        return localInstance;
    }

    public Favorite()
    {
        favoriteChatsList = ApplicationLoader.databaseHandler.getFavoriteChatsList();
        hiddenChatsList = ApplicationLoader.databaseHandler.getHiddenChatsList();
        usersNotificationsExceptions = ApplicationLoader.databaseHandler.getUsersNotificationsExceptions();
    }

    public void addFavorite(Long id)
    {
        favoriteChatsList.add(id);
        ApplicationLoader.databaseHandler.addFavorite(id);
    }

    public void addHidden(Long id)
    {
        hiddenChatsList.add(id);
        ApplicationLoader.databaseHandler.addHiddenChat(id);
    }

    public void deleteFavorite(Long id)
    {
        favoriteChatsList.remove(id);
        ApplicationLoader.databaseHandler.deleteFavorite(id);
    }

    public void deleteHidden(Long id)
    {
        hiddenChatsList.remove(id);
        ApplicationLoader.databaseHandler.deleteHiddenChat(id);
    }

    public void addUserNotificationException(Integer userId)
    {
        usersNotificationsExceptions.add(userId);
        ApplicationLoader.databaseHandler.addUserNotificationException(userId);
    }

    public void removeUserNotificationException(Integer userId)
    {
        usersNotificationsExceptions.remove(userId);
        ApplicationLoader.databaseHandler.deleteUserNotificationException(userId);
    }

    public boolean isFavorite(Long id)
    {
        return favoriteChatsList.contains(id);
    }

    public boolean isHidden(Long id)
    {
        return hiddenChatsList.contains(id);
    }

    public boolean isUserNotificationException(int id)
    {
        return usersNotificationsExceptions.contains(id);
    }
}
