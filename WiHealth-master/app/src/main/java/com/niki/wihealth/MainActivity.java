package com.niki.wihealth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.niki.wihealth.adapter.NearbyListAdapter;
import com.niki.wihealth.adapter.SearchListAdapter;
import com.niki.wihealth.model.SportCenter;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    ArrayList<SportCenter> sportCenters;
    NearbyListAdapter adapter;
    ViewFlipper flipper;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    ImageView ivQr;
    EditText etSearch;
    DataPassing dataPassing = DataPassing.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CardView basket, badminton, swimming, gym, tennis, football;

    Button btnPt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar  = findViewById(R.id.toolbar1);

        setSupportActionBar(toolbar);

        flipper = findViewById(R.id.v_flipper);
        ivQr = findViewById(R.id.iv_qrcode);
        etSearch = findViewById(R.id.etSearch);
        badminton = findViewById(R.id.badminton);
        basket = findViewById(R.id.basket);
        football = findViewById(R.id.football);
        swimming = findViewById(R.id.swimming);
        gym = findViewById(R.id.gym);
        tennis = findViewById(R.id.tennis);
        btnPt= findViewById(R.id.btnPt);

        btnPt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),PersonalTrainer.class);
                startActivity(intent);
            }
        });

        basket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CategoryList.class);
                intent.putExtra("query", "basket");
                startActivity(intent);
            }
        });
        badminton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CategoryList.class);
                intent.putExtra("query", "badminton");
                startActivity(intent);
            }
        });
        football.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CategoryList.class);
                intent.putExtra("query", "football");
                startActivity(intent);
            }
        });
        gym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CategoryList.class);
                intent.putExtra("query", "gym");
                startActivity(intent);
            }
        });
        swimming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CategoryList.class);
                intent.putExtra("query", "swimming");
                startActivity(intent);
            }
        });
        tennis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CategoryList.class);
                intent.putExtra("query", "tennis");
                startActivity(intent);
            }
        });


        dataPassing.setUserLat(-6.244185);
        dataPassing.setUserLon(106.802963);

        RecyclerView recyclerView;
        sportCenters = new ArrayList<>();
        recyclerView = findViewById(R.id.rv_search_nearby);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NearbyListAdapter(getApplicationContext(), sportCenters);
        recyclerView.setAdapter(adapter);

        db.collection("sport_center").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()){
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    int i = 0;
                    Log.d("LOGGER", "" + list.size());
                    for (DocumentSnapshot d : list){
                        SportCenter f = d.toObject(SportCenter.class);
                        sportCenters.add(f);
                        if (i == 4){
                            break;
                        }
                        double lat = Double.parseDouble(sportCenters.get(i).getLat());
                        double lon = Double.parseDouble(sportCenters.get(i).getLon());
//
                        double euclidean = ((Math.sqrt(Math.pow(lat - dataPassing.getUserLat(), 2)) + (Math.sqrt(Math.pow(lon - dataPassing.getUserLon(), 2)))) * 111.319);
                        sportCenters.get(i).setDistance(Math.round(euclidean * 10) / 10.0);
                        i++;
                    }
                    Collections.sort(sportCenters);
                    adapter.notifyDataSetChanged();
                    Log.d("LOGGER",""+ adapter.getItemCount());
                }
                else{
                    Toast.makeText(getApplicationContext(), "Aw, Snap!, Your Qr Code is broken", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

        });

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH){
                    String query = etSearch.getText().toString();
                    Intent intent = new Intent(getApplicationContext(), SearchSportCenter.class);
                    intent.putExtra("query", query);
                    startActivity(intent);
                    finish();
                    return true;
                }
                return false;
            }
        });

        int image[] = {R.drawable.banner1,R.drawable.banner2,R.drawable.banner3};

        for(int images:image){
            flipperImage(images);
        }

        ivQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), QrCodeScanner.class));
            }
        });
    }

    public void flipperImage(int image){
        ImageView imageView = new ImageView(this);

        imageView.setBackgroundResource(image);

        flipper.addView(imageView);
        flipper.setFlipInterval(2000);
        flipper.setAutoStart(true);

        flipper.setInAnimation(this,android.R.anim.slide_in_left);
        flipper.setOutAnimation(this,android.R.anim.slide_out_right);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseAuth.getCurrentUser() == null){
            startActivity(new Intent(getApplicationContext(), Login.class));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile:
                Intent i = new Intent(MainActivity.this, Profile.class);
                startActivity(i);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
