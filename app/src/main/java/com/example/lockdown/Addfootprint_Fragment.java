package com.example.lockdown;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lockdown.database.AppDatabase;
import com.example.lockdown.database.DataInitializer;
/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Addfootprint_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Addfootprint_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Addfootprint_Fragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ADD_DEFAULT = "WTF?!";
    Context context;
    // TODO: Rename and change types of parameters
    private String ADDRESS;

    private OnFragmentInteractionListener mListener;


    private AppDatabase db;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param ADDRESS ADD_DEFAULT.
     * @return A new instance of fragment Addfootprint_Fragment.
     */
    public static Addfootprint_Fragment newInstance(String add) {
        Addfootprint_Fragment fragment = new Addfootprint_Fragment();
        Bundle args = new Bundle();
        args.putString(ADD_DEFAULT, add);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getContext();
        if (getArguments() != null) {
            ADDRESS = getArguments().getString(ADD_DEFAULT);
        }

        db = AppDatabase.getInstance(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music, container, false);
        TextView Address= view.findViewById(R.id.Address);
        Address.setText(ADDRESS);
        final Button button_edit = view.findViewById(R.id.button_add);
        button_edit.setOnClickListener(this);
        //
        final Button buttonShare = view.findViewById(R.id.button_share);
        buttonShare.setOnClickListener(this);

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_add:
                WindowManager mWindowManager  = (WindowManager) this.getContext().getSystemService(Context.WINDOW_SERVICE);
                DisplayMetrics metrics = new DisplayMetrics();
                mWindowManager.getDefaultDisplay().getMetrics(metrics);
                int width = metrics.widthPixels;
                int height = metrics.heightPixels;
                final EditText addEdit = new EditText(context);
                addEdit.setHint("Give me a name");
                addEdit.setGravity(Gravity.CENTER);
                addEdit.setHeight(height/15);
                addEdit.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                AlertDialog.Builder builderAdd = new AlertDialog.Builder(context);
                builderAdd.setIcon(R.mipmap.ic_launcher) // TODO CHANGE ICON
                        .setTitle("Add FootPrint")
                        .setView(addEdit)
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(context, "You cancelled your FootPrint. Q.Q", Toast.LENGTH_SHORT);
                            }

                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO: jump to next activity

                                DataInitializer.addFootprint(db, addEdit.getText().toString());
                                Toast.makeText(context, "FootPrint NO." + db.fpModel().getMaxId() + " " + addEdit.getText().toString() + " is added", Toast.LENGTH_LONG).show();
                            }

                        });
                final AlertDialog dialog = builderAdd.create();
                builderAdd.show();
//                Button btnPositive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
//                Button btnNegative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
//                LinearLayout.LayoutParams layoutparams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
//                layoutparams.height = 40;
//                btnPositive.setLayoutParams(layoutparams);
//                btnNegative.setLayoutParams(layoutparams);
                break;
            case R.id.button_share:
                AlertDialog.Builder builderShare = new AlertDialog.Builder(context);
                builderShare.setTitle("You wanna share by:")
                        .setIcon(R.mipmap.ic_launcher) //TODO change ICON
                        .setItems(R.array.dialog_arrays, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String[] items = getResources().getStringArray(R.array.dialog_arrays);
                                Toast.makeText(context, "You selected: " + which
                                                                + " , " + items[which],
                                                        Toast.LENGTH_LONG).show();
                                }});
                builderShare.show();
                break;
            case R.id.button_save:
                break;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
