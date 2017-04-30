package com.dotcom.jamaat.activity.Common;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.dotcom.jamaat.adapters.NotificationAdapter;
import com.dotcom.jamaat.util.Constants;
import com.dotcom.jamaat.util.SharedPreferencesManager;
import com.dotcom.jamaat.util.network.NetworkUtil;
import com.dotcom.jamaat.R;
import com.dotcom.jamaat.model.Notification;
import com.dotcom.jamaat.util.AppUtility;
import com.dotcom.jamaat.view.ShadowVerticalSpaceItemDecorator;
import com.dotcom.jamaat.view.VerticalSpaceItemDecorator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class NotificationActivity extends BaseActivity  {
    android.support.v7.widget.RecyclerView mListView;
    NotificationAdapter mAdapter;
    ArrayList<Notification> notificationArrayList;
    private ProgressDialog dialog;
    private NetworkUtil networkUtil;
    Context mContext;
    int today,eYear;
    String eMonth,fajr,sunrise,zawal,zuhrEnd,asrEnd,sunset,nisfulLail;
    private LinearLayout namazLL;
    TextView empty,dayTV,monthTV,miqaatTv,englishDayTv,englishMothTv,fajrTV,zuhurTv,asrTv,magribTv;
    boolean doubleBackToExitPressedOnce = false;
    private Toast toast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
//        notificationArrayList = new ArrayList<>();
        mContext = this;
        mListView = (android.support.v7.widget.RecyclerView) findViewById(R.id.notification_listView);
        empty = (TextView) findViewById(R.id.empty);
//        mListView.setEmptyView(empty);
        namazLL = (LinearLayout) findViewById(R.id.namazLL);
        fajrTV = (TextView) findViewById(R.id.fajrTime);
        zuhurTv = (TextView) findViewById(R.id.zoherStartTime);
        asrTv = (TextView) findViewById(R.id.asarTime);
        magribTv = (TextView) findViewById(R.id.magribTime);
        dayTV = (TextView) findViewById(R.id.day);
        englishDayTv = (TextView) findViewById(R.id.english_day);
        englishMothTv = (TextView) findViewById(R.id.english_month_year);
        miqaatTv = (TextView) findViewById(R.id.miqaatTv);
        monthTV = (TextView) findViewById(R.id.month_year);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("NOTIFICATIONS");
        Calendar calendar = Calendar.getInstance();
         today = calendar.get(Calendar.DAY_OF_MONTH);
//        String dayLongName = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
         eYear = calendar.get(Calendar.YEAR);
         eMonth = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
        // 4. Initialize ItemAnimator, LayoutManager and ItemDecorators
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mListView.setHasFixedSize(true);
        int verticalSpacing = 10;
        VerticalSpaceItemDecorator itemDecorator =
                new VerticalSpaceItemDecorator(verticalSpacing);
        ShadowVerticalSpaceItemDecorator shadowItemDecorator =
                new ShadowVerticalSpaceItemDecorator(this, R.drawable.drop_shadow);

    // 7. Set the LayoutManager
        mListView.setLayoutManager(layoutManager);

    // 8. Set the ItemDecorators
        mListView.addItemDecoration(shadowItemDecorator);
        mListView.addItemDecoration(itemDecorator);
        namazLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNamazDialog();
            }
        });

    }

    @Override
    protected String getTagName() {
        return null;
    }

    @Override
    public void showProgress(boolean show, String tag) {
        if (show) {
//            empty.setVisibility(View.VISIBLE);
//            empty.setText("");
            showDialog();
        } else{
//            empty.setVisibility(View.GONE);
            hideDialog();
        }
    }
    public void showDialog() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(true);
        dialog.show();
    }

    public void hideDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void onSuccess(JSONObject response, String tag) {
        Log.v("Notification result::",response.toString());
        if(tag.equalsIgnoreCase(Constants.NOTIFICATIONS_LIST)) {
            try {
                notificationArrayList = new ArrayList<>();
                empty.setVisibility(View.GONE);
                int status = response.getInt("status");
                if (status == 200) {
                    JSONObject dataObj = response.optJSONObject("misri");
                    String day = dataObj.optString("d");
                    String arrabicDay = AppUtility.getArabicNumbers(day);
                    String month = dataObj.optString("m");
                    String year = dataObj.optString("y");
                    String arabicYear = AppUtility.getArabicNumbers(year);
                    String miqaat = dataObj.optString("miqaat");
                    JSONObject namazObj = response.optJSONObject("namaz");
                     fajr = namazObj.optString("fajr");
                     sunrise = namazObj.optString("sunrise");
                     zawal = namazObj.optString("zawal");
                     zuhrEnd = namazObj.optString("zuhrEnd");
                     asrEnd = namazObj.optString("asrEnd");
                     sunset = namazObj.optString("sunset");
                     nisfulLail = namazObj.optString("nisfulLail");
                    JSONArray dataArray = response.optJSONArray("data");
                    if(dataArray != null){
                        for (int i = 0; i < dataArray.length(); i++) {
                            Notification notification = new Notification();
                            JSONObject notificationObj = dataArray.getJSONObject(i);
                            String message = notificationObj.optString("message");
                            Boolean isNew = notificationObj.optBoolean("current");
                            long DateTimeStamp = notificationObj.optInt("timestamp");
                            String bg = notificationObj.optString("bg");
                            String fontColor = notificationObj.optString("font");
                            String date = AppUtility.getDateTime(DateTimeStamp);
                            notification.setTitle(message);
                            notification.setDate(date);
                            notification.setNew(isNew);
                            notification.setBgColor(bg);
                            notification.setFontColor(fontColor);

                            if (notification != null) {
                                notificationArrayList.add(notification);
                            }
                        }
                    }else{
                        empty.setVisibility(View.VISIBLE);
                        empty.setText("No Message Today");
                    }
                    mAdapter = new NotificationAdapter(NotificationActivity.this, R.layout.notification_list_item,
                            notificationArrayList);
                    mListView.setAdapter(mAdapter);
                    englishDayTv.setText(""+today);
                    englishMothTv.setText(eMonth+", "+ eYear);
                    dayTV.setText(arrabicDay);
                    monthTV.setText(month + " "+ arabicYear);
                    miqaatTv.setText(miqaat);
                    fajrTV.setText("Fajr"+"\n"+fajr);
                    zuhurTv.setText("Zuhr Start"+"\n"+zawal);
                    asrTv.setText("Zuhr End"+"\n"+zuhrEnd);
                    magribTv.setText("Sunset"+"\n"+sunset);
                    mAdapter.notifyDataSetChanged();
                    // Call onLoadMoreComplete when the LoadMore task, has finished
                }else if(status == 401) {
                    Intent intent = new Intent(NotificationActivity.this,LoginActivity.class);
                    startActivity(intent);
                    SharedPreferencesManager.removePreference(Constants.TOKEN);
                    SharedPreferencesManager.removePreference(Constants.SAVEPASSWORD);
                    SharedPreferencesManager.removePreference(Constants.ALLOWEDSTATUS);
                    Toast.makeText(NotificationActivity.this, "You have logged in from another device", Toast.LENGTH_LONG).show();
                    finish();
                }else {
                    String message = response.optString("message");
                    Toast.makeText(NotificationActivity.this, message, Toast.LENGTH_LONG).show();
                }
                hideDialog();
            } catch (JSONException je) {
                je.printStackTrace();
            }
        }
    }

    @Override
    public void onError(VolleyError error, String message, String tag) {
        // This is wrong implementation we implemented this to handle a error with deleted meeting it will be corrected at backend
        // In this we first hit meeting api with given meeting id and when it throw error we show a toast and when it give success we take it to meeting detail page
        showAToast(message);
    }

    @Override
    protected void onStart() {

        if (NetworkUtil.isConnected()) {
            loadJsonData(Request.Method.GET, Constants.NOTIFICATIONS_LIST, null, Constants.NOTIFICATIONS_LIST, false);
        } else {;
//                empty.setVisibility(View.VISIBLE);
//            empty.setText(R.string.no_internet_connection);

            Toast.makeText(NotificationActivity.this, R.string.network_not_connected_error_msg, Toast.LENGTH_LONG).show();
        }
        SharedPreferencesManager.removePreference("notificationMessage");
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

//        this.doubleBackToExitPressedOnce = true;
//        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//             Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public boolean isNBadgeVisible(){
        return false;
    }

    public void onRefresh() {
        showDialog();
        notificationArrayList = new ArrayList<>();
        mAdapter = new NotificationAdapter(NotificationActivity.this, R.layout.notification_list_item,
                notificationArrayList);
        mListView.setAdapter(mAdapter);
        if (NetworkUtil.isConnected()) {
            loadJsonData(Request.Method.GET, Constants.NOTIFICATIONS_LIST, null, Constants.NOTIFICATIONS_LIST, false);
        } else {
//            empty.setVisibility(View.VISIBLE);
//            empty.setText(R.string.no_internet_connection);
            Toast.makeText(NotificationActivity.this, R.string.network_not_connected_error_msg, Toast.LENGTH_LONG).show();
        }
    }
    public void showAToast (String st){ //"Toast toast" is declared in the class
        try{ toast.getView().isShown();     // true if visible
            toast.setText(st);
        } catch (Exception e) {         // invisible if exception
            toast = Toast.makeText(NotificationActivity.this, st, Toast.LENGTH_SHORT);
        }
        toast.show();  //finally display it
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, 500);
    }
    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            // Extract data included in the Intent
        onRefresh();

        }
    };
    @Override
    protected void onPause() {
        // Unregister since the activity is paused.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver);

        super.onPause();
    }

    @Override
    protected void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, new IntentFilter(Constants.BROADCAST_ACTION_NOTIFICATION_RECIEVED));

        super.onResume();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_filter, menu);
        MenuItem readAll = menu.findItem(R.id.readAll);
        readAll.setVisible(true);
        readAll.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                showSortDialog();
                return false;
            }
        });
        return true;
    }
    public void showSortDialog(){
        final Dialog dialog = new Dialog(NotificationActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setCancelable(true);

        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.forgot_password_dialog);
        TextView message = (TextView) dialog.findViewById(R.id.messageView);
        message.setText("To send miqaat/event message from jamaat portal click on the call button");
        Button call = (Button) dialog.findViewById(R.id.sendBtn);
        Button SMS = (Button) dialog.findViewById(R.id.sendSMSBtn);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "+918982987153"));
                startActivity(intent);
            }
        });
        SMS.setVisibility(View.VISIBLE);
        SMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent smsIntent = new Intent(
                        android.content.Intent.ACTION_VIEW);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.putExtra("address", "+918982987153");
                NotificationActivity.this.startActivity(Intent.createChooser(smsIntent,"SMS:"));
