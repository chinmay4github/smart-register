package com.hermit.starreg;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabAccesorAdapter extends FragmentPagerAdapter {


    public TabAccesorAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {

        switch (i){

            case 0:
                ChatsFragment chatsFragment = new ChatsFragment();
                return chatsFragment;
            case 1:
                GroupsFragment groupsFragment = new GroupsFragment();
                return groupsFragment;
            case 2:
                ContactsFragment contactsFragment =new ContactsFragment();
                return contactsFragment;
                default:
                    return null;




        }


    }

    @Override
    public int getCount() {
        return 3;
    }

    public CharSequence getPageTitle(int position){


        switch(position){
            case 0:
                return "Chats";
            case 1:
                return "Groups";
            case 2:
                return "Contacts";

            default:
                return null;



        }


    }
}
