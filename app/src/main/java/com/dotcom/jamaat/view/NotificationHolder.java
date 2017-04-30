package com.dotcom.jamaat.view;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dotcom.jamaat.R;
import com.dotcom.jamaat.model.Notification;

import static android.R.attr.data;
import static android.R.attr.theme;
import static android.R.attr.thickness;

/**
 * Created by Husain on 23-03-2017.
 */

public class NotificationHolder extends RecyclerView.ViewHolder {

    private final TextView title;
    private final TextView mDate;
    private ImageView new_notification;
    private LinearLayout mainLayout;

    private Notification notification;
    private Context context;

    public NotificationHolder(Context context, View itemView) {

        super(itemView);

        // 1. Set the context
        this.context = context;

        // 2. Set up the UI widgets of the holder
        this.title = (TextView) itemView.findViewById(R.id.title_notification);
        this.mDate = (TextView) itemView.findViewById(R.id.date_notification);
        this.new_notification = (ImageView) itemView.findViewById(R.id.new_notification);
        this.mainLayout = (LinearLayout) itemView.findViewById(R.id.mainListItem);
        // 3. Set the "onClick" listener of the holder
    }

    public void bindBakery(Notification notification) {

        // 4. Bind the data to the ViewHolder
        this.notification = notification;
        this.title.setText(notification.getTitle());
        this.title.setTextColor(Color.parseColor(notification.getFontColor()));
        this.mDate.setTextColor(Color.parseColor(notification.getFontColor()));
        this.mDate.setText(notification.getDate());
        this.mainLayout.setBackgroundColor(Color.parseColor(notification.getBgColor()));
        Boolean isNew = notification.getNew();
        if(isNew){
            this.new_notification.setVisibility(View.VISIBLE);
        }else{
            this.new_notification.setVisibility(View.GONE);
        }
    }
}
