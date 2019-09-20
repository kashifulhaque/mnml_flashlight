package tkzylabs.mnmlflashlight;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

@SuppressWarnings("FieldCanBeLocal")
public class MainActivity extends AppCompatActivity {

    // Widgets
    private ImageView mToggle;
    private CameraManager mCameraManager;

    // Variables
    private boolean isFlashAvailable, flashStatus;
    private String cameraID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // First check if the flash is available or not.
        checkFlashAvailable();

        // Initialize Widgets method call
        initialize();
    }

    // Method to check is the flash is available
    private void checkFlashAvailable() {
        isFlashAvailable = getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        /* If the flash is not available
        *
        *   Set flashStatus to false
        *   Call showError method.
        *
        * */
        if (!isFlashAvailable) {
            flashStatus = false;
            showError();
        }
        else {
            flashStatus = true;
        }
    }

    // Initialize all widgets here
    private void initialize() {
        mToggle = findViewById(R.id.btnToggle);

        // Initliaze the Camera Manager and the Camera ID
        mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        // Risky steps
        try {
            cameraID = mCameraManager.getCameraIdList()[0];
        }
        catch (CameraAccessException e) {
            e.printStackTrace();
        }

        // Set a click listener on the ImageView
        mToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFlashAvailable) {
                    if (flashStatus) {
                        mToggle.setImageDrawable(getDrawable(R.drawable.torch_on));
                        switchFlashlight(flashStatus);
                        flashStatus = false;
                    }
                    else {
                        mToggle.setImageDrawable(getDrawable(R.drawable.torch_off));
                        switchFlashlight(flashStatus);
                        flashStatus = true;
                    }
                }
            }
        });
    }

    // Toggle the Flashlight
    private void switchFlashlight(boolean flashStatus) {
        try {
            mCameraManager.setTorchMode(cameraID, flashStatus);
        }
        catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    // Flash not available error
    private void showError() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Error");
        alertDialog.setMessage("Flash is NOT available");
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        alertDialog.show();
    }

}
