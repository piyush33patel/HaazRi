package com.example.navigationhaazrai;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;

public class Updates extends AppCompatActivity {
    Button bt;
    DownloadManager downloadManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updates);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.updates_id);

        bt = (Button)findViewById(R.id.download);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(Updates.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                {
                    String version = "HaazRi(new version)";
                    downloadManager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
                    Uri uri = Uri.parse(Constants.URL_UPDATES);
                    DownloadManager.Request request = new DownloadManager.Request(uri);
                    request.setTitle(version);
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "HaazRi.apk");
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                    downloadManager.enqueue(request);
                    Snackbar.make(constraintLayout,"Downloading....",Snackbar.LENGTH_SHORT).show();

                    BroadcastReceiver onComplete=new BroadcastReceiver() {
                        public void onReceive(Context c, Intent i) {
                            String path = Environment.DIRECTORY_DOWNLOADS + "HaazRi.apk";
                            //hep
                            File toInstall = new File(Environment.DIRECTORY_DOWNLOADS, "HaazRi.apk");
                            Intent intent;
                            Uri apkUri = FileProvider.getUriForFile(Updates.this, BuildConfig.APPLICATION_ID +".fileprovider",toInstall);
                            intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
                            intent.setData(apkUri);
                            intent.setFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                            Updates.this.startActivity(intent);
                            //hep
                            Snackbar.make(constraintLayout,""+path,Snackbar.LENGTH_LONG).show();

                        }
                    };
                    registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                }
                else
                {
                    ActivityCompat.requestPermissions(Updates.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1 );
                }
            }
        });
    }
}


