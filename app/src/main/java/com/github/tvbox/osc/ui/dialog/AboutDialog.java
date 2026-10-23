package com.github.tvbox.osc.ui.dialog;

import android.content.Context;
import android.graphics.Color; // 【新增导入】
import android.graphics.drawable.ColorDrawable; // 【新增导入】

import androidx.annotation.NonNull;

import com.github.tvbox.osc.R;

import org.jetbrains.annotations.NotNull;

public class AboutDialog extends BaseDialog {

    public AboutDialog(@NonNull @NotNull Context context) {
        super(context);
        setContentView(R.layout.dialog_about);
        
        // 👇【新增】强制设置系统窗口背景为深色，防止底层界面透出
        if (getWindow() != null) {
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F21A1A20")));
        }
    }
}
