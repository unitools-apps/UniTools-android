package com.github.ali77gh.unitools.ui.fragments;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.core.CH;
import com.github.ali77gh.unitools.core.MyDataBeen;
import com.github.ali77gh.unitools.core.Translator;
import com.github.ali77gh.unitools.core.tools.DateTimeTools;
import com.github.ali77gh.unitools.core.tools.Sort;
import com.github.ali77gh.unitools.data.model.Event;
import com.github.ali77gh.unitools.data.model.Friend;
import com.github.ali77gh.unitools.data.model.UClass;
import com.github.ali77gh.unitools.data.repo.EventRepo;
import com.github.ali77gh.unitools.data.repo.FriendRepo;
import com.github.ali77gh.unitools.data.repo.UClassRepo;
import com.github.ali77gh.unitools.data.repo.UserInfoRepo;
import com.github.ali77gh.unitools.ui.adapter.WallFriendAdapter;
import com.github.ali77gh.unitools.ui.animation.ExpandAndCollapse;
import com.github.ali77gh.unitools.ui.animation.FadeLoop;
import com.github.ali77gh.unitools.ui.dialogs.AddClassDialog;
import com.github.ali77gh.unitools.ui.dialogs.AddEventDialog;
import com.github.ali77gh.unitools.ui.dialogs.AddFriendDialog;
import com.github.ali77gh.unitools.ui.dialogs.ClassInfoDialog;
import com.github.ali77gh.unitools.ui.dialogs.EventInfoDialog;
import com.github.ali77gh.unitools.ui.dialogs.FriendInfoDialog;
import com.github.ali77gh.unitools.ui.dialogs.SearchFriendDialog;
import com.github.ali77gh.unitools.ui.dialogs.SetupWeekCounterDialog;
import com.github.ali77gh.unitools.ui.dialogs.ShareClassesDialog;
import com.github.ali77gh.unitools.ui.view.NonScrollListView;
import com.github.ali77gh.unitools.ui.widget.ShowNextClassWidget;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

;


public class WallFragment extends Fragment implements Backable {

    private LinearLayout weekNumberParent;

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
    private ImageView searchFriendBtn;
    private NonScrollListView friendsList;
    private TextView friendsFirstRow;

    private FriendInfoDialog friendInfoDialog;

    private Timer timer = new Timer();

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

        weekNumberParent = cView.findViewById(R.id.linear_wall_week_number_parent);

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
        searchFriendBtn = cView.findViewById(R.id.btn_wall_search_friend);
        expandFriendsBtn = cView.findViewById(R.id.image_wall_expand_friends);
        friendsList = cView.findViewById(R.id.listview_home_friends_expandble);
        friendsFirstRow = cView.findViewById(R.id.text_friends_first_row);


        SetupWeekCounter();

        SetupClassesList();
        SetupEventsList();
//        SetupFriendsList(); //this called from onResume()

        SetupExpandCollapse();
        SetupAddAndShare();
        SetupRefreshLoop();

