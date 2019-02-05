package com.magentamedia.yaumiamal.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.magentamedia.yaumiamal.R;
import com.magentamedia.yaumiamal.models.JadwalSholat;
import com.magentamedia.yaumiamal.providers.YawmiMethodes;
import com.magentamedia.yaumiamal.services.ServIntentYaumi;
import com.magentamedia.yaumiamal.utils.sharedPreferenceSingleton;

import java.util.ArrayList;

public class ListAdzanRecyclerFragment extends Fragment {

    private RecyclerView recyclerView;
    public ArrayList<JadwalSholat> adzanLists;
    private YawmiMethodes me;
    private sharedPreferenceSingleton pref;
    private static String[] spKey = new String[]{"IS_FAJR_ACTIVE", "IS_ZUHR_ACTIVE", "IS_ASR_ACTIVE",
            "IS_MAGHRIB_ACTIVE", "IS_ISHA_ACTIVE"};
    private String next_azan;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pref = sharedPreferenceSingleton.getInstance(getContext());
        me = new YawmiMethodes();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_adzan_recyclerview, container, false);
        adzanLists = getArguments().getParcelableArrayList("fragment_data");
        next_azan = getArguments().getString("next_azan");
        recyclerView = view.findViewById(R.id.list_adzan_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    private void updateUI() {
        PickAdzanListAdapter adapter = new PickAdzanListAdapter(adzanLists);
        recyclerView.setAdapter(adapter);
    }

    //holder
    private class AdzanRecyclerViewHolder extends RecyclerView.ViewHolder {
        private JadwalSholat models;
        TextView rlText;
        TextView rlwaktu;
        public LinearLayout container;
        public ImageView bt_notifON;
        public ImageView bt_notifOFF;

        public AdzanRecyclerViewHolder(View view) {

            super(view);

            rlText = view.findViewById(R.id.rv_nama_adzan_text);
            rlwaktu = view.findViewById(R.id.rv_waktu_adzan_text);
            container = view.findViewById(R.id.ls_adzan_parent);
            bt_notifON = view.findViewById(R.id.bt_notif_on);
            bt_notifOFF = view.findViewById(R.id.bt_notif_of);
        }

        public void bindData(JadwalSholat jadwalSholat) {
            models = jadwalSholat;
            rlText.setText(me.capitalizem(models.getAzan_label()).toString());
            rlwaktu.setText(me.toTimes(models.getAzan_time()));
        }

    }

    //adapter
    private class PickAdzanListAdapter extends RecyclerView.Adapter<AdzanRecyclerViewHolder> {

        private ArrayList<JadwalSholat> adapterData;

        public PickAdzanListAdapter(ArrayList<JadwalSholat> data) {
            adapterData = data;
        }

        @Override
        public AdzanRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_adzan_recycler_content, parent,
                    false);

            return new AdzanRecyclerViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final AdzanRecyclerViewHolder holder, final int position) {

            final JadwalSholat al = adapterData.get(position);
            holder.bt_notifON.setVisibility(View.GONE);
            holder.bt_notifOFF.setVisibility(View.GONE);


            //alarm state
            for (int i = 0; i < adapterData.size(); i++) {
                if (position == i) {
                    if (adapterData.get(i).isAzan_state()) {
                        holder.bt_notifON.setVisibility(View.VISIBLE);
                        holder.bt_notifOFF.setVisibility(View.GONE);
                    } else {
                        holder.bt_notifON.setVisibility(View.GONE);
                        holder.bt_notifOFF.setVisibility(View.VISIBLE);
                    }

                    if (adapterData.get(i).getAzan_label().toLowerCase().equals(next_azan)) {
                        holder.rlText.setTextColor(getResources().getColor(R.color.magenta_light));
                        holder.rlwaktu.setTextColor(getResources().getColor(R.color.magenta_light));
                    }
                }

            }

            //click event for activate alarm
            holder.bt_notifON.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    //set preferences to false each azan alarm clicked
                    pref.put(sharedPreferenceSingleton.Key.valueOf(spKey[position]), false);
                    holder.bt_notifON.setVisibility(View.GONE);
                    holder.bt_notifOFF.setVisibility(View.VISIBLE);

                    ServIntentYaumi.LauncherService(getContext());
                }

            });

            //click event for shutdown alarm
            holder.bt_notifOFF.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    //set preferences to true each azan alarm clicked
                    pref.put(sharedPreferenceSingleton.Key.valueOf(spKey[position]), true);
                    holder.bt_notifON.setVisibility(View.VISIBLE);
                    holder.bt_notifOFF.setVisibility(View.GONE);

                    ServIntentYaumi.LauncherService(getContext());

                }
            });

            holder.bindData(al);
        }


        @Override
        public int getItemCount() {
            return adapterData.size();
        }

    }

}
