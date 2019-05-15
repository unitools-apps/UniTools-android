package com.github.ali77gh.unitools.ui.adapter;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.core.tools.DateTimeTools;
import com.github.ali77gh.unitools.data.model.Friend;
import com.github.ali77gh.unitools.data.model.UClass;

import java.util.List;

public class WallFriendAdapter extends BaseAdapter {

    private Activity _activity;
    private List<Friend> friends;

    public WallFriendAdapter(Activity activity, List<Friend> friends) {
        this._activity = activity;
        this.friends = friends;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    }

    @Override
    public int getCount() {
        return friends.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {


        ViewGroup cview = (ViewGroup) _activity.getLayoutInflater().inflate(R.layout.item_home_friend, null);
        Friend friend = friends.get(i);

        TextView name = (TextView) cview.getChildAt(0);
        TextView available = (TextView) cview.getChildAt(1);

        name.setText(friend.name);
        if (AvailableToday(friend)) available.setVisibility(View.VISIBLE);

        return cview;
    }

    private boolean AvailableToday(Friend friend) {
        int today = DateTimeTools.getCurrentTime().dayOfWeek;

        for (UClass uClass : friend.classList) {
            if (uClass.time.dayOfWeek == today) return true;
        }
        return false;
    }
}
