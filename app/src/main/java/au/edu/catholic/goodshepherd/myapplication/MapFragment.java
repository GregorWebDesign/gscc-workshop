package au.edu.catholic.goodshepherd.myapplication;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.maps.android.PolyUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment {
    TextView textLongitude;
    TextView textLatitude;

    Circle ovalCircle;
    Polygon schoolArea;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Search for a Fragment with the "tag" mapFragment
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentByTag("mapFragment");
        // if it doesn't exist...
        if (supportMapFragment == null) {
            // Create the fragment here
            supportMapFragment = new SupportMapFragment();
            // We need to tell the Fragment Manager to load the fragment using this transaction
            // because it's a Fragment inside of a Fragment. If you don't do it this way,
            // you will get errors when swapping tabs.
            getChildFragmentManager()
                    .beginTransaction()
                    .add(R.id.mapContainer, supportMapFragment, "mapFragment")
                    .commitNow();
        }
        // When the map is ready, call our callback
        supportMapFragment.getMapAsync(new MyMapReadyCallback());

        textLatitude = (TextView) view.findViewById(R.id.textLatitude);
        textLongitude = (TextView) view.findViewById(R.id.textLongitude);

        // Check if we have the correct permissions and throw an exception if we don't
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            throw new SecurityException("Requires Access Fine Location Permission");
        }
        // Get the locationation manager from the system service.
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(new Criteria(), true);

        // Request that we get Location Updates in MyLocationListener
        locationManager.requestLocationUpdates(provider, 500, 1, new MyLocationListener());
    }

    class MyMapReadyCallback implements OnMapReadyCallback {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            // Check if we have the correct permissions.
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // Crash if we don't :(
                throw new SecurityException("Requires Access Fine Location Permission");
            }

            // Enable the "My Location" feature
            googleMap.setMyLocationEnabled(true);

            // Oval Map Parts
            ovalCircle = googleMap.addCircle(MyMapOptions.ovalCircle);
            googleMap.addMarker(MyMapOptions.ovalMarker);

            // Computer Room Map Parts
            googleMap.addMarker(MyMapOptions.pcRoomMarker);

            // School Map Parts
            schoolArea = googleMap.addPolygon(MyMapOptions.schoolArea);
            googleMap.addPolyline(MyMapOptions.schoolSidewalkOutline);
        }
    }

    class MyLocationListener implements LocationListener {
        @Override
        // This function will get called every time out location updates.
        public void onLocationChanged(Location location) {
            // Display our Latitude
            textLatitude.setText("Latitude: " + location.getLatitude());
            // Display our Longitude
            textLongitude.setText("Longitude: " + location.getLongitude());

            // We want to use Location.distanceTo which doesn't allow the LatLng types
            // that our shapes use, so we convert it like this.
            Location ovalCentre = new Location("");
            ovalCentre.setLongitude(ovalCircle.getCenter().longitude);
            ovalCentre.setLatitude(ovalCircle.getCenter().latitude);

            if (location.distanceTo(ovalCentre) < ovalCircle.getRadius()) {
                ovalCircle.setFillColor(MyMapOptions.MyColors.transparentGreen);
                ovalCircle.setStrokeColor(MyMapOptions.MyColors.solidGreen);
            } else {
                ovalCircle.setFillColor(MyMapOptions.MyColors.transparentRed);
                ovalCircle.setStrokeColor(MyMapOptions.MyColors.solidRed);
            }

            // We want to use PolyUtil.containsLocation which doesn't allow the Location types
            // that our location uses, so we convert it like this...
            LatLng locationLatLng = new LatLng(location.getLatitude(), location.getLongitude());

            if (PolyUtil.containsLocation(locationLatLng, schoolArea.getPoints(), false)) {
                schoolArea.setStrokeColor(MyMapOptions.MyColors.solidGreen);
            } else {
                schoolArea.setStrokeColor(MyMapOptions.MyColors.solidBlack);
            }
        }

        @Override public void onStatusChanged(String s, int i, Bundle bundle) {}
        @Override public void onProviderEnabled(String s) {}
        @Override public void onProviderDisabled(String s) {}
    }
}
