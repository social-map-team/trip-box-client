package com.socialmap.yy.travelbox.module.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.socialmap.yy.travelbox.R;
import com.socialmap.yy.travelbox.utils.TbsClient;

import java.io.UnsupportedEncodingException;
import java.util.Timer;
import java.util.TimerTask;

import static com.socialmap.yy.travelbox.utils.TbsClient.getInstance;

/**
 * Created by gxyzw_000 on 2015/3/7.
 */
public class SOSCDialogFragment extends DialogFragment {
    SOSFragmentCallBack sosfragmentCallBack = null;



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View v = inflater.inflate(R.layout.dialog_sosc, null);
        EditText message = (EditText) v.findViewById(R.id.message);
        message.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                count = 0;
            }
        });

        //TODO first use EditText, now I use OnClickListener to detect it.
        message.setOnClickListener(new View.OnClickListener() {
            boolean firstChange = true;

            @Override
            public void onClick(View v) {
                if (firstChange) {
                    maxCount = 300;
                    count = 0;
                    firstChange = false;
                }
            }
        });

          String sosmessage = message.getText().toString();

        builder.setView(v)
                // Add action buttons
                .setPositiveButton(R.string.sos_confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        timer.cancel();
                        //TODO send sos confirm
                        sosfragmentCallBack.callbackFun1(null);
                        getInstance()
                                .request("/api/sos/confirm", "post"

                                )
                                .execute(new TbsClient.Callback() {
                                    @Override
                                    public void onFinished(TbsClient.ServerResponse response) {
                                        try {
                                            String content = new String(response.getContent(), "UTF-8");
                                            Log.i("yy", response.getStatusCode() + "\n" + content);

                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });

                    }
                })
                .setNegativeButton(R.string.sos_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        timer.cancel();


                        SOSCDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }




    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what >= 0) {
                Button send = ((AlertDialog) SOSCDialogFragment.this.getDialog()).getButton(DialogInterface.BUTTON_NEGATIVE);
                send.setText(getString(R.string.sos_cancel) + "(" + msg.what + ")");
            }
        }
    };
    private Timer timer = new Timer();
    private int maxCount = 5;
    private int count = 0;
    private  int UPDATE_BUTTON_TEXT = 1;
    private  String sosmessage;

    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            if (count <= maxCount) {
                handler.sendEmptyMessage(maxCount - count);
                count++;
            } else {
                timer.cancel();
                //TODO auto send sos
                getInstance()
                        .request("/api/sos/", "post",
                                "message",sosmessage
                                )
                        .execute(new TbsClient.Callback() {
                            @Override
                            public void onFinished(TbsClient.ServerResponse response) {
                                try {
                                    String content = new String(response.getContent(), "UTF-8");
                                    Log.i("yy", response.getStatusCode() + "\n" + content);

                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                //close dialog
                SOSCDialogFragment.this.getDialog().cancel();
            }
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        timer.schedule(timerTask, 0, 1000);


    }


    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);
        sosfragmentCallBack = (MainActivity)activity;
    }





}
