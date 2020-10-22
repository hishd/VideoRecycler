package com.hishd.videorecycler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Size;
import android.view.View;
import android.widget.Toast;

import com.hishd.videorecycler.Adapters.VideoItemAdapter;
import com.hishd.videorecycler.Model.VideoItem;
import com.hishd.videorecycler.Util.SizeToDigits;
import com.hishd.videorecycler.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements VideoItemAdapter.OnVideoItemClickedListener {

    private ActivityMainBinding binding;

    private final int REQ_CODE = 101;
    private final String STORAGE_PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final String selection=MediaStore.Video.Media.DATA +" like ? ";
    private final String[] selectionArgs = new String[]{"%Download/FBDownloader%"};

    private Cursor videoCursor;
    long videoID;
    String videoName;
    long videoSize;

    private VideoItemAdapter adapter;
    private List<VideoItem> videoItemList;

    private final String[] projection = {MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.SIZE };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        videoItemList = new ArrayList<>();
        adapter = new VideoItemAdapter(this,videoItemList,this);
        binding.rclrVideos.setLayoutManager(new LinearLayoutManager(this));
        binding.rclrVideos.setAdapter(adapter);


        if(checkAndRequestForPermission(STORAGE_PERMISSION,REQ_CODE))
            fetchVideoInfo();

    }

    private void fetchVideoInfo() {
        System.gc();
        if(videoCursor == null)
            videoCursor = getApplicationContext().getContentResolver().query(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    projection,
                    selection,
                    selectionArgs,
                    MediaStore.Video.Media.DISPLAY_NAME + " ASC"
            );

        if(videoCursor!=null) {
            int nameColumn = videoCursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
            int sizeColumn = videoCursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE);
            int pathIndex = videoCursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            int idColumn = videoCursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);

            if(videoCursor.getCount() == 0) {
                Toast.makeText(MainActivity.this, "No videos found", Toast.LENGTH_LONG).show();
                return;
            }

            while (videoCursor.moveToNext()) {
                //Populate your data from here
                videoID = videoCursor.getLong(idColumn);
                videoName = videoCursor.getString(nameColumn);
                videoSize = videoCursor.getInt(sizeColumn);
                Log.d("DIRECTORY", Environment.DIRECTORY_DOWNLOADS);
                videoItemList.add(new VideoItem(videoName, SizeToDigits.getFileSize(videoSize), videoCursor.getString(pathIndex)));
            }

            adapter.notifyDataSetChanged();
        }
    }

    private boolean checkAndRequestForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }

        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
            return false;
        } else if (ContextCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_DENIED) {
            Toast.makeText(getApplicationContext(), "Permission was denied", Toast.LENGTH_SHORT).show();
            return false;
        }

        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == 101){
                fetchVideoInfo();
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onVideoClicked(int position) {
        startActivity(new Intent(Intent.ACTION_VIEW).setDataAndType(Uri.parse(videoItemList.get(position).getImagePath()), "video/mp4"));
    }
}