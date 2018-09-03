package com.skyle.huffman;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class EncodeFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener{

    public static final String ENCODING_SAVE = "0";
    public static final String CHARACTERS_SAVE = "1";
    public static final String RATIO_SAVE = "2";

    private Huffman huffman;

    public EncodeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle save) {
        super.onCreate(save);
        if (save != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle save) {
        View view = inflater.inflate(R.layout.fragment_encode, container, false);

        view.findViewById(R.id.copy_button).setOnClickListener(this);
        view.findViewById(R.id.encode_button).setOnClickListener(this);

        Spinner spinner = view.findViewById(R.id.encode_dictionary_spinner);

        List<String> spinner_values = new ArrayList<>();
        spinner_values.add("Default");
        spinner_values.add("Emily");
        spinner_values.add("Amelia");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, spinner_values);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle save) {
        super.onSaveInstanceState(save);
        try {
            save.putStringArray(EncodeFragment.ENCODING_SAVE, huffman.encoding());
            save.putInt(EncodeFragment.CHARACTERS_SAVE, huffman.getCharacters());
            save.putDouble(EncodeFragment.RATIO_SAVE, huffman.compressionRatio());
        } catch (Exception e) {}

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.copy_button:
                Log.v("Button Click", "Copy Button, Encode Fragment");
                TextView encoded = getView().findViewById(R.id.encoded);
                if (encoded != null && encoded.getText().length() > 0) {
                    ClipboardManager clipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Huffman Encoding", encoded.getText());
                    clipboardManager.setPrimaryClip(clip);
                    Toast.makeText(getActivity(), "Copied to Clipboard", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Nothing to copy", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.encode_button:
                Log.v("Button Click", "Encode Button, Encode Fragment");
                EditText edit = getView().findViewById(R.id.editText);
                if (edit != null && edit.getText().length() > 0) {
                    huffman = new Huffman(edit.getText().toString());
                    String e = huffman.compress(edit.getText().toString());
                    TextView text = getView().findViewById(R.id.encoded);
                    text.setText(e);
                    clearTable();
                    loadTable(huffman.encoding());
                    setStats(huffman);
                } else {
                    Toast.makeText(getActivity(), "Nothing to encode", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void setStats(Huffman c) {
        TextView total = getView().findViewById(R.id.total_characters);
        total.setText(String.valueOf(c.getCharacters()));
        TextView ratio = getView().findViewById(R.id.compression_ratio);
        String r = String.valueOf(c.compressionRatio());
        ratio.setText(r.substring(0, (r.length() > 4 ? 4 : r.length())));
    }

    private void clearTable() {
        TableLayout table = getView().findViewById(R.id.encoding_table);
        table.removeAllViews();
    }

    private void loadTable(String[] encoding) {
        TableLayout table = getView().findViewById(R.id.encoding_table);
        table.addView(getRow("Character", "Encoding", Typeface.BOLD));
        table.addView(getLine());
        for (String s : encoding) {
            String l = s.substring(0, s.indexOf("$"));
            String c = s.substring(s.indexOf("$")+1, s.length());
            table.addView(getRow(l, c, Typeface.NORMAL));
            table.addView(getLine());
        }
    }

    private TableRow getLine() {
        TableRow row = new TableRow(getContext());

        TextView a = new TextView(getContext());
        a.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        a.setHeight(5);
        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.span = 2;
        a.setLayoutParams(params);
        row.addView(a);
        return row;
    }

    private TableRow getRow(String a, String b, int typeface) {
        TableRow row = new TableRow(getContext());
        TextView letter = new TextView(getContext());
        letter.setText(a);
        letter.setTypeface(null, typeface);
        letter.setGravity(Gravity.CENTER_HORIZONTAL);
        TextView code = new TextView(getContext());
        code.setText(b);
        code.setTypeface(null, typeface);
        code.setGravity(Gravity.CENTER_HORIZONTAL);
        row.addView(letter);
        row.addView(code);
        return row;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getActivity(), "pos="+position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(getActivity(), "nothing selected", Toast.LENGTH_SHORT).show();
    }
}
