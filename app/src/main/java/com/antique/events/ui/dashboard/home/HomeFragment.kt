package com.antique.events.ui.dashboard.home

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import com.mikepenz.itemanimators.SlideDownAlphaAnimator
import com.antique.events.R
import com.antique.events.data.model.Event
import com.antique.events.data.model.GetActivitiesResponse
import com.antique.events.data.services.ApiRequester
import com.antique.events.data.services.LocalStorageService
import com.antique.events.ui.appointment.EventDetailsActivity
import com.antique.events.ui.dashboard.MainActivity
import com.antique.events.ui.dashboard.appointment.fragments.BarcodeBottomSheetDialog
import com.antique.events.ui.utils.adapter.EventAdapterItem
import com.antique.events.ui.utils.helper.DialogLoader
import com.xub.lakad.presentation.common.libs.VerticalSpaceItemDecoration
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private lateinit var eventAdapterItem: FastItemAdapter<EventAdapterItem>

    private lateinit var loadingDialog: Dialog;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        eventAdapterItem = FastItemAdapter();
        rv_events.itemAnimator = SlideDownAlphaAnimator()
        rv_events.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_events.addItemDecoration(VerticalSpaceItemDecoration(30))
        rv_events.adapter = eventAdapterItem;

        getEvents()
    }

    private fun getEvents() {
        loadingDialog = DialogLoader.createProgressDialog(context)!!;
        // show the dialog after it was initialized
        loadingDialog.show();
        ApiRequester.getActivities().enqueue(object : Callback<GetActivitiesResponse?> {
            override fun onResponse(
                call: Call<GetActivitiesResponse?>,
                response: Response<GetActivitiesResponse?>
            ) {
                loadingDialog.dismiss()
                if(response.code() == 200){
                    Log.d("Android debug", response.body().toString())
                    val user = LocalStorageService.getUser();
                    var events: List<Event> = response.body()!!.data.map {
                       Event(
                            id = it._id,
                            title = it.title,
                            description = it.description,
                            startDateTime = it.startDateTime,
                            endDateTime = it.endDateTime,
                            participants = it.participants,
                            registeredUsers = it.registeredUsers,
                            department = it.department
                        )
                    }

                    initEventItems(events)
                } else {
                    Toast.makeText(
                        context,
                        "Problem with connection please logout and try again",
                        Toast.LENGTH_LONG
                    ).show();
                }
            }

            override fun onFailure(call: Call<GetActivitiesResponse?>, t: Throwable) {
                loadingDialog.dismiss()
                Toast.makeText(
                    context,
                    "Problem with connection please logout and try again",
                    Toast.LENGTH_LONG
                ).show();
            }
        })
    }

    private fun initEventItems(events: List<Event>) {
        eventAdapterItem.clear()
        for (event in events) {
            eventAdapterItem.add(
                EventAdapterItem(event)
            )
        }
        eventAdapterItem.withOnClickListener{view, adapter, item, position ->
            startActivity(EventDetailsActivity.getIntent(requireContext(), item.model))
            true;
        }
        eventAdapterItem.notifyAdapterDataSetChanged()
    }

}