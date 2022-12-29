package com.przemas.monitoring.rest.model

import com.fasterxml.jackson.annotation.JsonProperty

data class Monitor(
    @JsonProperty("AlarmFrameCount")
    var AlarmFrameCount: String? = null,
    @JsonProperty("AlarmMaxFPS")
    var AlarmMaxFPS: String? = null,
    @JsonProperty("AlarmRefBlendPerc")
    var AlarmRefBlendPerc: String? = null,
    @JsonProperty("AnalysisFPSLimit")
    var AnalysisFPSLimit: String? = null,
    @JsonProperty("AnalysisUpdateDelay")
    var AnalysisUpdateDelay: String? = null,
    @JsonProperty("ArchivedEventDiskSpace")
    var ArchivedEventDiskSpace: String? = null,
    @JsonProperty("ArchivedEvents")
    var ArchivedEvents: String? = null,
    @JsonProperty("AutoStopTimeout")
    var AutoStopTimeout: String? = null,
    @JsonProperty("Brightness")
    var Brightness: String? = null,
    @JsonProperty("Channel")
    var Channel: String? = null,
    @JsonProperty("Colour")
    var Colour: String? = null,
    @JsonProperty("Colours")
    var Colours: String? = null,
    @JsonProperty("Contrast")
    var Contrast: String? = null,
    @JsonProperty("ControlAddress")
    var ControlAddress: String? = null,
    @JsonProperty("ControlDevice")
    var ControlDevice: String? = null,
    @JsonProperty("ControlId")
    var ControlId: String? = null,
    @JsonProperty("Controllable")
    var Controllable: String? = null,
    @JsonProperty("DayEventDiskSpace")
    var DayEventDiskSpace: String? = null,
    @JsonProperty("DayEvents")
    var DayEvents: String? = null,
    @JsonProperty("DefaultRate")
    var DefaultRate: String? = null,
    @JsonProperty("DefaultScale")
    var DefaultScale: String? = null,
    @JsonProperty("DefaultView")
    var DefaultView: String? = null,
    @JsonProperty("Deinterlacing")
    var Deinterlacing: String? = null,
    @JsonProperty("Device")
    var Device: String? = null,
    @JsonProperty("Enabled")
    var Enabled: String? = null,
    @JsonProperty("EncoderParameters")
    var EncoderParameters: String? = null,
    @JsonProperty("EventPrefix")
    var EventPrefix: String? = null,
    @JsonProperty("Exif")
    var Exif: String? = null,
    @JsonProperty("FPSReportInterval")
    var FPSReportInterval: String? = null,
    @JsonProperty("Format")
    var Format: String? = null,
    @JsonProperty("FrameSkip")
    var FrameSkip: String? = null,
    @JsonProperty("Function")
    var Function: String? = null,
    @JsonProperty("Height")
    var Height: String? = null,
    @JsonProperty("Host")
    var Host: String? = null,
    @JsonProperty("HourEventDiskSpace")
    var HourEventDiskSpace: String? = null,
    @JsonProperty("HourEvents")
    var HourEvents: String? = null,
    @JsonProperty("Hue")
    var Hue: String? = null,
    @JsonProperty("Id")
    var Id: String? = null,
    @JsonProperty("ImageBufferCount")
    var ImageBufferCount: String? = null,
    @JsonProperty("LabelFormat")
    var LabelFormat: String? = null,
    @JsonProperty("LabelSize")
    var LabelSize: String? = null,
    @JsonProperty("LabelX")
    var LabelX: String? = null,
    @JsonProperty("LabelY")
    var LabelY: String? = null,
    @JsonProperty("LinkedMonitors")
    var LinkedMonitors: String? = null,
    @JsonProperty("MaxFPS")
    var MaxFPS: String? = null,
    @JsonProperty("Method")
    var Method: String? = null,
    @JsonProperty("MonthEventDiskSpace")
    var MonthEventDiskSpace: String? = null,
    @JsonProperty("MonthEvents")
    var MonthEvents: String? = null,
    @JsonProperty("MotionFrameSkip")
    var MotionFrameSkip: String? = null,
    @JsonProperty("Name")
    var Name: String? = null,
    @JsonProperty("Options")
    var Options: String? = null,
    @JsonProperty("Orientation")
    var Orientation: String? = null,
    @JsonProperty("OutputCodec")
    var OutputCodec: String? = null,
    @JsonProperty("OutputContainer")
    var OutputContainer: String? = null,
    @JsonProperty("Palette")
    var Palette: String? = null,
    @JsonProperty("Pass")
    var Pass: String? = null,
    @JsonProperty("Path")
    var Path: String? = null,
    @JsonProperty("Port")
    var Port: String? = null,
    @JsonProperty("PostEventCount")
    var PostEventCount: String? = null,
    @JsonProperty("PreEventCount")
    var PreEventCount: String? = null,
    @JsonProperty("Protocol")
    var Protocol: String? = null,
    @JsonProperty("RTSPDescribe")
    var RTSPDescribe: String? = null,
    @JsonProperty("RecordAudio")
    var RecordAudio: String? = null,
    @JsonProperty("RefBlendPerc")
    var RefBlendPerc: String? = null,
    @JsonProperty("Refresh")
    var Refresh: String? = null,
    @JsonProperty("ReturnDelay")
    var ReturnDelay: String? = null,
    @JsonProperty("ReturnLocation")
    var ReturnLocation: String? = null,
    @JsonProperty("SaveJPEGs")
    var SaveJPEGs: String? = null,
    @JsonProperty("SectionLength")
    var SectionLength: String? = null,
    @JsonProperty("Sequence")
    var Sequence: String? = null,
    @JsonProperty("ServerId")
    var ServerId: String? = null,
    @JsonProperty("SignalCheckColour")
    var SignalCheckColour: String? = null,
    @JsonProperty("SignalCheckPoints")
    var SignalCheckPoints: String? = null,
    @JsonProperty("StorageId")
    var StorageId: String? = null,
    @JsonProperty("StreamReplayBuffer")
    var StreamReplayBuffer: String? = null,
    @JsonProperty("SubPath")
    var SubPath: String? = null,
    @JsonProperty("TotalEventDiskSpace")
    var TotalEventDiskSpace: String? = null,
    @JsonProperty("TotalEvents")
    var TotalEvents: String? = null,
    @JsonProperty("TrackDelay")
    var TrackDelay: String? = null,
    @JsonProperty("TrackMotion")
    var TrackMotion: String? = null,
    @JsonProperty("Triggers")
    var Triggers: String? = null,
    @JsonProperty("Type")
    var Type: String? = null,
    @JsonProperty("User")
    var User: String? = null,
    @JsonProperty("V4LCapturesPerFrame")
    var V4LCapturesPerFrame: String? = null,
    @JsonProperty("V4LMultiBuffer")
    var V4LMultiBuffer: String? = null,
    @JsonProperty("VideoWriter")
    var VideoWriter: String? = null,
    @JsonProperty("WarmupCount")
    var WarmupCount: String? = null,
    @JsonProperty("WebColour")
    var WebColour: String? = null,
    @JsonProperty("WeekEventDiskSpace")
    var WeekEventDiskSpace: String? = null,
    @JsonProperty("WeekEvents")
    var WeekEvents: String? = null,
    @JsonProperty("Width")
    var Width: String? = null,
    @JsonProperty("ZoneCount")
    var ZoneCount: String? = null
)

