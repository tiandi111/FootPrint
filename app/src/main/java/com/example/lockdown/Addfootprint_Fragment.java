package com.example.lockdown;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

    // TODO: Rename and change types of parameters
    private String ADDRESS;

    private OnFragmentInteractionListener mListener;


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
        if (getArguments() != null) {
            ADDRESS = getArguments().getString(ADD_DEFAULT);
        }
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
        Context context = this.getContext();
        view.findViewById(R.id.button_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new AlertDialog.Builder(context)
                        .setTitle("You wanna share by:")
                        .setItems(R.array.dialog_arrays,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        String[] items = getResources()
                                                .getStringArray(
                                                        R.array.dialog_arrays);
                                        Toast.makeText(
                                                context,
                                                "You selected: " + which
                                                        + " , " + items[which],
                                                Toast.LENGTH_LONG).show();
                                    }
                                }).create().show();
            }
        });


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
                Intent intent = new Intent(getActivity(), Yourfootprint_Activity.class);
                startActivity(intent);
                break;
            case R.id.button_share:
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
