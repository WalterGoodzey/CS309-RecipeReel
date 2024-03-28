package com.example.recipeapp.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.recipeapp.MessageAdapter;
import com.example.recipeapp.MessageItemObject;
import com.example.recipeapp.R;
import com.example.recipeapp.WebSocketListener;
import com.example.recipeapp.WebSocketManager;

import org.java_websocket.handshake.ServerHandshake;

import java.util.ArrayList;

/**
 * Activity used to display a chat interface between local user and
 * another user account. Activity handles the display of messages and the websocket
 * methods to enable the conversation between the two users
 *
 * @author Ryan McFadden
 */
public class ChatActivity extends AppCompatActivity implements WebSocketListener {
    /** MessageAdapter for list of messages */
    private MessageAdapter adapter;
    /** ListView for storing sent and received messages */
    private ListView messageListView;
    /** EditText for user to type messages to be sent */
    private EditText messageEditText;
    /** Button to send message typed in messageEditText to websocket */
    private Button sendButton;
    /** Local user's userId */
    private int userId;
    /** Base URL of websocket connection */
    private static final String BASE_URL = "ws://10.0.2.2:8080/chat/";
    /** Message to fill next MessageItemObject (sent or received) */
    private String message;

    /**
     * onCreate method for ChatActivity
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        sendButton = findViewById(R.id.button_chat_send);
        messageEditText = findViewById(R.id.edit_chat_message);

        //get username from previous activity
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            userId = extras.getInt("id");
        }

        //message list setup and operation
        messageListView = findViewById(R.id.messageListView);

        // Initialize the adapter with an empty list (data will be added later)
        adapter = new MessageAdapter(this, new ArrayList<>());
        messageListView.setAdapter(adapter);

        //for websocket connection
        String serverUrl = BASE_URL + "ryanm";
        // Establish WebSocket connection and set listener
        WebSocketManager.getInstance().connectWebSocket(serverUrl);
        WebSocketManager.getInstance().setWebSocketListener(ChatActivity.this);

        /* send button listener */
        sendButton.setOnClickListener(v -> {
            try {
                message = messageEditText.getText().toString();
                messageEditText.getText().clear();
                // send message
                WebSocketManager.getInstance().sendMessage(message);

                //add message to messageListView
                MessageItemObject item = new MessageItemObject(message, null, null, userId, true);
                adapter.add(item);
            } catch (Exception e) {
                Log.d("ExceptionSendMessage:", e.getMessage().toString());
            }
        });

    }

    /**
     * Handler for when message is received from the websocket
     * @param message The received WebSocket message.
     */
    @Override
    public void onWebSocketMessage(String message) {
        /**
         * In Android, all UI-related operations must be performed on the main UI thread
         * to ensure smooth and responsive user interfaces. The 'runOnUiThread' method
         * is used to post a runnable to the UI thread's message queue, allowing UI updates
         * to occur safely from a background or non-UI thread.
         */
        runOnUiThread(() -> {
            //add message to messageListView
            MessageItemObject item = new MessageItemObject(message, null, null, -1, false);
            adapter.add(item);
        });
    }

    /**
     * Handler for closing websocket connection
     * @param code   The status code indicating the reason for closure.
     * @param reason A human-readable explanation for the closure.
     * @param remote Indicates whether the closure was initiated by the remote endpoint.
     */
    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
        String closedBy = remote ? "server" : "local";
        runOnUiThread(() -> {
//            String s = msgTv.getText().toString();
//            msgTv.setText(s + "---\nconnection closed by " + closedBy + "\nreason: " + reason);
        });
    }

    /**
     * Handler for when websocket connection is opened
     * @param handshakedata Information about the server handshake.
     */
    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {}

    /**
     * Handle for when there is a websocket error
     * @param ex The exception that describes the error.
     */
    @Override
    public void onWebSocketError(Exception ex) {}

}