data class MonitorInfoResponse(
    @JsonProperty("Monitor")
    var Monitor: Monitor? = null,
    @JsonProperty("Monitor_Status")
    var Monitor_Status: MonitorStatus? = null
)

data class MonitorInfosResponseWrapper(
    @JsonProperty("monitors")
    var monitors: List<MonitorInfoResponse>? = null
)

data class MonitoringState(
    @JsonProperty("Definition")
    var Definition: String? = null,
    @JsonProperty("Id")
    var Id: String? = null,
    @JsonProperty("IsActive")
    var IsActive: String? = null,
    @JsonProperty("Name")
    var Name: String? = null
)

data class MonitoringStateResponse(
    @JsonProperty("State")
    var State: MonitoringState? = null
)

data class MonitoringStatesResponseWrapper(
    @JsonProperty("states")
    var states: List<MonitoringStateResponse>? = null
)

data class MonitorStatus(
    @JsonProperty("AnalysisFPS")
    var AnalysisFPS: String? = null,
    @JsonProperty("CaptureBandwidth")
    var CaptureBandwidth: String? = null,
    @JsonProperty("CaptureFPS")
    var CaptureFPS: String? = null,
    @JsonProperty("MonitorId")
    var MonitorId: String? = null,
    @JsonProperty("Status")
    var Status: String? = null
)