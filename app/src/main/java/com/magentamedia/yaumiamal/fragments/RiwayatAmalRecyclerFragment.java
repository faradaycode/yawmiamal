package com.magentamedia.yaumiamal.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.magentamedia.yaumiamal.R;
import com.magentamedia.yaumiamal.models.RiwayatAmalModel;
import com.magentamedia.yaumiamal.providers.YawmiMethodes;

import java.util.ArrayList;
import java.util.List;

public class RiwayatAmalRecyclerFragment extends Fragment {
    private RecyclerView recyclerView;
    private PickAmalListAdapter adapter;
    private List<RiwayatAmalModel> my_riwayat;
    private CompactCalendarView calend;
    private YawmiMethodes me = new YawmiMethodes();
    private String ki;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle == null) {
            ki = me.toDBDate(System.currentTimeMillis());
            Log.d("nobundle", ki);
        } else {
            ki = bundle.getString("selectedate");
            Log.d("bundle", ki);
        }

        getDatas(ki);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.riwayat_list_amal_recyclerview, container, false);

        recyclerView = view.findViewById(R.id.ri_amal_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));

        updateUI();

        return view;
    }

    private void updateUI() {
        adapter = new PickAmalListAdapter(my_riwayat);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    //holder
    private class AmalRecyclerViewHolder extends RecyclerView.ViewHolder {
        public TextView rlText;
        public ImageView finished_time;
        public ImageView forgeted_time;
        public RelativeLayout container;

        public AmalRecyclerViewHolder(View view) {
            super(view);
            rlText = view.findViewById(R.id.r_amal_title);
            container = view.findViewById(R.id.r_amal_par);
            finished_time = view.findViewById(R.id.st_finished);
            forgeted_time = view.findViewById(R.id.st_forgeted);
        }

        public void bindData(RiwayatAmalModel al) {
            rlText.setText(me.capitalizem(al.getAmalan().toLowerCase()).toString());

            if (al.getStatus() != 0) {
                forgeted_time.setVisibility(View.GONE);
            } else {
                finished_time.setVisibility(View.GONE);
            }
        }

    }

    //adapter
    private class PickAmalListAdapter extends RecyclerView.Adapter<AmalRecyclerViewHolder> {

        private List<RiwayatAmalModel> my_riwayatist;

        public PickAmalListAdapter(List<RiwayatAmalModel> data) {
            my_riwayatist = data;
        }

        @Override
        public AmalRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.riwayat_amal_recycler_content, parent, false);

            return new AmalRecyclerViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final AmalRecyclerViewHolder holder, int position) {
            final RiwayatAmalModel al = my_riwayatist.get(position);

            //bind data to view
            holder.bindData(al);

        }

        @Override
        public int getItemCount() {
            return my_riwayat.size();
        }

    }

    private void getDatas(String date) {

        String sql = "SELECT a.id_a, b.amalan, c.status_passed FROM tb_amalanku a JOIN " +
                "tb_list_amalan b ON a.id_la = b.id_la JOIN tb_passed_amal c ON c.id_a = a.id_a " +
                "WHERE c.date_passed = '" + date + "'";

        Cursor csr = me.joinData(getActivity(), sql);

        my_riwayat = new ArrayList<>();

        for (int i = 0; i < csr.getCount(); i++) {

            csr.moveToPosition(i);

            my_riwayat.add(new RiwayatAmalModel(csr.getInt(0), csr.getString(1), csr.getInt(2)));
            
        }

        Log.d("getdate", ""+csr.getCount());
    }
}