        return cView;
    }

    @Override
    public void onResume() {
        super.onResume();
        SetupFriendsList();
    }

    private FadeLoop weekCounterFadeLoop;
    private void SetupWeekCounter() {
        weekNumberParent.setOnClickListener(view -> {
            SetupWeekCounterDialog dialog = new SetupWeekCounterDialog(getActivity());
            dialog.show();
            dialog.setOnDismissListener(dialogInterface -> SetupWeekCounter());
        });

        TextView text = (TextView) weekNumberParent.getChildAt(0);
        ProgressBar progress = (ProgressBar) weekNumberParent.getChildAt(1);

        int currentWeek = UserInfoRepo.getWeekNumber();
        String currentWeekString = Translator.getWeekNumberString(currentWeek);

        if (currentWeekString.equals(CH.getString(R.string.not_set))) {
            weekCounterFadeLoop = new FadeLoop(text, Integer.MAX_VALUE, 1000);
            weekCounterFadeLoop.setMinFade((float) 0.3);
            weekCounterFadeLoop.animate();
        } else {
            if (weekCounterFadeLoop != null)
                weekCounterFadeLoop.stopAnimate();
        }

        text.setText(currentWeekString + " : " + Translator.getDayString(DateTimeTools.getCurrentDayOfWeek()));
        progress.setProgress((int) (((float) currentWeek / 16) * 100));
    }

    private void SetupClassesList() {

        // load to list
        classesFirstRow.setText(getString(R.string.there_is_no_class_yet));
        List<String> classesString = new ArrayList<>();
        List<UClass> uClasses = UClassRepo.getAll();
        Sort.SortClass(uClasses);
        for (UClass uClass : uClasses) {
            if (uClasses.indexOf(uClass) == 0)
                classesFirstRow.setText(Translator.getUClassReadable(uClass));
            else classesString.add(Translator.getUClassReadable(uClass));
        }
        ArrayAdapter<String> classesStringAdapter = new ArrayAdapter<>(getActivity(), R.layout.item_home_global, classesString);
        classesList.setAdapter(classesStringAdapter);

        // first row click
        classesFirstRow.setOnClickListener(view -> {

            if (uClasses.size() == 0) return;

            ClassInfoDialog infoDialog = new ClassInfoDialog(getActivity(), uClasses.get(0), () -> {
                //on delete
                UClassRepo.Remove(uClasses.get(0).id);
                SetupClassesList();
            });
            infoDialog.setEditListener(uClass -> {
                uClass.id = uClasses.get(0).id;
                UClassRepo.Update(uClass);
                SetupClassesList();
            });
            infoDialog.show();
        });

        // on list click
        classesList.setOnItemClickListener((adapterView, view, i, l) -> {
            ClassInfoDialog infoDialog = new ClassInfoDialog(getActivity(), uClasses.get(i + 1), () -> {
                //on delete
                UClassRepo.Remove(uClasses.get(i + 1).id);
                SetupClassesList();
            });
            infoDialog.setEditListener(uClass -> {
                uClass.id = uClasses.get(i + 1).id;
                UClassRepo.Update(uClass);
                SetupClassesList();
            });
            infoDialog.show();
        });

        // on first row long click
        classesFirstRow.setOnLongClickListener(view -> {

            if (uClasses.size() == 0) return true;

            AddClassDialog addClassDialog = new AddClassDialog(getActivity(), uClasses.get(0));
            addClassDialog.setListener(uClass -> {
                uClass.id = uClasses.get(0).id;
                UClassRepo.Update(uClass);
                SetupClassesList();

            });
            addClassDialog.show();
            CH.toast(R.string.enter_time_in_24_system);
            return true;
        });

        // on list long click (edit)
        classesList.setOnItemLongClickListener((adapterView, view, i, l) -> {
            AddClassDialog addClassDialog = new AddClassDialog(getActivity(), uClasses.get(i + 1));
            addClassDialog.setListener(uClass -> {
                uClass.id = uClasses.get(i + 1).id;
                UClassRepo.Update(uClass);
                SetupClassesList();
            });
            addClassDialog.show();
            CH.toast(R.string.enter_time_in_24_system);
            return true;
        });
    }

    private void SetupEventsList() {

        //load to list
        eventsFirstRow.setText(getString(R.string.you_have_no_event_yet));
        List<String> eventsString = new ArrayList<>();
        List<Event> events = EventRepo.getAll();
        Sort.SortEvent(events);
        for (Event event : events) {
            if (events.indexOf(event) == 0)
                eventsFirstRow.setText(getResources().getString(R.string.next) + " " + Translator.getEventReadable(event));
            else eventsString.add(Translator.getEventReadable(event));
        }
        ArrayAdapter<String> eventsStringAdapter = new ArrayAdapter<>(getActivity(), R.layout.item_home_global, new ArrayList<>(eventsString));
        eventsList.setAdapter(eventsStringAdapter);

        //first row click
        eventsFirstRow.setOnClickListener(view -> {

            if (events.size() == 0) return;

            EventInfoDialog infoDialog = new EventInfoDialog(getActivity(), events.get(0), () -> {
                //on delete
                EventRepo.Remove(events.get(0).id);
                SetupEventsList();
            });
            infoDialog.setEditListener(event -> {
                event.id = events.get(0).id;
                EventRepo.Update(event);
                SetupEventsList();
            });
            infoDialog.show();
        });

        // on list click
        eventsList.setOnItemClickListener((adapterView, view, i, l) -> {
            EventInfoDialog infoDialog = new EventInfoDialog(getActivity(), events.get(i + 1), () -> {
                //on delete
                EventRepo.Remove(events.get(i + 1).id);
                SetupEventsList();
            });
            infoDialog.setEditListener(event -> {
                event.id = events.get(i + 1).id;
                EventRepo.Update(event);
                SetupEventsList();
            });
            infoDialog.show();
        });

        // on first row long click
        eventsFirstRow.setOnLongClickListener(view -> {

            if (events.size() == 0) return true;

            AddEventDialog addClassDialog = new AddEventDialog(getActivity(), events.get(0));
            addClassDialog.setListener(event -> {
                event.id = events.get(0).id;
                EventRepo.Update(event);
                SetupEventsList();
                updateWidgets();
            });
            addClassDialog.show();
            CH.toast(R.string.enter_time_in_24_system);
            return true;
        });

        // on list long click (edit)
        eventsList.setOnItemLongClickListener((adapterView, view, i, l) -> {
            AddEventDialog addClassDialog = new AddEventDialog(getActivity(), events.get(0));
            addClassDialog.setListener(event -> {
                event.id = events.get(0).id;
                EventRepo.Update(event);
                SetupEventsList();
                updateWidgets();
            });
            addClassDialog.show();
            CH.toast(R.string.enter_time_in_24_system);
            return true;
        });
    }

    private void SetupFriendsList() {


        List<Friend> friends = FriendRepo.getAll();

        if (friends.size() == 0) {
            friendsFirstRow.setText(getString(R.string.you_have_no_friends_yet));
        } else {
            friendsFirstRow.setText("");
        }


        friendsList.setAdapter(new WallFriendAdapter(getActivity(), friends));

        friendsList.setOnItemClickListener((adapterView, view, i, l) -> {
            friendInfoDialog = new FriendInfoDialog(getActivity(), friends.get(i), () -> {
                //on delete
                FriendRepo.Remove(friends.get(i).id);
                SetupFriendsList();
            });
            friendInfoDialog.show();
        });
    }

    private void SetupExpandCollapse() {

        expandClassesBtn.setOnClickListener(view -> {
            LinearLayout parent = (LinearLayout) classesList.getParent();
            if (parent.getVisibility() != View.VISIBLE) {
                // expand
                if (UClassRepo.getAll().size() > 1) {
                    ExpandAndCollapse.expand(parent);
                    expandClassesBtn.animate().rotation(180).setStartDelay(200).start();
                } else{
                    CH.toast(R.string.you_have_no_more_classes);
                    new FadeLoop(addClassBtn,3).animate();
                }

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
                } else{
                    CH.toast(R.string.you_have_no_more_events);
                    new FadeLoop(addEventBtn,3).animate();
                }

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
                if (FriendRepo.getAll().size() > 0) {
                    ExpandAndCollapse.expand(parent);
                    expandFriendsBtn.animate().rotation(180).setStartDelay(200).start();
                } else{
                    CH.toast(R.string.you_have_no_more_friends);
                    new FadeLoop(addFriendBtn,3).animate();
                }

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
            AddClassDialog addClassDialog = new AddClassDialog(getActivity(), null);
            addClassDialog.setListener(uClass -> {
                UClassRepo.Insert(uClass);
                SetupClassesList();
                updateWidgets();
            });
            addClassDialog.show();
            CH.toast(R.string.enter_time_in_24_system);
        });

        shareClassesBtn.setOnClickListener(view -> {
            if (UClassRepo.getAll().size() == 0) {
                CH.toast(R.string.you_have_no_more_classes);
                return;
            }
            new ShareClassesDialog(getActivity()).show();
        });

        //events

        addEventBtn.setOnClickListener(view -> {
            AddEventDialog addEventDialog = new AddEventDialog(getActivity(), null);
            addEventDialog.setListener(event -> {
                EventRepo.Insert(event);
                SetupEventsList();
            });
            addEventDialog.show();
            CH.toast(R.string.enter_time_in_24_system);
        });

        //friends

        addFriendBtn.setOnClickListener(view -> {
            AddFriendDialog addFriendDialog = new AddFriendDialog(getActivity());
            addFriendDialog.setListener(friend -> {
                FriendRepo.Insert(friend);
                SetupFriendsList();
            });
            addFriendDialog.show();
        });

        searchFriendBtn.setOnClickListener(v -> new SearchFriendDialog(getActivity()).show());
    }

    public void OnBarcodeReaded(String text) {
        friendInfoDialog.OnFriendStringReady(text);
        MyDataBeen.onNewAddFriendWithQRCode();
    }

    protected void updateWidgets() {
        RemoteViews remoteViews = new RemoteViews(getActivity().getPackageName(), R.layout.widget_show_next_class);
        ComponentName thisWidget = new ComponentName(getActivity(), ShowNextClassWidget.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(getActivity());
        int[] appWidgetIds = manager.getAppWidgetIds(thisWidget);
        manager.partiallyUpdateAppWidget(appWidgetIds, remoteViews);

        ShowNextClassWidget.Update(getActivity(), manager, appWidgetIds);
    }

    private void SetupRefreshLoop() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    getActivity().runOnUiThread(() -> SetupClassesList());
                } catch (RuntimeException e) {
                    Log.e("internal", e.getMessage() + "at wallFragment.SetupRefreshLoop()");
                }

            }
        }, 60000, 60000);
    }

    @Override
    public void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }

    @Override
    public boolean onBack() {
        return false;
    }
}
