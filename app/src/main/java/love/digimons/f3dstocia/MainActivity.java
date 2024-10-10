package love.digimons.f3dstocia;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.documentfile.provider.DocumentFile;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.util.zip.DataFormatException;

import love.digimons.f3dstocia.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_PICK_FILE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        if (Build.VERSION.SDK_INT >= 33) {
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(
                        Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + this.getPackageName()));
                this.startActivity(intent);
            }
        }
        if (hasStoragePermissions(this)) {
            requestStoragePermissions(this);
        }
        Button buttonSelectFile = findViewById(R.id.buttonSelectFile);
        buttonSelectFile.setOnClickListener(v -> {
            selectFile();
        });
    }

    private void selectFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, REQUEST_CODE_PICK_FILE);
    }

    public static String getFileRealNameFromUri(Context context, Uri fileUri) {
        if (context == null || fileUri == null) return null;
        DocumentFile documentFile = DocumentFile.fromSingleUri(context, fileUri);
        if (documentFile == null) return null;
        return documentFile.getName();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_FILE && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                TextView textViewResult = findViewById(R.id.textViewResult);
                String filename = getFileRealNameFromUri(this, uri).replace(".3ds", "").replace(".cci","");
                try {
                    InputStream inputStream = null;
                    try {
                        inputStream = getContentResolver().openInputStream(uri);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    File tempFile = new File(getCacheDir(), "tempfile");
                    try (OutputStream outputStream = Files.newOutputStream(tempFile.toPath())) {
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = inputStream.read(buffer)) > 0) {
                            outputStream.write(buffer, 0, length);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    File3dsTocia.toCia(uri, new File(getExternalFilesDir(null), filename+".cia"), textViewResult, filename, tempFile, this);
                    if (tempFile.exists()) {
                        tempFile.delete();
                    }
                } catch (IOException | CloneNotSupportedException | DataFormatException |
                         NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    private boolean hasStoragePermissions(Context context) {
        if(android.os.Build.VERSION.SDK_INT < 33){
            int readPermission = ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE);
            int writePermission = ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            return readPermission == PackageManager.PERMISSION_GRANTED && writePermission == PackageManager.PERMISSION_GRANTED;
        }else{
            int audioPermission = ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_MEDIA_AUDIO);
            int imagePermission = ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_MEDIA_IMAGES);
            int videoPermission = ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_MEDIA_VIDEO);
            return audioPermission == PackageManager.PERMISSION_GRANTED && imagePermission == PackageManager.PERMISSION_GRANTED && videoPermission == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestStoragePermissions(Context context) {
        String [] permissions;
        if(android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU){
            permissions = new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        }else{
            permissions = new String[]{android.Manifest.permission.READ_MEDIA_AUDIO, android.Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO};
        }
        ActivityCompat.requestPermissions((Activity) context,
                permissions,
                4);
    }
}