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

public class CategoryList extends AppCompatActivity {
    ArrayList<SportCenter> sportCenters;
    SearchListAdapter adapter;
    String query = "";
    FrameLayout blur;
    EditText etSearch;
    ImageView ivQr;
    AVLoadingIndicatorView search_loader;
    DataPassing dataPassing = DataPassing.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        ivQr = findViewById(R.id.iv_qrcode_category);
        query = getIntent().getStringExtra("query");
        search_loader = findViewById(R.id.category_loader);
        blur = findViewById(R.id.background_blur_category);
        etSearch = findViewById(R.id.etSearchCategory);

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
        recyclerView = findViewById(R.id.rv_category);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SearchListAdapter(getApplicationContext(), sportCenters);
        recyclerView.setAdapter(adapter);

        CollectionReference sportRef = db.collection("sport_center");
        sportRef.whereEqualTo("category", query).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()){
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    int i = 0;
                    for (DocumentSnapshot d : list){
                        SportCenter s = d.toObject(SportCenter.class);
                        sportCenters.add(s);
                        double lat = Double.parseDouble(sportCenters.get(i).getLat());
                        double lon = Double.parseDouble(sportCenters.get(i).getLon());
                        double euclidean = ((Math.sqrt(Math.pow(lat - dataPassing.getUserLat(), 2)) + (Math.sqrt(Math.pow(lon - dataPassing.getUserLon(), 2)))) * 111.319);
                        sportCenters.get(i).setDistance(Math.round(euclidean * 10) / 10.0);
                        i++;

                    }
                    adapter.notifyDataSetChanged();
                    search_loader.setVisibility(View.INVISIBLE);
                }
                search_loader.setVisibility(View.INVISIBLE);
                blur.setVisibility(View.INVISIBLE);
            }
        });

    }
}
