package com.example.leaflog.util

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener

class CustomSensorEventListener(
    val onChange: (Any) -> Unit
): SensorEventListener {
    override fun onSensorChanged(event: SensorEvent) {
        onChange(event.values[0])
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {

    }
}