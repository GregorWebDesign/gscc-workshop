package au.edu.catholic.goodshepherd.myapplication;


import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 */
public class CameraFragment extends Fragment {
    SurfaceView surfaceCamera;
    Switch switchCamera;
    TextView textCamera;

    CameraSource cameraSource;
    BarcodeDetector detector;

    public CameraFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // find the views for our field variables
        surfaceCamera = (SurfaceView) view.findViewById(R.id.surfaceCamera);
        switchCamera = (Switch) view.findViewById(R.id.switchCamera);
        textCamera = (TextView) view.findViewById(R.id.textCamera);

        // This is the action that happens with the switch is toggled
        switchCamera.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton b, boolean isToggledOn) {
                if (isToggledOn) {
                    // Check we have the correct permission and give a warning if not
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        textCamera.setText("You don't have permission to use the camera!");
                        // then don't do anything else
                        return;
                    }

                    textCamera.setText("Switch flipped 'ON'");

                    // Create a barcode detector using the Builder.
                    detector = new BarcodeDetector.Builder(getActivity().getApplicationContext())
                            // Let's detect ALL FORMATs of barcodes!
                            .setBarcodeFormats(Barcode.ALL_FORMATS)
                            .build();

                    detector.setProcessor(new MyDetectorProcessor());

                    // Create a camera source for our detector and our surfaceView
                    // This needs the `final` keyword because we are using it inside a callback below
                    cameraSource = new CameraSource.Builder(getContext(), detector)
                            .setAutoFocusEnabled(true)
                            .setFacing(CameraSource.CAMERA_FACING_BACK)
                            .setRequestedFps(15.0f)
                            .setRequestedPreviewSize(640, 480)
                            .build();

                    try {
                        textCamera.setText("Starting Camera!");
                        // Start the camera
                        cameraSource.start(surfaceCamera.getHolder());
                        textCamera.setText("Camera Running! Find a Barcode.");
                    }
                    // If there was an error starting the camera...
                    catch (IOException e) {
                        // Display a message
                        textCamera.setText("There was a Camera Start Error! "  + e.getMessage());
                    }
                } else {
                    // If we toggle the switch off, stop the camera
                    cameraSource.stop();
                    textCamera.setText("Camera Paused");
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_camera, container, false);
    }

    class MyDetectorProcessor implements Detector.Processor<Barcode> {
        @Override public void release() {}

        @Override
        public void receiveDetections(Detector.Detections<Barcode> detections) {
            // We read the detections into an array of barcodes
            final SparseArray<Barcode> barcodes = detections.getDetectedItems();

            // Make sure some barcodes exist
            if (barcodes.size() != 0) {
                // use post because we're actually in a different thread right now!
                textCamera.post(new Runnable() {    // Use the post method of the TextView
                    public void run() {
                        // get the first barcode from the array and show it on screen.
                        textCamera.setText(barcodes.valueAt(0).displayValue);
                    }
                });
            }
        }
    }
}
