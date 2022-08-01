package com.androidcourse.neworder;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ViewDialog extends Dialog{

    private Dialog dialog;
    private Context mContext;
    public ViewDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }



    public void showDialog(String msg,String Title){
        dialog = new Dialog(this.mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_other);
        TextView text = dialog.findViewById(R.id.instructions);
        TextView title = dialog.findViewById(R.id.title_dialog);
        title.setText(Title);
        text.setText(msg);
        dialog.show();

    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
