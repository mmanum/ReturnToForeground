package net.xaduin.android.returntoforeground.services

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.util.Log
import android.view.accessibility.AccessibilityEvent

class MyAccessibilityService : AccessibilityService() {

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event?.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            val packageName = event.packageName
            // Aquí obtienes el nombre del paquete de la aplicación en primer plano
            Log.d("Foreground App", packageName.toString())
        }
    }

    override fun onInterrupt() {}

    override fun onServiceConnected() {
        super.onServiceConnected()
        // Configura los eventos que desea recibir tu servicio de accesibilidad
        val accessibilityServiceInfo = AccessibilityServiceInfo()
        accessibilityServiceInfo.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
        accessibilityServiceInfo.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
        serviceInfo = accessibilityServiceInfo
    }
}