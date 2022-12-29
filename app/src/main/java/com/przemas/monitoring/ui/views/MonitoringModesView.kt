package com.przemas.monitoring.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import com.przemas.monitoring.R
import com.przemas.monitoring.rest.model.MonitoringState

class MonitoringModesView(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    LinearLayout(context, attrs, defStyleAttr) {
    private var modesSpinner: Spinner
    private var modesSpinnerItems: List<String> = ArrayList()

    var monitoringStates: List<MonitoringState> = emptyList()
        set(value) {
            field = value
            synchronizeUI()
        }

    init {
        LayoutInflater.from(context)
            .inflate(R.layout.view_monitoring_modes, this, true)
        modesSpinner = findViewById(R.id.monitoring_modes_spinner)
    }

    private var selectedStateName: String?
        get() = modesSpinnerItems[modesSpinner.selectedItemPosition]
        set(value) {
            modesSpinnerItems.indexOf(value).takeIf { it > 0 }
                ?.let { modesSpinner.setSelection(it) }
        }

    var selectedState: MonitoringState?
        get() = monitoringStates.find { it.Name == selectedStateName }
        set(value) {
            selectedStateName = value?.Name
        }

    private fun synchronizeUI() {
        modesSpinnerItems = arrayListOf("") + monitoringStates.map { it.Name!! }

        modesSpinner.adapter = ArrayAdapter(
            context,
            android.R.layout.simple_list_item_1,
            modesSpinnerItems
        )
    }

    fun setOnItemSelected(listener: (state: MonitoringState?) -> Unit) {
        modesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // do nothing
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                listener.invoke(selectedState)
            }
        }
    }
}