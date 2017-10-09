package com.projects.cdharini.tinytweet.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.projects.cdharini.tinytweet.utils.TinyTweetConstants;

/**
 * Created by dharinic on 10/8/17.
 */

public class ComposeAlertDialogFragment extends DialogFragment {

    public ComposeAlertDialogFragment() {
        // Empty constructor required for DialogFragment
    }

    public static ComposeAlertDialogFragment newInstance(String title, String savedTweet) {
        ComposeAlertDialogFragment frag = new ComposeAlertDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString(TinyTweetConstants.SAVED_TWEET, savedTweet);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString("title");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage("Do you want to save your tweet for later?");
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // on success
                //save to shared prefs
                SharedPreferences pref =
                        PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor edit = pref.edit();
                edit.putString(TinyTweetConstants.SAVED_TWEET, getArguments().getString(TinyTweetConstants.SAVED_TWEET));
                edit.commit();
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null/* && dialog.isShowing()*/) {
                    SharedPreferences pref =
                            PreferenceManager.getDefaultSharedPreferences(getActivity());
                    SharedPreferences.Editor edit = pref.edit();
                    edit.putString(TinyTweetConstants.SAVED_TWEET, "");
                    edit.commit();
                    dialog.dismiss();
                }
            }

        });

        return alertDialogBuilder.create();
    }
}

