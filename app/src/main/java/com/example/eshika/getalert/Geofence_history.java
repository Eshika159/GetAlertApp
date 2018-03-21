package com.example.eshika.getalert;

import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Geofence_history extends AppCompatActivity {
RecyclerView recyclerView;
QuickAdapter adapter;
//Context mcontext=(navActivity)getApplicationContext();
        //=(navActivity)getApplicationContext();
DbHelper db;

  public Geofence_history(){

  }

//public  Geofence_history(Context mcontext){

  //  this.mcontext=mcontext;


//}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geofence_history);
//db= (DbHelper) getIntent().getSerializableExtra("object");

          // mcontext=  (Context) bundle.get("context");
       db=new DbHelper(this);
        recyclerView=(RecyclerView)findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

               //load place from db and set in adapter a list
      loadtask();
      //  List<CustomList> list=db.getAll();
        //  List<CustomList> list=new ArrayList<>();
        // list.add(new CustomList("dit",67,78));
        //adapter = new QuickAdapter(getApplicationContext(), list);
        //recyclerView.setAdapter(adapter);

    }

    private void loadtask() {
        List<CustomList> list=db.getAll();

       //List<CustomList> list=new ArrayList<>();
       // list.add(new CustomList("dit",67,78));
        adapter = new QuickAdapter(getApplicationContext(), list);
        recyclerView.setAdapter(adapter);

    }


}
