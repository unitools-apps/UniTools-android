package com.github.ali77gh.unitools.uI.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.core.Translator;
import com.github.ali77gh.unitools.core.tools.Sort;
import com.github.ali77gh.unitools.data.Model.Event;
import com.github.ali77gh.unitools.data.Model.Friend;
import com.github.ali77gh.unitools.data.Model.UClass;
import com.github.ali77gh.unitools.data.Repo.EventRepo;
import com.github.ali77gh.unitools.data.Repo.FriendRepo;
import com.github.ali77gh.unitools.data.Repo.UserInfoRepo;
import com.github.ali77gh.unitools.uI.animation.ExpandAndCollapse;
import com.github.ali77gh.unitools.uI.dialogs.AddClassDialog;
import com.github.ali77gh.unitools.uI.dialogs.AddEventDialog;
import com.github.ali77gh.unitools.uI.dialogs.AddFriendDialog;
import com.github.ali77gh.unitools.uI.view.NonScrollListView;

import java.util.ArrayList;
import java.util.List;


public class WallFragment extends Fragment {

    private Button classesAddBtn;
    private Button classesShowAllBtn;
    private NonScrollListView classesList;
    private TextView classesFirstRow;

    private Button friendAddBtn;
    private Button friendShowAllBtn;
    private NonScrollListView friendsList;
    private TextView friendsFirstRow;
    private AddFriendDialog addFriendDialog;

    private Button eventAddBtn;
    private Button eventShowAllBtn;
    private NonScrollListView eventsList;
    private TextView eventsFirstRow;

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
        classesFirstRow = cView.findViewById(R.id.text_classes_first_row);

        friendAddBtn = cView.findViewById(R.id.btn_home_friends_add);
        friendShowAllBtn = cView.findViewById(R.id.btn_home_friends_show_all);
        friendsList = cView.findViewById(R.id.listview_home_friends_expandble);
        friendsFirstRow = cView.findViewById(R.id.text_friends_first_row);

        eventAddBtn = cView.findViewById(R.id.btn_home_events_add);
        eventShowAllBtn = cView.findViewById(R.id.btn_home_events_show_all);
        eventsList = cView.findViewById(R.id.listview_home_events_expandble);
        eventsFirstRow = cView.findViewById(R.id.text_event_first_row);

        SetupListsAndFirstRow();
        SetupExpandCollapse();
        SetupAdd();

        return cView;
    }

    private void SetupListsAndFirstRow() {

        //classes
        List<String> classesString = new ArrayList<>();
        List<UClass> uClasses = UserInfoRepo.getUserInfo().Classes;
        // todo test sort classes with time
        Sort.SortClass(uClasses);
        for (UClass uClass : uClasses) {
            if (uClasses.indexOf(uClass) == 0) classesFirstRow.setText(Translator.getUClassReadable(uClass));
            else classesString.add(Translator.getUClassReadable(uClass));
        }
        ArrayAdapter<String> classesStringAdapter = new ArrayAdapter<>(getActivity(), R.layout.item_spinner,classesString );
        classesList.setAdapter(classesStringAdapter);

        //friends
        List<String> friendsString = new ArrayList<>();
        List<Friend> friends = FriendRepo.getAll();
        // todo sort friends with time
        for (Friend friend : friends) {
            if (friends.indexOf(friend) == 0) friendsFirstRow.setText(friend.name);
            else friendsString.add(friend.name);
        }
        ArrayAdapter<String> friendsStringAdapter = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, new ArrayList<>(friendsString));
        friendsList.setAdapter(friendsStringAdapter);

        //events
        List<String> eventsString = new ArrayList<>();
        List<Event> events = EventRepo.getAll();
        //todo sort events with time
        for (Event event : events) {
            if (events.indexOf(event) == 0) eventsFirstRow.setText(Translator.getEventReadable(event));
            else eventsString.add(Translator.getEventReadable(event));
        }
        ArrayAdapter<String> eventsStringAdapter = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, new ArrayList<>(eventsString));
        eventsList.setAdapter(eventsStringAdapter);
    }

    private void SetupExpandCollapse() {

        classesShowAllBtn.setOnClickListener(view -> {
            if (classesList.getVisibility() != View.VISIBLE) {
                // expand
                if (UserInfoRepo.getUserInfo().Classes.size() > 1) {
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
                if (FriendRepo.getAll().size() > 1) {
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
                if (EventRepo.getAll().size() > 1) {
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
            AddClassDialog addFriendDialog = new AddClassDialog(getActivity());
            addFriendDialog.setListener(uClass -> {
                UserInfoRepo.AddClass(uClass);
                SetupListsAndFirstRow(); // todo refresh layout not tested
            });
            addFriendDialog.show();
        });

        friendAddBtn.setOnClickListener(view -> {
            addFriendDialog= new AddFriendDialog(getActivity(),this);
            addFriendDialog.setListener(friend -> {
                FriendRepo.insert(friend);
                SetupListsAndFirstRow(); // todo refresh layout not tested
            });
            addFriendDialog.show();
        });

        eventAddBtn.setOnClickListener(view -> {
            AddEventDialog addEventDialog = new AddEventDialog(getActivity());
            addEventDialog.setListener(event -> {
                EventRepo.insert(event);
                SetupListsAndFirstRow(); // todo refresh layout not tested
            });
            addEventDialog.show();
        });
    }

    public void OnBarcodeReaded(String text){
        addFriendDialog.onFriendStringReady(text);
    }
}
