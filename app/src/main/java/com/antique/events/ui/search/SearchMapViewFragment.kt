package com.antique.events.ui.search

import android.content.ContentValues
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.antique.events.R
import com.antique.events.data.model.Clinic
import com.antique.events.ui.appointment.ClinicActivity
import kotlinx.android.synthetic.main.fragment_search_map_view.*

class SearchMapViewFragment : Fragment(), OnMapReadyCallback {

    companion object {
        fun getInstance(): Fragment {
            val fragment = SearchMapViewFragment()
            return fragment
        }
    }

    private lateinit var mMap: GoogleMap
    private var markerMap: MutableMap<String, Clinic> = mutableMapOf();

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_map_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    private fun showMarkerDetails(clinic: Clinic){
        map_marker_details.visibility = View.VISIBLE;
        tv_item_title.text = clinic.name
        tv_item_position.text = clinic.specialization
        tv_item_address.text = clinic.address
        map_marker_details.setOnClickListener {
            startActivity(ClinicActivity.getIntent(requireContext(), clinic));
        }
    }

    // Fetch clinics
    private lateinit var clinicRef: DatabaseReference;
    private fun fetchClinics() {
        val clinics : MutableList<Clinic?> = mutableListOf();
        // clinic data listener
        clinicRef = Firebase.database.reference.child("users");
        val clinicsListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Fetch user data from firebase
                for (postSnapshot in dataSnapshot.children) {
                    val fetchedClinic: Clinic? = postSnapshot.getValue<Clinic>()!!;
                    if(fetchedClinic!!.type == 2){
                        if(fetchedClinic.latitude != null && fetchedClinic.longitude != null){
                            val commonMarker = MarkerOptions()
                                .snippet(fetchedClinic.address)
                                .title(fetchedClinic.name)
                                .position(LatLng(
                                fetchedClinic.latitude!!,
                                fetchedClinic.longitude!!
                            ));
                            val marker: Marker? = mMap.addMarker(commonMarker);
                            markerMap[marker!!.id] = fetchedClinic;
                        }
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        clinicRef.addValueEventListener(clinicsListener)
    }

    override fun onMapReady(gmap: GoogleMap) {
        mMap = gmap;
        val barotacNuevo = LatLng(10.889977, 122.705547)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(barotacNuevo))
        mMap.setMinZoomPreference(12f);
        mMap.setOnMarkerClickListener {
            this.showMarkerDetails(markerMap[it.id]!!);
            it.showInfoWindow();
            true
        }

        this.fetchClinics();
    }
}