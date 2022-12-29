package com.przemas.monitoring.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.przemas.monitoring.rest.model.Monitor
import com.przemas.monitoring.rest.model.MonitoringState
import com.przemas.monitoring.tunnel.model.Host

class MonitorModel : ViewModel() {
    var connectionStatus = MutableLiveData<ConnectionStatus>()
    var host: MutableLiveData<Host> = MutableLiveData()
    var monitoringStates = MutableLiveData<List<MonitoringState>>()
    var monitors = MutableLiveData<List<Monitor>>()
}

