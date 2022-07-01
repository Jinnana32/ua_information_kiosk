package com.antique.events.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.antique.events.R
import com.antique.events.utils.FragmentHelper
import com.google.android.material.tabs.TabLayout
import com.newcardano.clinica.ui.dashboard.home.SearchListFragment
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {

    companion object {
        fun getIntent(context: Context) : Intent {
            return Intent(context, SearchActivity::class.java);
        }
    }

    private val SEARCH_LIST_TAB = 0;
    private val SEARCH_MAP_TAB = 1;

    private lateinit var listFragment : Fragment;
    private lateinit var searchInterface: SearchInterface;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val toolbar: Toolbar = toolbar as Toolbar
        setSupportActionBar(toolbar)
        if(supportActionBar !== null){
            supportActionBar!!.setTitle("Search clinics");
            supportActionBar!!.setDisplayHomeAsUpEnabled(true);
            supportActionBar!!.setDisplayShowHomeEnabled(true);

            toolbar.setNavigationOnClickListener {
                finish();
            }
        }

        listFragment = SearchListFragment.getInstance();

        et_search.addTextChangedListener {
            getSearchInterface()!!.onSearch(it.toString());
        }

        initTabs();
    }

    fun getSearchInterface(): SearchInterface? {
        return searchInterface
    }

    fun setSearchInterface(search: SearchInterface) {
        searchInterface = search
    }

    private fun initTabs() {
        // initial tab
        FragmentHelper.setupFragment(
            supportFragmentManager,
            SearchListFragment.getInstance(),
            R.id.layout_search
        )

        tab_search.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
                tabSelected(tab)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tabSelected(tab)
            }
        })
    }

    private fun tabSelected(tab: TabLayout.Tab?) {
        if (tab != null) {
            when (tab.position) {
                SEARCH_LIST_TAB -> {

                    layout_search.visibility = View.VISIBLE;
                    layout_map.visibility = View.GONE;
                    FragmentHelper.setupFragment(
                        supportFragmentManager,
                        listFragment,
                        R.id.layout_search
                    )
                }
                SEARCH_MAP_TAB -> {
                    layout_search.visibility = View.GONE;
                    layout_map.visibility = View.VISIBLE;
                    FragmentHelper.setupFragment(
                        supportFragmentManager,
                        SearchMapViewFragment.getInstance(),
                        R.id.layout_map
                    )
                }
            }

        }
    }

}