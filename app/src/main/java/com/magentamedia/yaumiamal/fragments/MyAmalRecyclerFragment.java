package com.magentamedia.yaumiamal.fragments;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.magentamedia.yaumiamal.AturAmal;
import com.magentamedia.yaumiamal.R;
import com.magentamedia.yaumiamal.models.Alarm;
import com.magentamedia.yaumiamal.providers.YawmiMethodes;
import com.magentamedia.yaumiamal.receivers.YaumiReceivers;
import com.magentamedia.yaumiamal.services.ServIntentYaumi;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyAmalRecyclerFragment extends Fragment implements YaumiReceivers.onAmalResultListener {
    private RecyclerView recyclerView;
    private PickAmalListAdapter adapter;
    private List<Alarm> my_amal;
    private TextView meter;
    private ProgressBar pb;
    private YawmiMethodes me;
    private YaumiReceivers amal_datas;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        me = new YawmiMethodes();
        amal_datas = new YaumiReceivers(this);
        my_amal = new ArrayList<>();
    }

    @Override
    public void onStart() {
        super.onStart();

        IntentFilter filter = new IntentFilter();
        filter.addAction(ServIntentYaumi.ACTION_COMPLETE);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(amal_datas, filter);
    }

    @Override
    public void onStop() {
        super.onStop();

        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(amal_datas);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_list_amal_recyclerview, container, false);
        recyclerView = view.findViewById(R.id.my_amal_rv);
        meter = getActivity().findViewById(R.id.textMeter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        updateUI();

        return view;
    }

    private void updateUI() {
        adapter = new PickAmalListAdapter(my_amal);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onAmalLoaded(ArrayList<Alarm> amals) {
        my_amal = amals;
    }

    //holder
    private class AmalRecyclerViewHolder extends RecyclerView.ViewHolder {
        public TextView rlText;
        public TextView rlSubText;
        public CheckBox bt_store;
        public RelativeLayout container;
        public ImageView delbtn;
        public ImageView edtbtn;

        public AmalRecyclerViewHolder(View view) {
            super(view);
            rlText = view.findViewById(R.id.myamal_title);
            rlSubText = view.findViewById(R.id.myamal_subtitle);
            container = view.findViewById(R.id.my_amal_par);
            bt_store = view.findViewById(R.id.bt_setoramal);
            delbtn = view.findViewById(R.id.bt_deletelist);
            edtbtn = view.findViewById(R.id.bt_editlist);
            pb = getActivity().findViewById(R.id.progressBar2);
        }

        public void bindData(Alarm al) {
            String waktunya = "Dilakukan saat jam " + me.toTimes(new Date(al.getTime()));

            rlText.setText(me.capitalizem(al.getAlarmLabel().toLowerCase()).toString());
            rlSubText.setText(waktunya);
        }

    }

    //adapter
    private class PickAmalListAdapter extends RecyclerView.Adapter<AmalRecyclerViewHolder> {

        private List<Alarm> my_amalist;

        public PickAmalListAdapter(List<Alarm> data) {
            my_amalist = data;
        }

        @Override
        public AmalRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.my_list_amal_recycler_content, parent, false);

            return new AmalRecyclerViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final AmalRecyclerViewHolder holder, int position) {
            final Alarm al = my_amalist.get(position);
            final String label = my_amalist.get(position).getAlarmLabel();
            final long mTime = my_amalist.get(position).getTime();
            final Handler handler = new Handler();

            //click to finishing amal
            holder.bt_store.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    //TODO something remove list

                    if (isChecked) {

                        String sql = "UPDATE tb_passed_amal SET status_passed = 1 WHERE id_la = " +
                                "(SELECT id_la FROM tb_list_amalan WHERE amalan = '" + label + "')";

                        int res = me.SQLWriteMode(getActivity(), sql);

                        if (res > 0) {
                            me.onToast(getActivity(),holder.rlText.getText()
                                    .toString() + " Selesai Dikerjakan");

                            handler.postDelayed(new Runnable() {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void run() {
                                    meter.setText(String.valueOf((int) intData()) + "%");
                                    pb.setProgress((int) intData());
                                    removeAt(holder.getAdapterPosition());
                                }
                            }, 200);
                        }
                    }
                }
            });

            //delete click
            holder.delbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface
                            .OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int choice) {
                            switch (choice) {
                                case DialogInterface.BUTTON_POSITIVE:

                                    String sql = "DELETE from tb_amalanku WHERE id_la = " +
                                            "(SELECT id_la FROM tb_list_amalan WHERE amalan = '" + label + "')";
                                    me.SQLWriteMode(getActivity(), sql);
                                    removeAt(holder.getAdapterPosition());

                                    handler.postDelayed(new Runnable() {
                                        @SuppressLint("SetTextI18n")
                                        @Override
                                        public void run() {
                                            me.onToast(getActivity(), "Berhasil Menghapus Kegiatan");
                                        }
                                    }, 200);

                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:

                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Hapus Kegiatan ?")
                            .setPositiveButton("Ya", dialogClickListener)
                            .setNegativeButton("Tidak", dialogClickListener).show();

                }
            });

            //edit button
            holder.edtbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent nextPage = new Intent(getActivity(), AturAmal.class);
                    nextPage.putExtra("edited", true);
                    nextPage.putExtra("amal_label", label);

                    startActivity(nextPage);
                }
            });

            //bind data to view
            holder.bindData(al);

        }

        private double intData() {

            String where = "date_today = '"+me.toDBDate(System.currentTimeMillis())+"'";
            String keys = "target_today, done_today";
            Cursor cursor = me.getSpecificData(getActivity(), "tb_mypoint", keys, where);

            if (cursor.getCount() > 0) {

                return (cursor.getDouble(1) / cursor.getDouble(0)) * 100;
            } else {
                return  0;
            }
        }

        @Override
        public int getItemCount() {
            return my_amal.size();
        }

        public void removeAt(int p) {
            my_amal.remove(p);
            notifyItemRemoved(p);
            notifyItemRangeChanged(p, my_amal.size());
        }
    }

}