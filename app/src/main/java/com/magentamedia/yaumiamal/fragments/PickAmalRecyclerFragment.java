package com.magentamedia.yaumiamal.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.magentamedia.yaumiamal.AturAmal;
import com.magentamedia.yaumiamal.R;
import com.magentamedia.yaumiamal.models.AmalanList;
import com.magentamedia.yaumiamal.providers.YawmiMethodes;
import com.magentamedia.yaumiamal.utils.CustomDividerItemDecorator;
import com.magentamedia.yaumiamal.utils.VerticalSpaceDecorator;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PickAmalRecyclerFragment extends Fragment {
    private RecyclerView recyclerView;
    private PickAmalListAdapter adapter;
    private ArrayList<AmalanList> amalanLists;
    private YawmiMethodes me;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getDatas();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pick_amal_recyclerview, container, false);
        recyclerView = view.findViewById(R.id.pick_amal_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.addItemDecoration(new VerticalSpaceDecorator(16));
        updateUI();
        return view;
    }

    private void updateUI() {
        adapter = new PickAmalListAdapter(amalanLists);
        recyclerView.setAdapter(adapter);
    }

    //holder
    private class AmalRecyclerViewHolder extends RecyclerView.ViewHolder {

        public TextView rlText;
        public LinearLayout container;

        public AmalRecyclerViewHolder(View view) {
            super(view);
            rlText = view.findViewById(R.id.tx_am_title);
            container = view.findViewById(R.id.par_container);
        }

        public void bindData(AmalanList al) {
            rlText.setText(al.getAmal());
        }

    }

    //adapter
    private class PickAmalListAdapter extends RecyclerView.Adapter<AmalRecyclerViewHolder> {

        private ArrayList<AmalanList> amals;

        public PickAmalListAdapter(ArrayList<AmalanList> data) {
            amals = data;
        }

        @Override
        public AmalRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.pick_amal_recycler_content, parent, false);

            return new AmalRecyclerViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final AmalRecyclerViewHolder holder, int position) {
            final AmalanList al = amals.get(position);
            final int ids = amals.get(position).getId();
            final String teks = amals.get(position).getAmal();
            final int group = amals.get(position).getGrouping();

            Pattern p1 = Pattern.compile(".*?\\b(SUBUH|DZUHUR|ASHAR|MAGHRIB|ISYA)\\b.*?");
            final Matcher m = p1.matcher(teks);

            //click to get ID
            holder.container.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), AturAmal.class);
                        intent.putExtra("index", holder.getAdapterPosition()+1);
                        intent.putExtra("amalan", teks);
                        intent.putExtra("ida", ids);
                        intent.putExtra("group", group);
                        intent.putExtra("edited", false);
                        startActivity(intent);
//                    }
                }
            });

            holder.bindData(al);
        }

        @Override
        public int getItemCount() {
            return amals.size();
        }

    }

    private void getDatas() {
        AmalanList lists;
        me = new YawmiMethodes();
        String where = "tipe != 'c'";

        Cursor csr = me.getSpecificData(getActivity(), "tb_list_amalan", "*",
                where);

        amalanLists = new ArrayList<>();

        for (int i = 0; i<csr.getCount(); i++) {
            csr.moveToPosition(i);
            lists = new AmalanList();

            lists.setId(csr.getInt(0));
            lists.setAmal(me.capitalizem(csr.getString(1).toLowerCase()).toString());
            lists.setType(csr.getString(2));
            lists.setGrouping(csr.getInt(3));

            amalanLists.add(lists);
        }
    }
}
