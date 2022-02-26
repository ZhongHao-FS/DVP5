package com.fullsail.android.dvp5.pocketchef.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.fullsail.android.dvp5.pocketchef.ListAdapter;
import com.fullsail.android.dvp5.pocketchef.ListItem;
import com.fullsail.android.dvp5.pocketchef.R;
import com.fullsail.android.dvp5.pocketchef.Recipe;
import com.fullsail.android.dvp5.pocketchef.utilities.DetailWorker;
import com.fullsail.android.dvp5.pocketchef.utilities.NetworkUtility;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailsFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "DetailsFragment.TAG";
    public static final String BROADCAST_ACTION = "com.fullsail.android.dvp5.pocketchef.BROADCAST_ACTION_DETAIL";
    public static int ID = 0;
    private Context mContext;
    private BroadcastReceiver mReceiver;
    private Recipe mRecipe;

    public DetailsFragment() { super(R.layout.fragment_details); }

    public static DetailsFragment newInstance(int id) {
        Bundle args = new Bundle();
        args.putInt(TAG, id);

        DetailsFragment fragment = new DetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getContext();
        ID = requireArguments().getInt(TAG, 0);
        if (mContext != null && ID != 0) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(BROADCAST_ACTION);
            mReceiver = new UpdateReceiver();
            LocalBroadcastManager.getInstance(mContext).registerReceiver(mReceiver, intentFilter);

            if(NetworkUtility.isConnected(mContext)) {
                Constraints constraints = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build();
                OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(DetailWorker.class).setConstraints(constraints).build();
                WorkManager.getInstance(mContext).enqueue(workRequest);
            } else {
                Toast.makeText(mContext, "Network is not connected", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button addButton = view.findViewById(R.id.button_add);
        addButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

    }

    private void showRecipe(@NonNull View view) {
        ImageView iv_detail = view.findViewById(R.id.imageView_detail);
        Picasso.get().load(mRecipe.getImageLink()).placeholder(R.drawable.ic_placeholder_background)
                .fit().centerCrop().into(iv_detail);
        TextView tv_title = view.findViewById(R.id.textView_detailTitle);
        tv_title.setText(mRecipe.getTitle());

        ChipGroup chipGroup = view.findViewById(R.id.chipGroup);
        ArrayList<ListItem> ingredients = mRecipe.getIngredients();
        for (ListItem item : ingredients) {
            Chip chip = new Chip(mContext);
            chip.setText(item.getName());
            chip.setCheckable(true);
            chipGroup.addView(chip);
        }

        TextView tv_instruction = view.findViewById(R.id.textView_instructionText);
        tv_instruction.setText(mRecipe.getInstruction());

        RecyclerView rv = view.findViewById(R.id.recyclerView_details);
        GridLayoutManager manager = new GridLayoutManager(mContext, 1);
        rv.setLayoutManager(manager);
        ListAdapter adapter = new ListAdapter(mContext, mRecipe.getNutrition());
        rv.setAdapter(adapter);
    }

    private void unRegister() {
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mReceiver);
    }

    class UpdateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(BROADCAST_ACTION)) {
                mRecipe = (Recipe) intent.getSerializableExtra("ExtraRecipe");
                unRegister();
                showRecipe(requireView());
            }
        }
    }
}
