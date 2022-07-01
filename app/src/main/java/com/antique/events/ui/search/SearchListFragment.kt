package com.newcardano.clinica.ui.dashboard.home

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.antique.events.R
import com.antique.events.data.model.Clinic
import com.antique.events.ui.appointment.ClinicActivity
import com.antique.events.ui.search.SearchActivity
import com.antique.events.ui.search.SearchInterface
import com.antique.events.ui.utils.adapter.ClinicAdapterItem
import com.antique.events.ui.utils.helper.StringUtils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import com.mikepenz.itemanimators.SlideDownAlphaAnimator
import com.xub.lakad.presentation.common.libs.VerticalSpaceItemDecoration
import kotlinx.android.synthetic.main.fragment_search_list.*


class SearchListFragment : Fragment() {

    companion object {
        fun getInstance(): Fragment {
            val fragment = SearchListFragment()
            return fragment
        }
    }

    private lateinit var clinicAdapterItem: FastItemAdapter<ClinicAdapterItem>
    private var clinics : MutableList<Clinic?> = mutableListOf();
    private var searchedClinics : MutableList<Clinic?> = mutableListOf();

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_search_list, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clinicAdapterItem = FastItemAdapter();
        rv_clinics.itemAnimator = SlideDownAlphaAnimator()
        rv_clinics.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_clinics.addItemDecoration(VerticalSpaceItemDecoration(30))
        rv_clinics.adapter = clinicAdapterItem;

        (activity as SearchActivity).setSearchInterface(object : SearchInterface {
            override fun onSearch(query: String) {
                onSearchClinics(query);
            }
        });

        this.fetchClinics();
    }

    private fun initAppointmentItems(clinics: List<Clinic?>) {
        clinicAdapterItem.clear()
        for (clinic in clinics) {
            clinicAdapterItem.add(
                ClinicAdapterItem(clinic!!)
            )
        }
        clinicAdapterItem.withOnClickListener { view, adapter, item, position ->
            startActivity(ClinicActivity.getIntent(requireContext(), item.model));
            true;
        };
        clinicAdapterItem.notifyAdapterDataSetChanged()
    }

    // Fetch clinics
    private lateinit var clinicRef: DatabaseReference;
    private fun fetchClinics() {
        // clinic data listener
        clinicRef = Firebase.database.reference.child("users");
        val clinicsListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Fetch user data from firebase
                for (postSnapshot in dataSnapshot.children) {
                    val fetchedClinic: Clinic? = postSnapshot.getValue<Clinic>()!!;
                    if(fetchedClinic!!.type == 2){
                        clinics.add(fetchedClinic);
                    }
                }
                initAppointmentItems(clinics);
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        clinicRef.addValueEventListener(clinicsListener)
    }

    fun onSearchClinics(clinicName: String)  {
        if(clinicName.isNotEmpty()){
            Log.e("Android debug", "Initial size ${clinics.size}");
            searchedClinics = clinics.filter {
                var hasLetter = true;
                val stringLength = it!!.name.length - (it.name.length * 0.05);
                val distance: Int = StringUtils.levenshtein(clinicName, it.name);
                if(distance >= stringLength){
                    hasLetter = false
                }
                hasLetter
            }.toMutableList()
            Log.e("Android debug", "Search length ${searchedClinics.size}" );
            initAppointmentItems(searchedClinics);
        }else {
            initAppointmentItems(clinics);
        }
    }
}