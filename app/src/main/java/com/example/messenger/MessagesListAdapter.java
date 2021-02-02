package com.example.messenger;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class MessagesListAdapter extends ArrayAdapter {


    private List messagesInfoList;
    private Context context;

    public MessagesListAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
        this.messagesInfoList = objects;
        this.context = context;
    }

    private class ViewHolder {
        TextView messageText;
        TextView senderName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.listview_messages, null);

            holder = new ViewHolder();
            holder.messageText = (TextView) convertView.findViewById(R.id.messageText);
            holder.senderName = (TextView) convertView.findViewById(R.id.senderName);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        MessageHead contactsInfo = (MessageHead) messagesInfoList.get(position);
        holder.messageText.setText(contactsInfo.getText());
        holder.senderName.setText(contactsInfo.getSender());

        return convertView;
    }
}
