package com.example.eshika.getalert;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Eshika on 04-Feb-18.
 */

public class QuickAdapter extends RecyclerView.Adapter<QuickAdapter.QuickView>{

Context mcontext;
List<CustomList> list;


    public QuickAdapter(Context mcontext,List<CustomList>list) {
        this.mcontext=mcontext;
        this.list=list;

    }

    @Override
    public QuickView onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(mcontext);
      View view=  inflater.inflate(R.layout.list_history,parent,false);

return new QuickView(view,list);
    }

    @Override
    public void onBindViewHolder(QuickView holder, int position) {
        CustomList l=list.get(position);

        holder.place.setText(l.getPlacename());
        holder.lat.setText(String.valueOf(l.getLatitude()));
        holder.lng.setText(String.valueOf(l.getLongitude()));

    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public class QuickView extends RecyclerView.ViewHolder{
        List<CustomList>list;
Context mcontext;
TextView place;
TextView lat;
TextView lng;

        public QuickView(View itemView,List<CustomList>list) {
            super(itemView);
            this.list=list;
            place=(TextView)itemView.findViewById(R.id.place);
            lat=(TextView)itemView.findViewById(R.id.lat);
            lng=(TextView)itemView.findViewById(R.id.lng);

        }
    }

}
