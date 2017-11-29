package com.peac.cock.peacock_project.projectAdapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.peac.cock.peacock_project.R;
import com.peac.cock.peacock_project.projectDto.MessageItem;

import java.util.ArrayList;

/**
 * Created by wee on 2017. 11. 25..
 */

public class ListTab1Adapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<MessageItem> messageItems;
    private int layout;


    public ListTab1Adapter(Context context, int layout, ArrayList<MessageItem> messageItems) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.messageItems = messageItems;
        this.layout = layout;
    }

    public void setMessageItems(ArrayList<MessageItem> messageItems) {
        this.messageItems = messageItems;
    }

    @Override
    public int getCount() {
        if(messageItems != null) {
            return messageItems.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return messageItems.get(position).getMessageContent();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(layout, parent, false);
        }

        MessageItem messageItem = messageItems.get(position);

        ImageView imageView = convertView.findViewById(R.id.list_msg_img);
        Log.d("categoryImage", String.valueOf(R.drawable.message_category_unclassified));
        imageView.setImageResource(messageItem.getCategoryId());

        TextView msgContent = convertView.findViewById(R.id.list_msg_content);
        msgContent.setText(messageItem.getMessageContent());

        TextView msgDate= convertView.findViewById(R.id.list_msg_date);
        msgDate.setText(messageItem.getMessageDate());

        TextView msgBalance = convertView.findViewById(R.id.list_msg_balance);
        msgBalance.setText(String.valueOf(messageItem.getMessageBalance()));

        return convertView;
    }
}
