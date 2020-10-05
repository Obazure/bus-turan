package com.example.navigator.busturan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.navigator.busturan.adapters.RouteListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    public static final String ROUTE_NAME = "r-name";
    public static final String ROUTE_KEY = "r-key";

    ListView lvMain;
    ArrayList<Map<String, Object>> data;

   RouteListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvMain = findViewById(R.id.lv_main);

        DatabaseReference fireRef = FirebaseDatabase.getInstance().getReference();
        fireRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                data = new ArrayList<Map<String, Object>>((int) dataSnapshot.child("routes").getChildrenCount());
                Map<String, Object> m;
                for (DataSnapshot routeID : dataSnapshot.child("routes").getChildren()) {
                    m = new HashMap<String, Object>();
                    m.put(ROUTE_KEY, routeID.getKey());
                    m.put(ROUTE_NAME, routeID.child("name").getValue(String.class));
                    data.add(m);
                }
                adapter = new RouteListAdapter(MainActivity.this, data);
                lvMain.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("TAG",databaseError.getMessage());
            }
        });
        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String route_key = data.get(position).get(ROUTE_KEY).toString();
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                intent.putExtra(ROUTE_KEY,route_key);
                startActivity(intent);
            }
        });
    }
}
