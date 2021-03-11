package com.mishsapp.usingfetchframework;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileObserver;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;


import com.tonyodev.fetch2.Download;
import com.tonyodev.fetch2.Error;
import com.tonyodev.fetch2.Fetch;
import com.tonyodev.fetch2.FetchConfiguration;
import com.tonyodev.fetch2.FetchListener;
import com.tonyodev.fetch2.NetworkType;
import com.tonyodev.fetch2.Request;
import com.tonyodev.fetch2core.DownloadBlock;
import com.tonyodev.fetch2core.Func;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity  implements FetchListener {

    private Fetch fetch;
    private Button buttonDown;
    Notification.Builder builder;
    NotificationManagerCompat notificationManagerCompat;

    String urls[] = new String[]{
            "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d7/Android_robot.svg/800px-Android_robot.svg.png",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d7/Android_robot.svg/800px-Android_robot.svg.png",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d7/Android_robot.svg/800px-Android_robot.svg.png",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d7/Android_robot.svg/800px-Android_robot.svg.png",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d7/Android_robot.svg/800px-Android_robot.svg.png",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d7/Android_robot.svg/800px-Android_robot.svg.png",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d7/Android_robot.svg/800px-Android_robot.svg.png",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d7/Android_robot.svg/800px-Android_robot.svg.png",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d7/Android_robot.svg/800px-Android_robot.svg.png",
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonDown = findViewById(R.id.buttonDown);

        FetchConfiguration fetchConfiguration = new FetchConfiguration.Builder(this)
                .setDownloadConcurrentLimit(3)
                .build();

        fetch = Fetch.Impl.getInstance(fetchConfiguration);
        Log.e("Dizi Uzunlugu = ", String.valueOf(urls.length));



    }

    public void downloadButton(View view) {
        linkDownload(view);
    }

    public void linkDownload(View v){
        for (int i = 0; i< urls.length; i++){

            File folder = new File("  *************** "); //path yolunuz
            if(!(folder.exists())){  folder.mkdir(); }

            String tempUrl = urls[i];
            final Request request = new Request(tempUrl, folder+"/image.png"+(i+1)+"--");
            request.setNetworkType(NetworkType.ALL);
            request.addHeader(String.valueOf(i), String.valueOf(i+1));

            fetch.enqueue(request, new Func<Request>() {
                @Override
                public void call(@NotNull Request result) {

                    // Toast.makeText(getApplicationContext(),"CALL CALISIYOR ", Toast.LENGTH_SHORT).show();
                    Log.e("KONTROL" , "Call Calisiyor...");

                }
            }, new Func<Error>() {

                @Override
                public void call(@NotNull Error result) {

                   // Toast.makeText(getApplicationContext(),"CALL ERROR ", Toast.LENGTH_SHORT).show();
                    Log.e("KONTROL" , "Call Error...");

                }
            });

            fetch.addListener(MainActivity.this);
        }
    }

    @Override
    public void onAdded(@NotNull Download download) {
        showNotification(download.getId());
    }

    private void showNotification(int id) {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){

            NotificationChannel channel = new NotificationChannel("channel1id", "001", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("");

            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);

            builder = new Notification.Builder(getApplicationContext(),"channel1id");
            builder.setContentTitle("YÜKLENİYOR");
            builder.setSmallIcon(R.drawable.image);
            builder.setProgress(100,0,false);
            builder.setAutoCancel(false);
            builder.setWhen(System.currentTimeMillis());
            builder.setPriority(Notification.PRIORITY_DEFAULT);

            notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
            notificationManagerCompat.notify(id,builder.build());

        } else {

            builder = new Notification.Builder(getApplicationContext());
            builder.setContentTitle("yuklenmekte");
            builder.setProgress(100,0,false);
            builder.setAutoCancel(false);
            builder.setWhen(System.currentTimeMillis());
            builder.setPriority(Notification.PRIORITY_DEFAULT);

            notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
            notificationManagerCompat.notify(id,builder.build());

        }
    }

    @Override
    public void onCancelled(@NotNull Download download) {

    }

    @Override
    public void onCompleted(@NotNull Download download) {

        Toast.makeText(getApplicationContext(), "İndirme Başarılı", Toast.LENGTH_SHORT).show();

        if(builder != null){
            builder.setContentTitle("İndirme bitti");
            builder.setProgress(0,0,false);
            notificationManagerCompat.notify(download.getId(), builder.build());
        }

    }

    @Override
    public void onDeleted(@NotNull Download download) {

    }

    @Override
    public void onDownloadBlockUpdated(@NotNull Download download, @NotNull DownloadBlock downloadBlock, int i) {

    }

    @Override
    public void onError(@NotNull Download download, @NotNull Error error, @org.jetbrains.annotations.Nullable Throwable throwable) {

    }

    @Override
    public void onPaused(@NotNull Download download) {

    }
    @Override
    public void onProgress(@NotNull Download download, long l, long l1) {

        int progress = download.getProgress();
        builder.setProgress(100, progress,false);
        notificationManagerCompat.notify(download.getId(), builder.build());

    }

    @Override
    public void onQueued(@NotNull Download download, boolean b) {

    }

    @Override
    public void onRemoved(@NotNull Download download) {

    }

    @Override
    public void onResumed(@NotNull Download download) {

    }

    @Override
    public void onStarted(@NotNull Download download, @NotNull List<? extends DownloadBlock> list, int i) {

    }

    @Override
    public void onWaitingNetwork(@NotNull Download download) {


    }

}