package com.niki.wihealth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.niki.wihealth.adapter.SearchListAdapter;
import com.niki.wihealth.model.SportCenter;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

public class SearchSportCenter extends AppCompatActivity {
    ArrayList<SportCenter> sportCenters;
    SearchListAdapter adapter;
    String query = "";
    FrameLayout blur;
    EditText etSearch;
    ImageView ivQr;
    AVLoadingIndicatorView search_loader;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_sport_center);

        ivQr = findViewById(R.id.iv_qrcode_search);
        query = getIntent().getStringExtra("query");
        search_loader = findViewById(R.id.search_loader);
        blur = findViewById(R.id.background_blur_search);
        etSearch = findViewById(R.id.etSearchSearch);

        blur.setBackgroundResource(R.drawable.button_background);
        search_loader.setVisibility(View.VISIBLE);
        search_loader.show();

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH){
                    String query = etSearch.getText().toString();
                    Intent intent = new Intent(getApplicationContext(), SearchSportCenter.class);
                    intent.putExtra("query", query);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });

        ivQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), QrCodeScanner.class));
            }
        });

        RecyclerView recyclerView;
        sportCenters = new ArrayList<>();
        recyclerView = findViewById(R.id.rv_search_sport_station);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SearchListAdapter(getApplicationContext(), sportCenters);
        recyclerView.setAdapter(adapter);

        CollectionReference sportRef = db.collection("sport_center");
        sportRef.whereEqualTo("name", query).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()){
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                    for (DocumentSnapshot d : list){
                        SportCenter s = d.toObject(SportCenter.class);

                        sportCenters.add(s);
                    }
                    adapter.notifyDataSetChanged();
                    search_loader.setVisibility(View.INVISIBLE);
                }
                search_loader.setVisibility(View.INVISIBLE);
                blur.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}
