package com.example.recipeapp;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import java.util.List;

/**
 * Adapter class for a list of messages
 *
 * @author Ryan McFadden
 */
public class MessageAdapter extends ArrayAdapter<MessageItemObject> {
    /**
     * Constructor for MessageAdapter
     * @param context application's current context
     * @param items list of MessageItemObjects
     */
    public MessageAdapter(Context context, List<MessageItemObject> items) {
        super(context, 0, items);
    }

    /**
     * Method to update and return the updated View
     *
     * @param position The position of the item within the adapter's data set of the item whose view
     *        we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *        is non-null and of an appropriate type before using. If it is not possible to convert
     *        this view to display the correct data, this method can create a new view.
     *        Heterogeneous lists can specify their number of view types, so that this View is
     *        always of the right type (see {@link #getViewTypeCount()} and
     *        {@link #getItemViewType(int)}).
     * @param parent The parent that this view will eventually be attached to
     * @return convertView
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        MessageItemObject item = getItem(position);
        if(item.getSendingMessage()){
            //message is sent by the user
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.message_item, parent, false);
            }

            // Lookup view for data population
            TextView itemDate = convertView.findViewById(R.id.text_chat_date);
            TextView itemMessageSent = convertView.findViewById(R.id.text_chat_message_sent);
            TextView itemTimestampSent = convertView.findViewById(R.id.text_chat_timestamp_sent);
            TextView itemUserReceived = convertView.findViewById(R.id.text_chat_user_received);
            TextView itemMessageReceived = convertView.findViewById(R.id.text_chat_message_received);
            TextView itemTimestampReceived = convertView.findViewById(R.id.text_chat_timestamp_received);
            CardView cardSent = convertView.findViewById(R.id.card_chat_message_sent);
            CardView cardReceived = convertView.findViewById(R.id.card_chat_message_received);

            // Populate the data into the template view using the data object
            //populate sent message & timestamp
            itemDate.setText(item.getDate());
            itemMessageSent.setText(item.getMessage());
            itemTimestampSent.setText(item.getTimestamp());

            //view sent message block
            itemMessageSent.setVisibility(View.VISIBLE);
            itemTimestampSent.setVisibility(View.VISIBLE);
            cardSent.setVisibility(View.VISIBLE);

            //hide received message block
            itemUserReceived.setVisibility(View.INVISIBLE);
            itemMessageReceived.setVisibility(View.INVISIBLE);
            itemTimestampReceived.setVisibility(View.INVISIBLE);
            cardReceived.setVisibility(View.INVISIBLE);


            // Return the completed view to render on screen
            return convertView;

        } else {
            //message is being received by the user
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.message_item, parent, false);
            }

            // Lookup view for data population
            TextView itemDate = convertView.findViewById(R.id.text_chat_date);
            TextView itemMessageSent = convertView.findViewById(R.id.text_chat_message_sent);
            TextView itemTimestampSent = convertView.findViewById(R.id.text_chat_timestamp_sent);
            TextView itemUserReceived = convertView.findViewById(R.id.text_chat_user_received);
            TextView itemMessageReceived = convertView.findViewById(R.id.text_chat_message_received);
            TextView itemTimestampReceived = convertView.findViewById(R.id.text_chat_timestamp_received);
            CardView cardSent = convertView.findViewById(R.id.card_chat_message_sent);
            CardView cardReceived = convertView.findViewById(R.id.card_chat_message_received);


            // Populate the data into the template view using the data object
            String otherUser = item.getSenderUsername();
            itemUserReceived.setText(otherUser);

            //populate received user, message, & timestamp
            itemMessageReceived.setText(item.getMessage());
            itemTimestampReceived.setText(item.getTimestamp());
            itemDate.setText(item.getDate());

            //view received message block
            itemUserReceived.setVisibility(View.VISIBLE);
            itemMessageReceived.setVisibility(View.VISIBLE);
            itemTimestampReceived.setVisibility(View.VISIBLE);
            cardReceived.setVisibility(View.VISIBLE);

            //hide sent message block
            itemMessageSent.setVisibility(View.INVISIBLE);
            itemTimestampSent.setVisibility(View.INVISIBLE);
            cardSent.setVisibility(View.INVISIBLE);

            // Return the completed view to render on screen
            return convertView;
        }
    }

//    @Override
//    public ListItemObject getItem(int position){
//        return items.get(position);
//    }
}


