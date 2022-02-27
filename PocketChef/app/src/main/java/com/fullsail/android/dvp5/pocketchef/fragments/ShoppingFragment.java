package com.fullsail.android.dvp5.pocketchef.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fullsail.android.dvp5.pocketchef.ListAdapter;
import com.fullsail.android.dvp5.pocketchef.ListItem;
import com.fullsail.android.dvp5.pocketchef.R;
import com.fullsail.android.dvp5.pocketchef.utilities.FirebaseHelper;

import java.util.ArrayList;

public class ShoppingFragment extends Fragment {
    public static final String BROADCAST_ACTION = "com.fullsail.android.dvp5.pocketchef.BROADCAST_ACTION_LIST";
    private Context mContext;
    private BroadcastReceiver mReceiver;

    public ShoppingFragment() { super(R.layout.fragment_shopping); }

    public static ShoppingFragment newInstance() {
        return new ShoppingFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = requireContext();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BROADCAST_ACTION);
        mReceiver = new UpdateReceiver();
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mReceiver, intentFilter);

        FirebaseHelper.readShoppingList(mContext);
    }

    private void showRecyclerViewList(@NonNull View view, ArrayList<ListItem> list) {
        RecyclerView rv = view.findViewById(R.id.recycleView_shopping);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        rv.setLayoutManager(manager);
        ListAdapter adapter = new ListAdapter(mContext, list);
        rv.setAdapter(adapter);
    }

    private void unRegister() {
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mReceiver);
    }

    class UpdateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(BROADCAST_ACTION)) {
                ArrayList<ListItem> list = (ArrayList<ListItem>) intent.getSerializableExtra("ExtraList");
                unRegister();
                showRecyclerViewList(requireView(), list);
            }
        }
    }
}
