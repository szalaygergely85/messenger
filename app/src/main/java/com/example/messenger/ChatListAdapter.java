package com.example.messenger;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class ChatListAdapter extends ArrayAdapter {


    private List chatInfoList;
    private Context context;
    private FirebaseAuth fh;

    public ChatListAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
        this.chatInfoList = objects;
        this.context = context;
    }

    private class ViewHolder {
        TextView messageText;
        //   TextView senderName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ChatListAdapter.ViewHolder holder = null;

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.listview_chat, null);

            holder = new ChatListAdapter.ViewHolder();
            holder.messageText = (TextView) convertView.findViewById(R.id.chatText);
            //holder.senderName = (TextView) convertView.findViewById(R.id.senderName);
            convertView.setTag(holder);
        } else {
            holder = (ChatListAdapter.ViewHolder) convertView.getTag();
        }

        Chat contactsInfo = (Chat) chatInfoList.get(position);
        fh = FirebaseAuth.getInstance();

        if (contactsInfo.getSender().equalsIgnoreCase(fh.getCurrentUser().getUid())) {

            holder.messageText.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);

        } else {

            holder.messageText.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        }

        holder.messageText.setText(contactsInfo.getText());

        // holder.senderName.setText(contactsInfo.getSender());


        return convertView;
    }
}