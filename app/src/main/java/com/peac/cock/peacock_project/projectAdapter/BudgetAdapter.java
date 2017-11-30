package com.peac.cock.peacock_project.projectAdapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.peac.cock.peacock_project.R;
import com.peac.cock.peacock_project.projectDto.MessageItem;

import java.util.ArrayList;

/**
 * Created by dahye on 2017-11-30.
 */

public class BudgetAdapter extends BaseAdapter{
    private LayoutInflater inflater;
    private ArrayList<MessageItem> messageItems;
    private int layout;


    public BudgetAdapter(Context context, int layout, ArrayList<MessageItem> messageItems) {
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


        TextView budgetMonthExtraAmount = convertView.findViewById(R.id.budget_layout_month_extra_amount);
        TextView budgetMonthInfoAmount = convertView.findViewById(R.id.budget_layout_month_info_amount1);
        ProgressBar budgetProgressbar = convertView.findViewById(R.id.budget_layout_progressbar);
        TextView budgetPercentage = convertView.findViewById(R.id.budget_layout_percentage);
        TextView budgetResultText = convertView.findViewById(R.id.budget_layout_budget_result_text);

        MessageItem messageItem = messageItems.get(position);

        ImageView imageView = convertView.findViewById(R.id.list_msg_img);
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
