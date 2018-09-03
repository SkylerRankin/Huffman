package com.skyle.huffman;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

public class DictionaryFragment extends Fragment implements View.OnClickListener {

    public DictionaryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle save) {
        super.onCreate(save);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle save) {
        FrameLayout frameLayout = (FrameLayout) inflater.inflate(R.layout.fragment_dictionary, container, false);
        RecyclerView dictionary_recycler = frameLayout.findViewById(R.id.dictionary_recycler);
        String[] d = new String[20];
        for (int i=0; i<20; ++i) {
            d[i] = "--"+i+"--";
        }
        DescriptionAdapter adapter = new DescriptionAdapter(d);
        adapter.setListener(new DictionaryListener());
        dictionary_recycler.setAdapter(adapter);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        dictionary_recycler.setLayoutManager(layoutManager);
        frameLayout.findViewById(R.id.add_action_button).setOnClickListener(this);
        return frameLayout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_action_button:
                Intent i = new Intent(getActivity(), NewDictionaryActivity.class);
                getActivity().startActivity(i);
                break;
        }
    }

    public class DictionaryListener implements DescriptionAdapter.Listener {

        @Override
        public void onClick(int p) {
            Toast.makeText(getActivity(), "DictList: "+p, Toast.LENGTH_SHORT).show();
        }
    }
}
