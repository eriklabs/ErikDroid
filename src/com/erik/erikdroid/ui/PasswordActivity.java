package com.erik.erikdroid.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.erik.erikdroid.R;
import com.erik.erikdroid.utils.AppUtils;

public class PasswordActivity extends ActionBarActivity {

    TextView infoMessageTV;
    EditText oldPassET, newPassET;
    Button okButton, secondaryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_pass_fragment);
        initActionBar();
        initViews();

        AppUtils.loadPassword(this.getApplicationContext());

        if (AppUtils.notNullOrEmpty(AppUtils.userPassword)) {
            enableVerifyPassword();
        } else {
            enableCreatePassword();
        }
    }

    public void initActionBar() {
        getSupportActionBar().show();
        getSupportActionBar().setTitle(R.string.securityCenter);
    }

    public void initViews() {
        infoMessageTV = (TextView) findViewById(R.id.password_info_text);
        oldPassET = (EditText) findViewById(R.id.old_password);
        newPassET = (EditText) findViewById(R.id.new_password);
        okButton = (Button) findViewById(R.id.password_ok_button);
        secondaryButton = (Button) findViewById(R.id.password_second_button);
    }

    private void enableVerifyPassword() {
        infoMessageTV.setText(R.string.enterPasswordNote);
        okButton.setText(R.string.login);
        secondaryButton.setText(R.string.changePassword);
        secondaryButton.setVisibility(View.VISIBLE);
        oldPassET.setVisibility(View.GONE);
        newPassET.requestFocus();

        oldPassET.setText("");
        newPassET.setText("");
        newPassET.setHint(R.string.password);

        okButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newPassET.getText().toString().isEmpty()) {
                    AppUtils.showToast(PasswordActivity.this.getApplicationContext(), getString(R.string.emptyPasswordWarning));
                    return;
                }
                if (!newPassET.getText().toString().equals(AppUtils.userPassword)) {
                    AppUtils.showToast(PasswordActivity.this.getApplicationContext(), getString(R.string.wrongPasswordWarning));
                    return;
                }

                Toast.makeText(PasswordActivity.this, R.string.loginSuccessful, Toast.LENGTH_SHORT).show();
                openErikList();
            }
        });

        secondaryButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                enableChangePassword();
            }
        });
    }

    private void enableCreatePassword() {
        infoMessageTV.setText(R.string.createPasswordNote);
        okButton.setText(R.string.dialogOk);
        secondaryButton.setText(R.string.clean);
        oldPassET.setVisibility(View.GONE);
        secondaryButton.setVisibility(View.VISIBLE);
        newPassET.requestFocus();

        oldPassET.setText("");
        newPassET.setText("");

        newPassET.setHint(R.string.new_password);

        okButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newPassET.getText().toString().isEmpty()) {
                    AppUtils.showToast(PasswordActivity.this.getApplicationContext(), getString(R.string.emptyPasswordWarning));
                    return;
                }
                if (newPassET.getText().toString().length() < 4) {
                    AppUtils.showToast(PasswordActivity.this.getApplicationContext(), getString(R.string.shortPasswordWarning));
                    return;
                }

                if (newPassET.getText().toString().length() > 10) {
                    AppUtils.showToast(PasswordActivity.this.getApplicationContext(), getString(R.string.longPasswordWarning));
                    return;
                }

                AppUtils.userPassword = newPassET.getText().toString();
                AppUtils.savePassword(PasswordActivity.this.getApplicationContext());

                Toast.makeText(PasswordActivity.this, R.string.passwordSuccessfullyCreated, Toast.LENGTH_SHORT).show();
                openErikList();
            }
        });

        secondaryButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                newPassET.setText("");
            }
        });
    }

    private void enableChangePassword() {
        infoMessageTV.setText(R.string.createPasswordNote);
        okButton.setText(R.string.dialogOk);
        secondaryButton.setVisibility(View.GONE);
        oldPassET.setVisibility(View.VISIBLE);
        oldPassET.requestFocus();

        oldPassET.setText("");
        newPassET.setText("");
        newPassET.setHint(R.string.new_password);

        okButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!oldPassET.getText().toString().equals(AppUtils.userPassword)) {
                    AppUtils.showToast(PasswordActivity.this.getApplicationContext(), getString(R.string.wrongOldPasswordWarning));
                    return;
                }

                if (newPassET.getText().toString().isEmpty()) {
                    AppUtils.showToast(PasswordActivity.this.getApplicationContext(), getString(R.string.emptyNewPasswordWarning));
                    return;
                }
                if (newPassET.getText().toString().length() < 4) {
                    AppUtils.showToast(PasswordActivity.this.getApplicationContext(), getString(R.string.shortNewPasswordWarning));
                    return;
                }

                if (newPassET.getText().toString().length() > 10) {
                    AppUtils.showToast(PasswordActivity.this.getApplicationContext(), getString(R.string.longNewPasswordWarning));
                    return;
                }

                AppUtils.userPassword = newPassET.getText().toString();
                AppUtils.savePassword(PasswordActivity.this.getApplicationContext());

                Toast.makeText(PasswordActivity.this, R.string.passwordChangeSuccessful, Toast.LENGTH_SHORT).show();
                openErikList();
            }
        });
    }

    private void openErikList() {
        startActivity(new Intent(this, ErikListActivity.class));
    }

    @Override
    public void onBackPressed() {
        if (oldPassET.getVisibility() == View.VISIBLE) {
            enableVerifyPassword();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_about) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.aboutDialogMessage).setTitle(R.string.app_name);
            builder.setPositiveButton(R.string.dialogOk, null);
            builder.setIcon(R.drawable.ic_launcher);

            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
