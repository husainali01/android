package com.dotcom.jamaat.activity.Common;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.dotcom.jamaat.R;
import com.dotcom.jamaat.model.Notification;
import com.dotcom.jamaat.util.AppUtility;
import com.dotcom.jamaat.util.Constants;
import com.dotcom.jamaat.util.network.NetworkUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ForgotPasswordActivity extends BaseActivity {

    private NetworkUtil networkUtil;
    private Context mContext;
    private EditText mEmailAddress;
    private Button proceedButton;
    private ProgressDialog mDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.forgot_password_dialog);
        mEmailAddress = (EditText) findViewById(R.id.email_address_textview);
        proceedButton = (Button) findViewById(R.id.sendBtn);
        super.onCreate(savedInstanceState);
        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "+918982987153"));
                startActivity(intent);

            }
        });
        if (NetworkUtil.isConnected()) {
            loadJsonData(Request.Method.GET, Constants.GET_TIME, null, Constants.GET_TIME, false);
        } else {
            Toast.makeText(ForgotPasswordActivity.this, R.string.network_not_connected_error_msg, Toast.LENGTH_LONG).show();
        }
        Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("New Registration");
    }

    @Override
    protected String getTagName() {
        return null;
    }

    @Override
    public void showProgress(boolean show, String tag) {
        if (show) {
            showDialog();
        } else
            hideDialog();
    }

    @Override
    public void onSuccess(JSONObject response, String tag) {
        Log.v("Notification result::",response.toString());
        if(tag.equalsIgnoreCase(Constants.GET_TIME)) {
            try {
                int status = response.getInt("status");
                if (status == 200) {
                    String check = response.optString("result");
                    if(check.equalsIgnoreCase("true")){
                        proceedButton.setClickable(true);
                    }else{
                        proceedButton.setClickable(false);
                    }
                }
            } catch (JSONException je) {
                je.printStackTrace();
            }
        }
    }

    @Override
    public void onError(VolleyError error, String message, String tag) {
    }
    public void showDialog() {
        mDialog = new ProgressDialog(this);
        mDialog.setMessage("Please wait...");
        mDialog.setCancelable(true);
        mDialog.show();
    }

    public void hideDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }
    public boolean isNBadgeVisible(){
        return false;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                super.onBackPressed();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
