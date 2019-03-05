package com.example.krishbhatia.instagramclone.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.krishbhatia.instagramclone.R;

public class ConfirmPwdDialog extends DialogFragment {
   private EditText pwdField;

    private static final String TAG = "ConfirmPwdDialog";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.dialog_confirm_pwd,container,false);
        Log.d(TAG, "onCreateView: dialog is started");
        pwdField=view.findViewById(R.id.confirm_pwd);
        TextView cancel=view.findViewById(R.id.dialogCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().cancel();
            }
        });
        TextView confirm=view.findViewById(R.id.dialogConfirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: confirming captured password");
                String password=pwdField.getText().toString();
                if(!password.equals("")) {
                    mOnConfirmPassword.onConfirmpwd(password);
                    getDialog().dismiss();

                }
                else {
                    Toast.makeText(getActivity(), "All the fields must be filled", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
    public interface OnConfirmPassword{
       public void onConfirmpwd(String password);

    }
    OnConfirmPassword mOnConfirmPassword;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnConfirmPassword= (OnConfirmPassword) getTargetFragment();
        }
        catch (ClassCastException e){

        }
    }
}
