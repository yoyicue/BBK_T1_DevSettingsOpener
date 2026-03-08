package com.tool.devsettings;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Gravity;
import android.util.TypedValue;
import android.graphics.Color;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ScrollView scroll = new ScrollView(this);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(48, 64, 48, 64);

        TextView title = new TextView(this);
        title.setText("BBK 开发者工具 Lite");
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        title.setGravity(Gravity.CENTER);
        layout.addView(title);

        TextView subtitle = new TextView(this);
        subtitle.setText("适用于步步高学习平板");
        subtitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        subtitle.setGravity(Gravity.CENTER);
        subtitle.setTextColor(Color.GRAY);
        subtitle.setPadding(0, 8, 0, 24);
        layout.addView(subtitle);

        // 第一步：启用开发者选项
        addSection(layout, "第一步：启用开发者选项");
        addButton(layout, "打开 设备信息（点版本号7次）", v ->
            launch("com.android.settings",
                   "com.android.settings.Settings$MyDeviceInfoActivity"));

        // 第二步：打开开发者选项
        addSection(layout, "第二步：开启 USB 调试");
        addButton(layout, "打开 开发者选项", v -> {
            try {
                startActivity(new Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS));
            } catch (Exception e) {
                toast("无法打开，请先完成第一步");
            }
        });

        // 其他
        addSection(layout, "其他");
        addButton(layout, "系统设置", v ->
            launch("com.android.settings",
                   "com.android.settings.Settings"));

        scroll.addView(layout);
        setContentView(scroll);
    }

    private void addSection(LinearLayout layout, String text) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        tv.setTextColor(Color.DKGRAY);
        tv.setPadding(0, 32, 0, 8);
        layout.addView(tv);
    }

    private void addButton(LinearLayout layout, String text, android.view.View.OnClickListener listener) {
        Button btn = new Button(this);
        btn.setText(text);
        btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        btn.setAllCaps(false);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 8, 0, 0);
        btn.setLayoutParams(params);
        btn.setOnClickListener(listener);
        layout.addView(btn);
    }

    private void launch(String pkg, String cls) {
        try {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(pkg, cls));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
            toast("无法打开: " + cls.substring(cls.lastIndexOf('.') + 1));
        }
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}
