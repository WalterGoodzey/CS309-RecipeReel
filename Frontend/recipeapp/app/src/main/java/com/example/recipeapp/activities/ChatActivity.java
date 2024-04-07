package com.example.recipeapp.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.recipeapp.R;
import com.example.recipeapp.VolleySingleton;
import com.example.recipeapp.WebSocketListener;
import com.example.recipeapp.WebSocketManager;
import com.example.recipeapp.adapters.MessageAdapter;
import com.example.recipeapp.objects.MessageItemObject;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
    /** Local user's username */
    private String localUsername;
    /** Other user's username */
    private String otherUsername;
    /** Base URL of websocket connection */
    private static final String BASE_CHAT_URL = "ws://coms-309-018.class.las.iastate.edu:8080/chat/";
    /** Specific URL of this chat between local user and this specific other user
     *  Will be in the format: ws://coms-309-018.class.las.iastate.edu:8080/chat/{localUsername}/{otherUsername}
     */
    private String SPECIFIC_CHAT_URL = "";
    /** Base URL to get chat history */
    private static final String BASE_CHAT_HISTORY_URL = "http://coms-309-018.class.las.iastate.edu:8080/chat/";
    /** Specific URL to get the history of this chat between local user and this specific other user
     *  Will be in the format:
     */
    private String SPECIFIC_CHAT_HISTORY_URL = "";
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

        //get local username from shared preferences
        SharedPreferences saved_values = getSharedPreferences(getString(R.string.PREF_KEY), Context.MODE_PRIVATE);
        localUsername = saved_values.getString(getString(R.string.USERNAME_KEY), null);

        //get other username from intent
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            otherUsername = extras.getString("otherChatUser");
        }

        //message list setup and operation
        messageListView = findViewById(R.id.messageListView);

        // Initialize the adapter with an empty list (data will be added later)
        adapter = new MessageAdapter(this, new ArrayList<>());
        messageListView.setAdapter(adapter);

        //get the chat history
        SPECIFIC_CHAT_HISTORY_URL = BASE_CHAT_HISTORY_URL + localUsername + "/" + otherUsername;
        getChatHistory();

        //for websocket connection
        SPECIFIC_CHAT_URL = BASE_CHAT_URL + localUsername + "/" + otherUsername;
        // Establish WebSocket connection and set listener
        WebSocketManager.getInstance().connectWebSocket(SPECIFIC_CHAT_URL);
        WebSocketManager.getInstance().setWebSocketListener(ChatActivity.this);

        /* send button listener */
        sendButton.setOnClickListener(v -> {
            try {
                message = messageEditText.getText().toString();
                messageEditText.getText().clear();
                // send message
                WebSocketManager.getInstance().sendMessage(message);

                //add message to messageListView
                MessageItemObject item = new MessageItemObject(message,  null, localUsername, true);
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
            MessageItemObject item = new MessageItemObject(message, null, null, false);
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


    /**
     * Volley GET to get list of this chatroom's past messages
     */
    private void getChatHistory() {
        JsonArrayRequest chatHistoryReq = new JsonArrayRequest(
                Request.Method.GET,
                SPECIFIC_CHAT_HISTORY_URL,
                null, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Volley Response", response.toString());

                        // Parse the JSON array and add data to the adapter
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject msgObject = response.getJSONObject(i);
                                String message = msgObject.getString("content");
                                Date timestamp = (Date) msgObject.get("sent");
                                String senderUsername = msgObject.getString("sender");
                                Boolean sendingMessage = false;
                                if(senderUsername.equals(localUsername)){
                                    sendingMessage = true;
                                }
                                //Create a MessageItemObject and add it to the adapter
                                MessageItemObject item = new MessageItemObject(message, timestamp, senderUsername, sendingMessage);
                                adapter.add(item);

//                                String username = response.getString(i);
//                                // Create a ProfileItemObject and add it to the adapter
//                                MessageItemObject item = new MessageItemObject(username);
//                                adapter.add(item);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                throw new RuntimeException(e);
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", error.toString());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
//                headers.put("Authorization", "Bearer YOUR_ACCESS_TOKEN");
//                headers.put("Content-Type", "application/json");
                return headers;
            }
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
//                params.put("param1", "value1");
//                params.put("param2", "value2");
                return params;
            }
        };
        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(chatHistoryReq);
    }
}

