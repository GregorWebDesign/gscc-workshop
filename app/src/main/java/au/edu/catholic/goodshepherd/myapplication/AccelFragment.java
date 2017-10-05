package au.edu.catholic.goodshepherd.myapplication;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccelFragment extends Fragment {
    TextView textAccelX;
    TextView textAccelY;
    TextView textAccelZ;

    public AccelFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_accel, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Point the field variables to the widgets from the view
        // using the IDs we gave them in fragment_accel.xml
        textAccelX = (TextView) view.findViewById(R.id.textAccelX);
        textAccelY = (TextView) view.findViewById(R.id.textAccelY);
        textAccelZ = (TextView) view.findViewById(R.id.textAccelZ);

        // Get the sensor manager from the Android Activity System Service
        SensorManager sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);

        // Get the accelerometer sensor from the sensor manager
        Sensor accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Create an "instance" of MySensorEventListener
        MySensorEventListener myListener = new MySensorEventListener();

        // Register our listener with the sensor manager
        sensorManager.registerListener(myListener, accelSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    class MySensorEventListener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            // This function is called when the sensor updates.
            // sensorEvent contains the new sensor data in an
            // values: [0: X reading, 1: Y reading, 2: Z reading]
            textAccelX.setText("X: " + sensorEvent.values[0]);
            textAccelY.setText("Y: " + sensorEvent.values[1]);
            textAccelZ.setText("Z: " + sensorEvent.values[2]);
        }

        // We aren't going to use this, but it needs to be here!
        @Override public void onAccuracyChanged(Sensor sensor, int i) {}
    }
}
