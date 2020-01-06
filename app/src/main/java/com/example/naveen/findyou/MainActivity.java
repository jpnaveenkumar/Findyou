package com.example.naveen.findyou;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    public EditText e1;
    public Button b1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        e1=(EditText)findViewById(R.id.e1);
        b1=(Button)findViewById(R.id.b1);
        Firebase.setAndroidContext(this);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final String user=e1.getText().toString().trim();
                Firebase fm=new Firebase("https://androidtrainee-7e562.firebaseio.com/user/usernames/"+user+"/name");
                fm.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                        {
                            String h=dataSnapshot.getValue(String.class);
                            Toast.makeText(getApplicationContext(),"Tracking "+h,Toast.LENGTH_LONG).show();
                            Intent i=new Intent(MainActivity.this,MapsActivity.class);
                            i.putExtra("name",user);
                            i.putExtra("name1",h);
                            startActivity(i);
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Incorrect user",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
            }
        });
    }
}
