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
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccelBarsFragment extends Fragment {
    private final static int X = 0;
    private final static int Y = 1;
    private final static int Z = 2;

    private TextView textOrientation;
    private TextView[] texts = new TextView[3];
    private Bar[] bars = new Bar[3];

    public AccelBarsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_accel_bars, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textOrientation = (TextView) getView().findViewById(R.id.orientation);

        texts[X] = (TextView) getView().findViewById(R.id.accelX);
        bars[X] = new Bar(R.id.barXPositiveSpace, R.id.barXPositive, R.id.barXNegative, R.id.barXNegativeSpace);

        texts[Y] = (TextView) getView().findViewById(R.id.accelY);
        bars[Y] = new Bar(R.id.barYPositiveSpace, R.id.barYPositive, R.id.barYNegative, R.id.barYNegativeSpace);

        texts[Z] = (TextView) getView().findViewById(R.id.accelZ);
        bars[Z] = new Bar(R.id.barZPositiveSpace, R.id.barZPositive, R.id.barZNegative, R.id.barZNegativeSpace);

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
            updateOrientation(sensorEvent.values);

            for (int i=0; i<3; i++) {
                texts[i].setText(Float.toString(sensorEvent.values[i]));
                bars[i].updateBar(sensorEvent.values[i]);
            }
        }

        // We aren't going to use this, but it needs to be here!
        @Override public void onAccuracyChanged(Sensor sensor, int i) {}
    }

    private void updateOrientation(float[] values) {
        final float low = 4;
        final float high = 8;

        final int X = 0, Y = 1, Z = 2;

        if (Math.abs(values[X]) > high && Math.abs(values[Y]) < low && Math.abs(values[Z]) < low) {
            if (values[X] > 0) {
                textOrientation.setText(R.string.orientationXPositive);
            } else {
                textOrientation.setText(R.string.orientationXNegative);
            }
        } else if (Math.abs(values[X]) < low && Math.abs(values[Y]) > high && Math.abs(values[Z]) < low) {
            if (values[Y] > 0) {
                textOrientation.setText(R.string.orientationYPositive);
            } else {
                textOrientation.setText(R.string.orientationYNegative);
            }
        } else if (Math.abs(values[X]) < low && Math.abs(values[Y]) < low && Math.abs(values[Z]) > high) {
            if (values[Z] > 0) {
                textOrientation.setText(R.string.orientationZPositive);
            } else {
                textOrientation.setText(R.string.orientationZNegative);
            }
        } else {
            textOrientation.setText(R.string.orientationUnsure);
        }
    }

    private class Bar {
        private final static int HEIGHT = 30;

        private final Space positiveSpace;
        private final View positiveBar;
        private final View negativeBar;
        private final Space negativeSpace;

        private final float max = 20;

        Bar(int positiveSpaceId, int positiveBarId, int negativeBarId, int negativeSpaceId) {
            positiveSpace = (Space) getView().findViewById(positiveSpaceId);
            positiveBar = (View) getView().findViewById(positiveBarId);
            negativeBar = (View) getView().findViewById(negativeBarId);
            negativeSpace = (Space) getView().findViewById(negativeSpaceId);
        }

        void updateBar(float value) {
            LinearLayout.LayoutParams empty = new LinearLayout.LayoutParams(0, HEIGHT, 0f);
            LinearLayout.LayoutParams full = new LinearLayout.LayoutParams(0, HEIGHT, 1f);

            if (value > 0) {
                negativeBar.setLayoutParams(empty);
                negativeSpace.setLayoutParams(full);

                float barRatio = Math.min(value / max, 1);
                positiveBar.setLayoutParams(new LinearLayout.LayoutParams(0, HEIGHT, barRatio));
                positiveSpace.setLayoutParams(new LinearLayout.LayoutParams(0, HEIGHT, 1-barRatio));
            } else {
                positiveBar.setLayoutParams(empty);
                positiveSpace.setLayoutParams(full);

                float barRatio = Math.min(-value / max, 1);
                negativeBar.setLayoutParams(new LinearLayout.LayoutParams(0, HEIGHT, barRatio));
                negativeSpace.setLayoutParams(new LinearLayout.LayoutParams(0, HEIGHT, 1-barRatio));
            }
        }
    }
}
