package com.github.ali77gh.unitools.uI.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.core.Translator;
import com.github.ali77gh.unitools.data.Model.Event;
import com.github.ali77gh.unitools.data.Model.Friend;
import com.github.ali77gh.unitools.data.Model.UClass;
import com.github.ali77gh.unitools.data.Model.UserInfo;
import com.github.ali77gh.unitools.data.Repo.EventRepo;
import com.github.ali77gh.unitools.data.Repo.FriendRepo;
import com.github.ali77gh.unitools.data.Repo.UserInfoRepo;
import com.github.ali77gh.unitools.uI.animation.ExpandAndCollapse;
import com.github.ali77gh.unitools.uI.dialogs.AddClassDialog;
import com.github.ali77gh.unitools.uI.view.NonScrollListView;

import java.util.ArrayList;
import java.util.List;


public class WallFragment extends Fragment {

    private Button classesAddBtn;
    private Button classesShowAllBtn;
    private NonScrollListView classesList;

    private Button friendAddBtn;
    private Button friendShowAllBtn;
    private NonScrollListView friendsList;

    private Button eventAddBtn;
    private Button eventShowAllBtn;
    private NonScrollListView eventsList;

    public WallFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View cView = inflater.inflate(R.layout.fragment_wall, container, false);

        classesAddBtn = cView.findViewById(R.id.btn_home_classes_add);
        classesShowAllBtn = cView.findViewById(R.id.btn_home_classes_show_all);
        classesList = cView.findViewById(R.id.listview_home_classes_expandble);

        friendAddBtn = cView.findViewById(R.id.btn_home_friends_add);
        friendShowAllBtn = cView.findViewById(R.id.btn_home_friends_show_all);
        friendsList = cView.findViewById(R.id.listview_home_friends_expandble);

        eventAddBtn = cView.findViewById(R.id.btn_home_events_add);
        eventShowAllBtn = cView.findViewById(R.id.btn_home_events_show_all);
        eventsList = cView.findViewById(R.id.listview_home_events_expandble);

        SetupListsAndFirstRow();
        SetupExpandCollapse();
        SetupAdd();

        return cView;
    }

    private void SetupListsAndFirstRow() {

        List<String> items = new ArrayList<>();
        for (UClass uClass :  UserInfoRepo.getUserInfo().Classes){
            items.add(Translator.getClassReadable(uClass));
        }
        ArrayAdapter<String> classesStringAdapter = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, items);
        classesList.setAdapter(classesStringAdapter);


        items.clear();
        for (Friend friend : FriendRepo.getAll()){
            items.add(friend.name);
        }
        ArrayAdapter<String> friendsStringAdapter = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, items);
        friendsList.setAdapter(friendsStringAdapter);


        items.clear();
        for (Event event : EventRepo.getAll()){
            items.add(event.title);
        }
        ArrayAdapter<String> eventsStringAdapter = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, items);
        eventsList.setAdapter(eventsStringAdapter);
    }

    private void SetupExpandCollapse() {

        classesShowAllBtn.setOnClickListener(view -> {
            if (classesList.getVisibility() != View.VISIBLE) {
                // expand
                if (classesList.getChildCount()>1) {
                    ExpandAndCollapse.expand(classesList);
                    classesShowAllBtn.setText(getString(R.string.show_less));
                } else
                    Toast.makeText(getActivity(), getString(R.string.you_have_no_more_classes), Toast.LENGTH_LONG).show();

            } else {
                // collapse
                ExpandAndCollapse.collapse(classesList);
                classesShowAllBtn.setText(getString(R.string.show_all));
            }

        });

        friendShowAllBtn.setOnClickListener(view -> {
            if (friendsList.getVisibility() != View.VISIBLE) {
                // expand
                if (friendsList.getChildCount()>1) {
                    ExpandAndCollapse.expand(friendsList);
                    friendShowAllBtn.setText(getString(R.string.show_less));
                } else
                    Toast.makeText(getActivity(), getString(R.string.you_have_no_more_friends), Toast.LENGTH_LONG).show();

            } else {
                // collapse
                ExpandAndCollapse.collapse(friendsList);
                friendShowAllBtn.setText(getString(R.string.show_all));
            }

        });

        eventShowAllBtn.setOnClickListener(view -> {
            if (eventsList.getVisibility() != View.VISIBLE) {
                // expand
                if (friendsList.getChildCount()>1) {
                    ExpandAndCollapse.expand(eventsList);
                    eventShowAllBtn.setText(getString(R.string.show_less));
                } else
                    Toast.makeText(getActivity(), getString(R.string.you_have_no_more_events), Toast.LENGTH_LONG).show();

            } else {
                // collapse
                ExpandAndCollapse.collapse(eventsList);
                eventShowAllBtn.setText(getString(R.string.show_all));
            }

        });

    }

    private void SetupAdd() {

        classesAddBtn.setOnClickListener(view -> {
            Toast.makeText(getActivity(), "todo show dialog", Toast.LENGTH_SHORT).show();
            AddClassDialog addFriendDialog = new AddClassDialog(getActivity());
            addFriendDialog.setListener(uClass ->{
                Toast.makeText(getActivity(), Translator.getClassReadable(uClass), Toast.LENGTH_SHORT).show();
            });
            addFriendDialog.show();
        });

        friendAddBtn.setOnClickListener(view -> {
            Toast.makeText(getActivity(), "todo show dialog", Toast.LENGTH_SHORT).show();
//            AddFriendDialog addFriendDialog = new AddFriendDialog(getActivity());
//            addFriendDialog.setListener(new AddFriendDialog.AddClassDialogListener() {
//                @Override
//                public void onNewClass(Friend friend) {
//
//                }
//
//                @Override
//                public void onCancel() {
//
//                }
//            });
//            addFriendDialog.show();
        });

        eventAddBtn.setOnClickListener(view -> {
            Toast.makeText(getActivity(), "todo show dialog", Toast.LENGTH_SHORT).show();
        });
    }
}
