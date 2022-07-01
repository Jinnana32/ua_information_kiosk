package com.itsgmobility.hrbenefits.ui.placeholders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.antique.events.R
import kotlinx.android.synthetic.main.fragment_empty.*

class EmptyFragment : Fragment() {

    companion object {

        private val EMPTY_TITLE = "EMPTY_TITLE"
        private val EMPTY_MESSAGE = "EMPTY_MESSAGE"

        fun newInstance(message: String? = "", title: String? = ""): EmptyFragment {
            val fragment = EmptyFragment()
            val bundle = Bundle()
            bundle.putString(EMPTY_TITLE, if (title.isNullOrEmpty()) "Nothing’s happening now" else title)
            bundle.putString(EMPTY_MESSAGE, if (message.isNullOrEmpty()) "All your appointments will appear here." else message)
            fragment.arguments = bundle
            return fragment
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_empty, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title = this.requireArguments().getString(EMPTY_TITLE);
        val message = this.requireArguments().getString(EMPTY_MESSAGE);
        tv_empty_title.text = title;
        tv_empty_subtitle.text = message;

    }

}
