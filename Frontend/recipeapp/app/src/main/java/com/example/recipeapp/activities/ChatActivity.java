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

public class ChatActivity extends AppCompatActivity implements WebSocketListener {

    private MessageAdapter adapter;
    private ListView messageListView;
    private EditText messageEditText;
    private Button sendButton;
    private int userId;
    private static final String BASE_URL = "ws://10.0.2.2:8080/chat/";

    private String message, date, timestamp, senderId;
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
                // send message
                WebSocketManager.getInstance().sendMessage(message);

                //add message to messageListView
                MessageItemObject item = new MessageItemObject(message, null, null, null, -1);
                adapter.add(item);
            } catch (Exception e) {
                Log.d("ExceptionSendMessage:", e.getMessage().toString());
            }
        });

    }

    @Override
    public void onWebSocketMessage(String message) {
        /**
         * In Android, all UI-related operations must be performed on the main UI thread
         * to ensure smooth and responsive user interfaces. The 'runOnUiThread' method
         * is used to post a runnable to the UI thread's message queue, allowing UI updates
         * to occur safely from a background or non-UI thread.
         */
        runOnUiThread(() -> {
//            String s = msgTv.getText().toString();
//            msgTv.setText(s + "\n"+message);
        });
    }

    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
        String closedBy = remote ? "server" : "local";
        runOnUiThread(() -> {
//            String s = msgTv.getText().toString();
//            msgTv.setText(s + "---\nconnection closed by " + closedBy + "\nreason: " + reason);
        });
    }

    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {}

    @Override
    public void onWebSocketError(Exception ex) {}

}

//public class ChatActivity extends AppCompatActivity implements WebSocketListener{
//
//    private String BASE_URL = "ws://10.0.2.2:8080/chat/";
//    private Button sendButton;
//    private EditText msgEtx;
//    private int userId;
//
//    private RecyclerView mMessageRecycler;
//    private MessageListAdapter mMessageAdapter;
//    private List<MessageItemObject> mMessageList;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_chat);
//
//        mMessageRecycler = (RecyclerView) findViewById(R.id.recycler_gchat);
//        mMessageAdapter = new MessageListAdapter(this, mMessageList);
//        mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));
//        mMessageRecycler.setAdapter(mMessageAdapter);
//
//        sendButton = (Button) findViewById(R.id.button_gchat_send);
//        msgEtx = (EditText) findViewById(R.id.edit_gchat_message);
//
//        Intent createIntent = getIntent();
//        if(createIntent.hasExtra("id") && createIntent.getIntExtra("id", -1) > 0) {
//            /* set userId and add to url for GET request, make GET request (fills in user's profile page) */
//            userId = createIntent.getIntExtra("id", -1);
//
////            String serverUrl = BASE_URL + userId; //check with Dave to see if this is the right URL
//
//            String serverUrl = BASE_URL + "ryanm"; //TEMPORARY - for testing
//
//            // Establish WebSocket connection and set listener
//            WebSocketManager.getInstance().connectWebSocket(serverUrl);
//            WebSocketManager.getInstance().setWebSocketListener(ChatActivity.this);
//        } else {
//            Toast.makeText(getApplicationContext(), "No user id in intent", Toast.LENGTH_LONG).show();
//            Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
//            intent.putExtra("id", userId);
//            startActivity(intent);
//        }
//
//
////        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
////            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
////            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
////            return insets;
////        });
//
//
//        /* send button listener */
//        sendButton.setOnClickListener(v -> {
//            try {
//                // send message
//                WebSocketManager.getInstance().sendMessage(msgEtx.getText().toString());
//            } catch (Exception e) {
//                Log.d("ExceptionSendMessage:", e.getMessage().toString());
//            }
//        });
//    }
//
//    @Override
//    public void onWebSocketMessage(String message) {
//        /**
//         * In Android, all UI-related operations must be performed on the main UI thread
//         * to ensure smooth and responsive user interfaces. The 'runOnUiThread' method
//         * is used to post a runnable to the UI thread's message queue, allowing UI updates
//         * to occur safely from a background or non-UI thread.
//         */
//        runOnUiThread(() -> {
//            //TODO - Add receiveMessage
////            String s = msgTv.getText().toString();
////            msgTv.setText(s + "\n"+message);
//        });
//    }
//
//    @Override
//    public void onWebSocketClose(int code, String reason, boolean remote) {
//        String closedBy = remote ? "server" : "local";
//        runOnUiThread(() -> {
//            //TODO - What we want to do on close
//
////            String s = msgTv.getText().toString();
////            msgTv.setText(s + "---\nconnection closed by " + closedBy + "\nreason: " + reason);
//        });
//    }
//
//    @Override
//    public void onWebSocketOpen(ServerHandshake handshakedata) {}
//
//    @Override
//    public void onWebSocketError(Exception ex) {}
//
//}
