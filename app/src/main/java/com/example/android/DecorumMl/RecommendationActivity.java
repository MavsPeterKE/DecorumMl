package com.example.android.DecorumMl;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.android.DecorumMl.adapters.RecommendationsAdapter;
import com.example.android.DecorumMl.models.Furniture;

import java.util.ArrayList;
import java.util.List;


@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class RecommendationActivity extends AppCompatActivity {


    RecommendationsAdapter recommendationsAdapter;
    ArrayList<Furniture> sampleDataList = new ArrayList<>() ;
    RecyclerView recyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendations);
        setRecommendationList();
    }

    private void setRecommendationList() {
        recyclerView = findViewById(R.id.rec_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        recommendationsAdapter = new RecommendationsAdapter(RecommendationActivity.this, buildSampleData());
        recyclerView.setAdapter(recommendationsAdapter);
    }

    private List<Furniture> buildSampleData(){
        for (int i = 0; i<SampleData.ITEM_NAMES.length;i++){
            Furniture furniture = new Furniture();
            furniture.name = SampleData.ITEM_NAMES [i];
            furniture.imageId = SampleData.IMAGE_IDS [i];
            furniture.decription = SampleData.DESCRIPTIONS [i];
            furniture.amount = SampleData.PRICE_ITEM [i];
            sampleDataList.add(furniture);
        }
        return sampleDataList;
    }
}
