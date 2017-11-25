package com.hussain.smalltalk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.hussain.smalltalk.MainActivity.mDatabase;

public class UserActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    TextView Uname;
    RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    public FirebaseRecyclerAdapter<ChatHelper, Show_Chat_ViewHolder> mFirebaseAdapter;
    ProgressBar progressBar;
    LinearLayoutManager mLinearLayoutManager;
    static String LoggedIn_User_Email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
        mAuth = FirebaseAuth.getInstance();
        Uname = (TextView) findViewById(R.id.textView2);
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference("User");
            myRef.keepSynced(true);
        progressBar = (ProgressBar) findViewById(R.id.show_chat_progressBar2);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mLinearLayoutManager = new LinearLayoutManager(UserActivity.this);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        if (mAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
        onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            Uname.setText("Welcome, " + user.getDisplayName());
            LoggedIn_User_Email =user.getEmail();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.signout:
                mAuth.getInstance().signOut();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        progressBar.setVisibility(ProgressBar.VISIBLE);
        mFirebaseAdapter = new FirebaseRecyclerAdapter<ChatHelper, Show_Chat_ViewHolder>(ChatHelper.class, R.layout.user_layout, Show_Chat_ViewHolder.class, myRef) {

            public void populateViewHolder(final Show_Chat_ViewHolder viewHolder, ChatHelper model, final int position) {
                progressBar.setVisibility(ProgressBar.INVISIBLE);
                if (!model.getName().equals("Null")) {
                    viewHolder.Person_Name(model.getName());

                    if (model.getEmail().equals(LoggedIn_User_Email)) {
                        viewHolder.Layout_hide();
                    } else
                        viewHolder.Person_Email(model.getEmail());
                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(final View v) {

                            DatabaseReference ref = mFirebaseAdapter.getRef(position);
                            ref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String retrieve_name = dataSnapshot.child("Name").getValue(String.class);
                                    String retrieve_Email = dataSnapshot.child("Email").getValue(String.class);
                                    Intent intent = new Intent(getApplicationContext(), ConActivity.class);
                                    intent.putExtra("email", retrieve_Email);
                                    intent.putExtra("name", retrieve_name);
                                    startActivity(intent);
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    });
                }
            }
        };

        recyclerView.setAdapter(mFirebaseAdapter);
    }



    public static class Show_Chat_ViewHolder extends RecyclerView.ViewHolder {
        private final TextView person_name, person_email;

        private final RelativeLayout layout;
        final LinearLayout.LayoutParams params;

        public Show_Chat_ViewHolder(final View itemView) {
            super(itemView);
            person_name = (TextView) itemView.findViewById(R.id.chat_persion_name);
            person_email = (TextView) itemView.findViewById(R.id.chat_persion_email);

            layout = (RelativeLayout) itemView.findViewById(R.id.show_chat_single_item_layout);
            params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }


        private void Person_Name(String title) {
            person_name.setText(title);
        }
        private void Layout_hide() {
            params.height = 0;
            layout.setLayoutParams(params);

        }

        private void Person_Email(String title) {
            person_email.setText(title);
        }

    }
}
