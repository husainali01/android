package com.dotcom.jamaat.activity.Common;import android.content.BroadcastReceiver;import android.content.Context;import android.content.Intent;import android.content.IntentFilter;import android.content.pm.PackageManager;import android.os.Bundle;import android.os.Handler;import android.support.v4.app.FragmentManager;import android.support.v4.content.LocalBroadcastManager;import android.support.v7.app.AppCompatActivity;import android.text.TextUtils;import android.util.Log;import android.view.Menu;import android.view.MenuInflater;import android.view.MenuItem;import android.view.View;import android.widget.TextView;import android.widget.Toast;import com.android.volley.AuthFailureError;import com.android.volley.Cache;import com.android.volley.Request;import com.android.volley.Response;import com.android.volley.VolleyError;//import com.google.firebase.crash.FirebaseCrash;import com.dotcom.jamaat.util.Constants;import com.dotcom.jamaat.util.SharedPreferencesManager;import com.dotcom.jamaat.util.app.IANappApplication;import com.dotcom.jamaat.util.network.volley.VolleyErrorListener;import com.dotcom.jamaat.R;import com.dotcom.jamaat.util.AppUtility;import com.dotcom.jamaat.util.network.volley.JsonRequest;import org.json.JSONException;import org.json.JSONObject;import java.io.UnsupportedEncodingException;import java.util.EnumSet;import java.util.HashMap;import java.util.Map;import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;/** * Containing common features to be used in application Activitiy */public abstract class BaseActivity extends AppCompatActivity {    protected FragmentManager fm;    private Toast toast;    public static final String IAN_INTENT_ACTION = "IAN_INTENT_ACTION";    TextView tvBadge;    int unseen;    public enum ACTION {        CAMERA(11), READ_CONTACTS(12), WRITE_EXTERNAL_STORAGE(13);        private int requestCode;        ACTION(int code) {            this.requestCode = code;        }        public int getRequestCode() {            return this.requestCode;        }        private static final Map<Integer, ACTION> lookup = new HashMap<>();        static {            for (ACTION act : EnumSet.allOf(ACTION.class)) {                lookup.put(act.getRequestCode(), act);            }        }        public static ACTION fromOrdinal(int ordinal) {            return lookup.get(ordinal);        }    }    @Override    protected void onPause() {        // Unregister since the activity is paused.        LocalBroadcastManager.getInstance(this).unregisterReceiver(                mMessageReceiver);        super.onPause();    }    @Override    protected void onResume() {        // Register to receive messages.        // We are registering an observer (mMessageReceiver) to receive Intents        // with actions named "custom-event-name".        LocalBroadcastManager.getInstance(this).registerReceiver(                mMessageReceiver, new IntentFilter(Constants.BROADCAST_ACTION_NOTIFICATION_RECIEVED));        super.onResume();    }    // Our handler for received Intents. This will be called whenever an Intent    // with an action named "BROADCAST_ACTION_NOTIFICATION_RECIEVED" is broadcasted.    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {        @Override        public void onReceive(Context context, Intent intent) {            String message = intent.getStringExtra("message");            updateNotificationBadge();            // int unseen = SharedPreferencesManager.getIntPreference(Constants.UNSEEN_NOTIFICATION_COUNT, 0);            Log.d("receiver", "Got message: " + message);            // tvBadge.setText(""+unseen);        }    };    @Override    protected void onCreate(Bundle savedInstanceState) {        super.onCreate(savedInstanceState);        fm = getSupportFragmentManager();    }    @Override    public boolean onCreateOptionsMenu(Menu menu) {        MenuInflater inflater = getMenuInflater();        inflater.inflate(R.menu.menu_main, menu);        final Menu m = menu;        final MenuItem item = menu.findItem(R.id.notification);        View view = item.getActionView();        tvBadge = (TextView) view.findViewById(R.id.nBadgeCount);        if((unseen !=0)||(unseen > 0)){            tvBadge.setVisibility(View.VISIBLE);            if(unseen>99){                tvBadge.setText("99+");            }else{                tvBadge.setText(""+unseen);            }        }else{            tvBadge.setVisibility(View.GONE);        }        item.getActionView().setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View v) {                Intent intent = new Intent(BaseActivity.this, NotificationActivity.class);                startActivity(intent);            }        });        item.setVisible(isNBadgeVisible());        MenuItem searchItem = menu.findItem(R.id.search);        searchItem.setVisible(false);        return true;    }    protected void updateNotificationBadge()    {        unseen = SharedPreferencesManager.getIntPreference(Constants.UNSEEN_NOTIFICATION_COUNT, 0);        //Log.d("receiver", "Got message: " + message);        if(tvBadge!=null) {            if((unseen !=0)||(unseen > 0)){                tvBadge.setVisibility(View.VISIBLE);                if(unseen>99){                    tvBadge.setText("99+");                }else{                    tvBadge.setText(""+unseen);                }            }else{                tvBadge.setVisibility(View.GONE);            }        }    }    public boolean isNBadgeVisible(){        return true;    }    @Override    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {        ACTION action = ACTION.fromOrdinal(requestCode);        if (grantResults.length > 0                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {            // permission was granted, yay! Do the            // contacts-related task you need to do.            onPermissionGranted(action,requestCode);        } else {            onPermissionRevoked(action,requestCode);            // permission denied, boo! Disable the            // functionality that depends on this permission.        }    }    protected abstract String getTagName();    protected void onPermissionGranted(ACTION action,int requestCode) {    }    protected void onPermissionRevoked(ACTION action,int requestCode) {    }    public BaseActivity getBaseActivity() {        return this;    }    public abstract void showProgress(boolean show, String tag);    public abstract void onSuccess(JSONObject response, String tag);    public abstract void onError(VolleyError error, String message, String tag);    protected void loadJsonData(String url, String formattedJson,                                final String reqTag) {        loadJsonData(Request.Method.POST, url, formattedJson, reqTag,false);    }    protected void loadJsonData(String url, String formattedJson,                                final String reqTag,boolean shouldCache) {        loadJsonData(Request.Method.POST, url, formattedJson, reqTag,shouldCache);    }    /**     * Method to make json object request where json response starts with     */    protected void loadJsonData(int type, String url, String formattedJson,                                final String reqTag,boolean shouldCache) {        String newBaseUrl = SharedPreferencesManager.getStringPreference(Constants.BASE_URL,null);        if(newBaseUrl == null) {            url = Constants.getBaseAPIUrl() + url;        }        else if(newBaseUrl != null && newBaseUrl.length()>0)        {            url = newBaseUrl + url;        }        url = url.replaceAll(" ", "%20");        Log.v("Request Url:::",url);        if(formattedJson != null){            Log.v("Request Param",formattedJson);        }        // Show a progress spinner, and kick off a background task to        // perform the user login attempt.        showProgress(true, reqTag);        Cache cache = IANappApplication.getInstance().getRequestQueue().getCache();        Cache.Entry entry = cache.get(url);        if (entry != null) {            try {//                String data = new String(entry.data, Constants.CHARSET);                String data = new String(entry.data, Constants.CONTENT_TYPE_JSON);                JSONObject response = new JSONObject(data);                onSuccess(response, reqTag);            } catch (JSONException je) {                je.printStackTrace();            } catch (UnsupportedEncodingException e) {                e.printStackTrace();            }            showProgress(false, reqTag);        } else {            JSONObject reqParams = null;            try {                reqParams = TextUtils.isEmpty(formattedJson) ? null                        : new JSONObject(formattedJson);            } catch (JSONException ex) {                ex.printStackTrace();            }            JsonRequest jsonObjReq = new JsonRequest(type, url, reqParams,                    new Response.Listener<JSONObject>() {                        @Override                        public void onResponse(JSONObject response) {                            onSuccess(response, reqTag);                            showProgress(false, reqTag);                        }                    }, new VolleyErrorListener(getApplicationContext()) {                @Override                public void handleVolleyError(VolleyError error,                                              String message) {                    if (error instanceof AuthFailureError) {                        SharedPreferencesManager.removePreference(Constants.SORT);                        SharedPreferencesManager.removePreference(Constants.TOKEN);                        SharedPreferencesManager.removePreference(Constants.USERTYPE);                        SharedPreferencesManager.removePreference(Constants.SAVEPASSWORD);                        SharedPreferencesManager.removePreference(Constants.ALLOWEDSTATUS);                        Intent mainActivityInt = new Intent(BaseActivity.this,LoginActivity.class);                        mainActivityInt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);                        startActivity(mainActivityInt);                        //finish();                    }                    onError(error, message, reqTag);                    showProgress(false, reqTag);                }            });            jsonObjReq.setShouldCache(shouldCache);            // Adding request to request queue            /*jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(5000,                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/            IANappApplication.getInstance().addToRequestQueue(jsonObjReq);        }    }    protected void loadJsonData(int type, String url, Map<String,String> formattedJson,                                final String reqTag) {        //url = Constants.getBaseAPIUrl() + url;        String newBaseUrl = SharedPreferencesManager.getStringPreference(Constants.BASE_URL,null);        if(newBaseUrl == null) {            url = Constants.getBaseAPIUrl() + AppUtility.getUrlForFilter(url,formattedJson);        }        else if(newBaseUrl != null && newBaseUrl.length()>0)        {            url = newBaseUrl + AppUtility.getUrl(url,formattedJson);        }        url = url.replaceAll(" ", "%20");        Log.v("get request",url);        // Show a progress spinner, and kick off a background task to        // perform the user login attempt.        showProgress(true, reqTag);        Cache cache = IANappApplication.getInstance().getRequestQueue().getCache();        Cache.Entry entry = cache.get(url);        if (entry != null) {            try {//                String data = new String(entry.data, Constants.CHARSET);                String data = new String(entry.data, Constants.CONTENT_TYPE_JSON);                JSONObject response = new JSONObject(data);                onSuccess(response, reqTag);            } catch (JSONException je) {                je.printStackTrace();            } catch (UnsupportedEncodingException e) {                e.printStackTrace();            }            showProgress(false, reqTag);        } else {            JSONObject reqParams = null;           /* try {                reqParams = TextUtils.isEmpty(formattedJson) ? null                        : new JSONObject(formattedJson);            } catch (JSONException ex) {                ex.printStackTrace();            }*/            JsonRequest jsonObjReq = new JsonRequest(type, url, reqParams,                    new Response.Listener<JSONObject>() {                        @Override                        public void onResponse(JSONObject response) {                            onSuccess(response, reqTag);                            showProgress(false, reqTag);                        }                    }, new VolleyErrorListener(getApplicationContext()) {                @Override                public void handleVolleyError(VolleyError error,                                              String message) {                    if (error instanceof AuthFailureError) {                        SharedPreferencesManager.removePreference(Constants.SORT);                        SharedPreferencesManager.removePreference(Constants.TOKEN);                        SharedPreferencesManager.removePreference(Constants.USERTYPE);                        SharedPreferencesManager.removePreference(Constants.SAVEPASSWORD);                        SharedPreferencesManager.removePreference(Constants.ALLOWEDSTATUS);                        Intent mainActivityInt = new Intent(BaseActivity.this,LoginActivity.class);                        mainActivityInt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);                        startActivity(mainActivityInt);                        //finish();                    }                    onError(error, message, reqTag);                    showProgress(false, reqTag);                }            });            jsonObjReq.setShouldCache(false);            // Adding request to request queue            /*jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(5000,                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/            IANappApplication.getInstance().addToRequestQueue(jsonObjReq);        }    }    public void showAToast (String st){ //"Toast toast" is declared in the class        try{ toast.getView().isShown();     // true if visible            toast.setText(st);        } catch (Exception e) {         // invisible if exception            toast = Toast.makeText(BaseActivity.this, st, Toast.LENGTH_SHORT);        }        toast.show();  //finally display it        Handler handler = new Handler();        handler.postDelayed(new Runnable() {            @Override            public void run() {                toast.cancel();            }        }, 500);    }    /*Use Send Broadcast Reciever*/    protected void sendLocalBroadcast(String data, int resultCode, int requestCode) {        Intent intent = new Intent(BaseActivity.IAN_INTENT_ACTION);        intent.setAction(BaseActivity.IAN_INTENT_ACTION);        intent.putExtra("dataURI", data);        intent.putExtra("requestCode", requestCode);        intent.putExtra("resultCode", resultCode);        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);    }    @Override    protected void attachBaseContext(Context newBase) {        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));    }    @Override    public void onBackPressed() {        super.onBackPressed();    }    @Override    protected void onStart() {        updateNotificationBadge();        super.onStart();    }}