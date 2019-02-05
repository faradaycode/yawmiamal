package com.magentamedia.yaumiamal.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.magentamedia.yaumiamal.R;
import com.magentamedia.yaumiamal.models.ArtikelMessagesModel;
import com.magentamedia.yaumiamal.models.ArtikelModel;
import com.magentamedia.yaumiamal.models.MyAmalModel;
import com.magentamedia.yaumiamal.models.yawmiClient;
import com.magentamedia.yaumiamal.models.yawmiServerAPI;
import com.magentamedia.yaumiamal.providers.YawmiMethodes;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArtikelRecyclerFragment extends Fragment {
    
    private RecyclerView recyclerView;
    private ArtikelListAdapter adapter;
    private List<ArtikelMessagesModel> artikelMessagesModels;
    private YawmiMethodes me = new YawmiMethodes();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getDatas();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, 
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.artikel_recyclerview, container, false);
        
        recyclerView = view.findViewById(R.id.artikel_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        
        updateUI();

        return view;
    }

    private void updateUI() {
        adapter = new ArtikelListAdapter(artikelMessagesModels);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    //holder
    private class ArtikelRecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView artitled;
        TextView arliked;
        TextView ardisliked;
        TextView arneutral;
        TextView arviewed;
        public RelativeLayout container;

        public ArtikelRecyclerViewHolder(View view) {
            super(view);

            container = view.findViewById(R.id.artikel_rv_container);
            artitled = view.findViewById(R.id.artikel_title);
            ardisliked = view.findViewById(R.id.artikel_sad);
            arliked = view.findViewById(R.id.artikel_like);
            arneutral = view.findViewById(R.id.artikel_neutral);
            arviewed = view.findViewById(R.id.artikel_viewed);

        }

        public void bindData(ArtikelMessagesModel al) {
            artitled.setText(me.capitalizem(al.getArTitle()));
            arviewed.setText(al.getArViewed());
            arliked.setText(al.getArLikes());
            ardisliked.setText(al.getArDislikes());
            arneutral.setText(al.getArNeutral());
        }

    }

    //adapter
    private class ArtikelListAdapter extends RecyclerView.Adapter<ArtikelRecyclerViewHolder> {

        private List<ArtikelMessagesModel> artikelist;

        public ArtikelListAdapter(List<ArtikelMessagesModel> data) {
            artikelist = data;
        }

        @Override
        public ArtikelRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.artikel_recyclerview_content, parent, false);

            return new ArtikelRecyclerViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ArtikelRecyclerViewHolder holder, int position) {
            final ArtikelMessagesModel al = artikelist.get(position);
            final int idx = artikelist.get(position).getIdAr();

            //bind data to view
            holder.bindData(al);

        }

        @Override
        public int getItemCount() {
            return artikelMessagesModels.size();
        }

    }

    private void getDatas() {

        artikelMessagesModels = new ArrayList<>();

        yawmiServerAPI api = yawmiClient.getRetrofit().create(yawmiServerAPI.class);
        Call<ArtikelModel> call = api.getPostingan();

        call.enqueue(new Callback<ArtikelModel>() {
            @Override
            public void onResponse(Call<ArtikelModel> call, Response<ArtikelModel> response) {

                ArtikelMessagesModel lists = response.body().getMessage();

                if (response.body().getStatus().equals("OK")) {
                    artikelMessagesModels.add(lists);
                }

            }

            @Override
            public void onFailure(Call<ArtikelModel> call, Throwable t) {
                Log.e("ARTHROW", t.getMessage());
            }
        });
    }
}