//                startActivity(smsIntent);
            }
        });
        dialog.show();
    }
    public void showNamazDialog(){
        final Dialog dialog = new Dialog(NotificationActivity.this);
//        dialog.setCancelable(true);
        dialog.setTitle("Namaz Timings");
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.namaz_time_dialog);
        TextView fajrT = (TextView) dialog.findViewById(R.id.fajrNamaz);
        TextView sunriseNamaz = (TextView) dialog.findViewById(R.id.sunriseNamaz);
        TextView zhurStart = (TextView) dialog.findViewById(R.id.zuhrStartNamaz);
        TextView zhurEnd = (TextView) dialog.findViewById(R.id.zhurEndNamaz);
        TextView asarEnd = (TextView) dialog.findViewById(R.id.asrEndNamaz);
        TextView sunsetTv = (TextView) dialog.findViewById(R.id.sunsetNamaz);
        TextView nisfUlLail = (TextView) dialog.findViewById(R.id.nisfNamaz);
        fajrT.setText("Fajr  :"+fajr);
        sunriseNamaz.setText("Fajr End  :"+sunrise);
        zhurStart.setText("zawal  :"+zawal);
        zhurEnd.setText("zuhr End  :"+zuhrEnd);
        asarEnd.setText("Asr End  :"+asrEnd);
        sunsetTv.setText("Magrib  :"+sunset);
        nisfUlLail.setText("Nisf-ul-lail  :"+nisfulLail);
        dialog.show();
    }
}
