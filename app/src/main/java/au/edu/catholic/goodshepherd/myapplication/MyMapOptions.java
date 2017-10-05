package au.edu.catholic.goodshepherd.myapplication;

import android.graphics.Color;

import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MyMapOptions {

    // Oval related map options

    // Coordinates for the centre of the oval
    static LatLng ovalCentre = new LatLng(-20.720620, 139.494870);
    // A circle that fits on the oval
    static CircleOptions ovalCircle = new CircleOptions()
            .radius(35)
            .center(ovalCentre)
            .fillColor(MyColors.transparentRed)
            .strokeWidth(1)
            .strokeColor(MyColors.solidRed);
    // A marker for the oval
    static MarkerOptions ovalMarker = new MarkerOptions()
            .position(ovalCentre)
            .title("Oval");

    // Computer room related map options

    // the computer room coordinates
    static LatLng pcLatLng = new LatLng(-20.721402, 139.494422);

    // a marker for the computer room
    static MarkerOptions pcRoomMarker = new MarkerOptions()
            .position(pcLatLng)
            .title("Computer Room");

    // A solid black line that traces around the sidewalk
    static PolylineOptions schoolSidewalkOutline = new PolylineOptions()
            .color(MyColors.solidBlack)
            .width(3)
            .add(new LatLng(-20.720132, 139.494677))
            .add(new LatLng(-20.720461, 139.495701))
            .add(new LatLng(-20.722229, 139.495055))
            .add(new LatLng(-20.721998, 139.494001))
            .add(new LatLng(-20.720132, 139.494677));

    // An enclosed shape that fills the school area
    static PolygonOptions schoolArea = new PolygonOptions()
            .fillColor(MyColors.transparentBlue)
            .strokeColor(MyColors.solidBlue)
            .strokeWidth(2)
            .add(new LatLng(-20.720205, 139.494721))
            .add(new LatLng(-20.720495, 139.495633))
            .add(new LatLng(-20.722192, 139.495023))
            .add(new LatLng(-20.721941, 139.494096))
            // Put this on the bottom layer
            .zIndex(-1);

    // Some colours that I've predefined for easier reading.
    static class MyColors {
        static int transparentRed =   Color.argb(60, 255,   0,   0);
        static int transparentGreen = Color.argb(60,   0, 255,   0);
        static int transparentBlue =  Color.argb(60,   0,   0, 255);

        static int solidRed =   Color.argb(255, 255,   0,   0);
        static int solidGreen = Color.argb(255,   0, 255,   0);
        static int solidBlue =  Color.argb(255,   0,   0, 255);

        static int solidBlack =  Color.argb(255,   0,   0,  0);
    }
}
