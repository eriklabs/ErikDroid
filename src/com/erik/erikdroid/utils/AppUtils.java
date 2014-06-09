package com.erik.erikdroid.utils;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.erik.erikdroid.R;

public class AppUtils {
    private static final String PASS_FILE = "PASS_FILE";

    public static String userPassword = "";

    public static void loadPassword(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PASS_FILE, Context.MODE_PRIVATE);
        userPassword = prefs.getString("pass", "");
    }

    public static void savePassword(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PASS_FILE, Context.MODE_PRIVATE);
        prefs.edit().putString("pass", userPassword).apply();
    }

    public static boolean notNullOrEmpty(String string) {
        if (string != null && !string.isEmpty()) {
            return true;
        }
        return false;
    }

    @SuppressLint("DefaultLocale")
    public static String convertToHumanReadable(String string) {
        if (string != null && !string.isEmpty()) {
            string = string.replace("_", " ");
            return string.substring(0, 1).toUpperCase() + string.substring(1);
        }
        return "";
    }

    public static void showAlert(Context ctx, final int errorResource,
            OnClickListener dialogButtonListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);

        builder.setMessage(errorResource);
        builder.setTitle(R.string.app_name);
        builder.setPositiveButton(R.string.dialogOk, dialogButtonListener);

        builder.create().show();
    }

    public static void showAlert(Context ctx, final String errorResource,
            OnClickListener dialogButtonListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);

        builder.setMessage(errorResource);
        builder.setTitle(R.string.app_name);
        builder.setPositiveButton(R.string.dialogOk, dialogButtonListener);

        builder.create().show();
    }

    public static void showToast(Context ctx, final String text) {
        Toast.makeText(ctx, text, Toast.LENGTH_SHORT).show();
    }
}
