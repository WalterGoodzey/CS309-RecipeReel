package com.example.recipeapp;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

public class MessageListAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<BaseMessage> mMessageList; //TODO Create type similar to base message in tutorial: https://sendbird.com/developer/tutorials/android-chat-tutorial-building-a-messaging-ui
    public MessageListAdapter(Context context, List<BaseMessage> messageList) {
        mContext = context;
        mMessageList = messageList;
    }
}
