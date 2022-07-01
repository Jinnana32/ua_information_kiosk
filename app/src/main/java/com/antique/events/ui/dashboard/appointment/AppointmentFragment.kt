package com.antique.events.ui.dashboard.appointment

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.antique.events.R
import com.antique.events.data.model.DownloadData
import com.antique.events.data.model.Event
import com.antique.events.data.model.GetDocumentResponse
import com.antique.events.data.services.ApiRequester
import com.antique.events.data.services.LocalStorageService
import com.antique.events.ui.appointment.EventDetailsActivity
import com.antique.events.ui.dashboard.appointment.fragments.HistoryAppoinmentFragment
import com.antique.events.ui.dashboard.appointment.fragments.PendingAppoinmentFragment
import com.antique.events.ui.utils.adapter.DownloadsAdapterItem
import com.antique.events.ui.utils.adapter.EventAdapterItem
import com.antique.events.ui.utils.helper.DialogLoader
import com.antique.events.utils.FragmentHelper
import com.antique.events.utils.FragmentLoaderUtil
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import com.mikepenz.itemanimators.SlideDownAlphaAnimator
import com.xub.lakad.presentation.common.libs.VerticalSpaceItemDecoration
import kotlinx.android.synthetic.main.fragment_appointment.*
import kotlinx.android.synthetic.main.fragment_home.rv_events
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AppointmentFragment : Fragment() {

    private val APPOINTMENT_PENDING_TAB = 0;
    private val APPOINTMENT_HISTORY_TAB = 1;

    private lateinit var downloadAdapterItem: FastItemAdapter<DownloadsAdapterItem>

    private lateinit var loadingDialog: Dialog;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_appointment, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadingDialog = DialogLoader.createProgressDialog(context)!!;

        downloadAdapterItem = FastItemAdapter();
        rv_events.itemAnimator = SlideDownAlphaAnimator()
        rv_events.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_events.addItemDecoration(VerticalSpaceItemDecoration(30))
        rv_events.adapter = downloadAdapterItem;


        initDownloads()
    }

    private fun initDownloads(){

        loadingDialog.show();
        val user = LocalStorageService.getUser();

        ApiRequester.getDocuments(user._id).enqueue(object : Callback<GetDocumentResponse?> {
            override fun onResponse(
                call: Call<GetDocumentResponse?>,
                response: Response<GetDocumentResponse?>
            ) {
                loadingDialog.dismiss()
                if(response.code() == 200){
                    Log.d("Android debug", response.body().toString())
                    val downloads: List<DownloadData> = response.body()!!.data.map {
                        DownloadData(
                             __v = it.__v,
                       _id = it._id,
                        createdAt = it.createdAt,
                        createdBy = it.createdBy,
                        department = it.department,
                         description = it.description,
                        fileName = it.fileName,
                        permission = it.permission,
                        student = it.student,
                       studentRequest = it.studentRequest,
                       title = it.title
                        )
                    }

                    initDownloads(downloads)
            }
            }

            override fun onFailure(call: Call<GetDocumentResponse?>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun initDownloads(events: List<DownloadData>) {
        downloadAdapterItem.clear()
        for (event in events) {
            downloadAdapterItem.add(
                DownloadsAdapterItem(event)
            )
        }
        downloadAdapterItem.withOnClickListener{view, adapter, item, position ->

            true;
        }
        downloadAdapterItem.notifyAdapterDataSetChanged()
    }


}