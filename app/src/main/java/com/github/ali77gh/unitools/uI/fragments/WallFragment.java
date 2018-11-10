package com.github.ali77gh.unitools.uI.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.core.ContextHolder;
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
import com.github.ali77gh.unitools.uI.dialogs.SetupWeekCounterDialog;
import com.github.ali77gh.unitools.uI.dialogs.ShareClassesDialog;
import com.github.ali77gh.unitools.uI.view.NonScrollListView;

import java.util.ArrayList;
import java.util.List;


public class WallFragment extends Fragment implements Backable {

    private ImageView setWeekCounter;
    private LinearLayout weekNummberParent;

    private ImageView expandClassesBtn;
    private ImageView shareClassesBtn;
    private ImageView addClassBtn;
    private NonScrollListView classesList;
    private TextView classesFirstRow;

    private ImageView expandEventsBtn;
    private ImageView addEventBtn;
    private NonScrollListView eventsList;
    private TextView eventsFirstRow;

    private ImageView expandFriendsBtn;
    private ImageView addFriendBtn;
    private NonScrollListView friendsList;
    private TextView friendsFirstRow;
    private AddFriendDialog addFriendDialog;


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

        setWeekCounter = cView.findViewById(R.id.btn_wall_setup_week_counter);
        weekNummberParent = cView.findViewById(R.id.linear_wall_week_number_parent);

        addClassBtn = cView.findViewById(R.id.btn_wall_add_class);
        shareClassesBtn = cView.findViewById(R.id.btn_wall_share_class);
        expandClassesBtn = cView.findViewById(R.id.image_wall_expand_classes);
        classesList = cView.findViewById(R.id.listview_home_classes_expandble);
        classesFirstRow = cView.findViewById(R.id.text_classes_first_row);

        addEventBtn = cView.findViewById(R.id.btn_wall_add_event);
        expandEventsBtn = cView.findViewById(R.id.image_wall_expand_events);
        eventsList = cView.findViewById(R.id.listview_home_events_expandble);
        eventsFirstRow = cView.findViewById(R.id.text_event_first_row);

        addFriendBtn = cView.findViewById(R.id.btn_wall_add_friend);
        expandFriendsBtn = cView.findViewById(R.id.image_wall_expand_friends);
        friendsList = cView.findViewById(R.id.listview_home_friends_expandble);
        friendsFirstRow = cView.findViewById(R.id.text_friends_first_row);


        SetupWeekCounter();
        SetupListsAndFirstRow();
        SetupExpandCollapse();
        SetupAddAndShare();

