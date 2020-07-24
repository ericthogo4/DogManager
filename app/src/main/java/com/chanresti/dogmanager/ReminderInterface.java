package com.chanresti.dogmanager;

public interface ReminderInterface {

    public void hideActionBarAFAB();
    public void showActionBarAFAB();
    public void addReminder(final String reminderTitle, final String reminderDTS, final long reminderTIM);
    public void updateReminder(final String reminderTitle, final String reminderDTS, final long reminderTIM, final int reminderId, final int reminderPosition);

}
