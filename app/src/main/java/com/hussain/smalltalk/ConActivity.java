package com.hussain.smalltalk;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Hussain on 31-Jul-17.
 */

public class ConActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef,myRef2;
    private FirebaseRecyclerAdapter<ConversationHelper, Conversation_ViewHolder> mFirebaseAdapter;
    public LinearLayoutManager mLinearLayoutManager;
    static String Sender_Name;
    ImageView send_icon;
    EditText message_area;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation_layout);

        String USER_ID = UserActivity.LoggedIn_User_Email.replace("@","").replace(".","");
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference().child("Chat").child(USER_ID).child(getIntent().getStringExtra("email").replace("@","").replace(".",""));
        myRef.keepSynced(true);
        myRef2 = FirebaseDatabase.getInstance().getReference().child("Chat").child(getIntent().getStringExtra("email").replace("@","").replace(".","")).child(USER_ID);
        myRef2.keepSynced(true);
        Sender_Name = getIntent().getStringExtra("name");
        recyclerView = (RecyclerView)findViewById(R.id.fragment_chat_recycler_view);
        send_icon = (ImageView)findViewById(R.id.sendButton);
        message_area = (EditText)findViewById(R.id.messageArea);
        mLinearLayoutManager = new LinearLayoutManager(ConActivity.this);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        mLinearLayoutManager.setStackFromEnd(true);
        send_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = message_area.getText().toString().trim();

                if(!messageText.equals("")){
                    ArrayMap<String, String> map = new ArrayMap<>();
                    map.put("message", messageText);
                    map.put("sender", "you");
                    myRef.push().setValue(map);
                    myRef2.push().setValue(map);
                    message_area.setText("");
                    recyclerView.postDelayed(new Runnable() {
                        @Override public void run()
                        {
                            recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount()-1);

                        }
                    }, 500);
                }
            }
        });

    }


    @Override
    public void onStart() {
       super.onStart();
        mFirebaseAdapter = new FirebaseRecyclerAdapter<ConversationHelper, Conversation_ViewHolder>(ConversationHelper.class, R.layout.conversation_item_layout, Conversation_ViewHolder.class, myRef) {
            public void populateViewHolder(final Conversation_ViewHolder viewHolder, ConversationHelper model, final int position) {
                viewHolder.getSender(model.getSender());
                viewHolder.getMessage(model.getMessage());
            }
        };
        recyclerView.setAdapter(mFirebaseAdapter);

       myRef.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren())
                {
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView.postDelayed(new Runnable() {
                        @Override public void run()
                        {
                            recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount()-1);
                        }
                    }, 500);
                    recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                        @Override
                        public void onLayoutChange(View v,
                                                   int left, int top, int right, int bottom,
                                                   int oldLeft, int oldTop, int oldRight, int oldBottom) {
                            if (bottom < oldBottom) {
                                recyclerView.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
                                    }
                                }, 100);
                            }
                        }
                    });
                }
                else {
                    recyclerView.setVisibility(View.GONE);
                }
            }

           @Override
           public void onCancelled(DatabaseError databaseError) {
           }
       });
    }


    public static class Conversation_ViewHolder extends RecyclerView.ViewHolder {
        private final TextView message, sender;
        View mView;
        LinearLayout layout;

        public Conversation_ViewHolder(final View itemView) {
            super(itemView);
            mView = itemView;
            message = (TextView) mView.findViewById(R.id.fetch_chat_messgae);
            sender = (TextView) mView.findViewById(R.id.fetch_chat_sender);
            layout = (LinearLayout) mView.findViewById(R.id.chat_linear_layout);
        }

        private void getSender(String title) {

            if(title.equals(UserActivity.LoggedIn_User_Email))
            {
                sender.setText("You");
            }
            else
            {
                sender.setText(Sender_Name);
            }
        }
        private void getMessage(String title) {

            if(!title.startsWith("https"))
            {
                message.setText(title);
                message.setTextColor(Color.parseColor("#000000"));
                message.setVisibility(View.VISIBLE);
            }
        }
    }
}