        return cView;
    }

    private void SetupWeekCounter() {
        setWeekCounter.setOnClickListener(view -> {
            SetupWeekCounterDialog dialog = new SetupWeekCounterDialog(getActivity());
            dialog.show();
            dialog.setOnDismissListener(dialogInterface -> {
                SetupWeekCounter();
            });
        });

        TextView text = (TextView) weekNummberParent.getChildAt(0);
        ProgressBar progress = (ProgressBar) weekNummberParent.getChildAt(1);

        int currentWeek = UserInfoRepo.getWeekNumber();

        text.setText(Translator.getWeekNumberString(currentWeek));
        progress.setProgress((int) (((float) currentWeek / 16) * 100));
    }

    private void SetupListsAndFirstRow() {

        //classes
        List<String> classesString = new ArrayList<>();
        List<UClass> uClasses = UserInfoRepo.getUserInfo().Classes;
        // todo test sort classes with time
        Sort.SortClass(uClasses);
        for (UClass uClass : uClasses) {
            if (uClasses.indexOf(uClass) == 0)
                classesFirstRow.setText(getResources().getString(R.string.next) + " " + Translator.getUClassReadable(uClass));
            else classesString.add(Translator.getUClassReadable(uClass));
        }
        ArrayAdapter<String> classesStringAdapter = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, classesString);
        classesList.setAdapter(classesStringAdapter);
        classesList.setOnItemLongClickListener((adapterView, view, i, l) -> {
            Toast.makeText(getActivity(), String.valueOf(i), Toast.LENGTH_SHORT).show();
            return true; // search about this
        });

        //events
        List<String> eventsString = new ArrayList<>();
        List<Event> events = EventRepo.getAll();
        //todo sort events with time
        for (Event event : events) {
            if (events.indexOf(event) == 0)
                eventsFirstRow.setText(getResources().getString(R.string.next) + " " + Translator.getEventReadable(event));
            else eventsString.add(Translator.getEventReadable(event));
        }
        ArrayAdapter<String> eventsStringAdapter = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, new ArrayList<>(eventsString));
        eventsList.setAdapter(eventsStringAdapter);

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
    }

    private void SetupExpandCollapse() {

        expandClassesBtn.setOnClickListener(view -> {
            LinearLayout parent = (LinearLayout) classesList.getParent();
            if (parent.getVisibility() != View.VISIBLE) {
                // expand
                if (UserInfoRepo.getUserInfo().Classes.size() > 1) {
                    ExpandAndCollapse.expand(parent);
                    expandClassesBtn.animate().rotation(180).setStartDelay(200).start();
                } else
                    Toast.makeText(getActivity(), getString(R.string.you_have_no_more_classes), Toast.LENGTH_LONG).show();

            } else {
                // collapse
                ExpandAndCollapse.collapse(parent);
                expandClassesBtn.animate().rotation(0).setStartDelay(200).start();
            }

        });

        expandEventsBtn.setOnClickListener(view -> {
            LinearLayout parent = (LinearLayout) eventsList.getParent();
            if (parent.getVisibility() != View.VISIBLE) {
                // expand
                if (EventRepo.getAll().size() > 1) {
                    ExpandAndCollapse.expand(parent);
                    expandEventsBtn.animate().rotation(180).setStartDelay(200).start();
                } else
                    Toast.makeText(getActivity(), getString(R.string.you_have_no_more_events), Toast.LENGTH_LONG).show();

            } else {
                // collapse
                ExpandAndCollapse.collapse(parent);
                expandEventsBtn.animate().rotation(0).setStartDelay(200).start();
            }

        });

        expandFriendsBtn.setOnClickListener(view -> {
            LinearLayout parent = (LinearLayout) friendsList.getParent();
            if (parent.getVisibility() != View.VISIBLE) {
                // expand
                if (FriendRepo.getAll().size() > 1) {
                    ExpandAndCollapse.expand(parent);
                    expandFriendsBtn.animate().rotation(180).setStartDelay(200).start();
                } else
                    Toast.makeText(getActivity(), getString(R.string.you_have_no_more_friends), Toast.LENGTH_LONG).show();

            } else {
                // collapse
                ExpandAndCollapse.collapse(parent);
                expandFriendsBtn.animate().rotation(0).setStartDelay(200).start();
            }

        });

    }

    private void SetupAddAndShare() {

        //classes

        addClassBtn.setOnClickListener(view -> {
            AddClassDialog addFriendDialog = new AddClassDialog(getActivity());
            addFriendDialog.setListener(uClass -> {
                UserInfoRepo.AddClass(uClass);
                SetupListsAndFirstRow();
            });
            addFriendDialog.show();
        });

        shareClassesBtn.setOnClickListener(view -> {
            new ShareClassesDialog(getActivity()).show();
        });

        //events

        addEventBtn.setOnClickListener(view -> {
            AddEventDialog addEventDialog = new AddEventDialog(getActivity());
            addEventDialog.setListener(event -> {
                EventRepo.insert(event);
                SetupListsAndFirstRow();
            });
            addEventDialog.show();
        });

        //friends

        addFriendBtn.setOnClickListener(view -> {
            addFriendDialog = new AddFriendDialog(getActivity(), this);
            addFriendDialog.setListener(friend -> {
                FriendRepo.insert(friend);
                SetupListsAndFirstRow();
            });
            addFriendDialog.show();
        });

    }

    public void OnBarcodeReaded(String text) {
        addFriendDialog.onFriendStringReady(text);
    }

    @Override
    public boolean onBack() {
        Toast.makeText(ContextHolder.getAppContext(), "back detect on wall", Toast.LENGTH_SHORT).show();
        return true;
    }
}
