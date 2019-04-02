package com.vaporware.testdisplay

import android.content.Context
import android.util.Log
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*
import java.lang.Exception


class Connection(context: Context,
                 topicMap: Map<String, (String?) -> Unit>,
                 serverUri: String = "tcp://iot.eclipse.org:1883",
                 clientId: String = "defaultUser"
                 ) {

    private val options = MqttConnectOptions().also {
        it.isCleanSession = false
        it.isAutomaticReconnect = true
        it.setWill("topic", "Disconnected".toByteArray(),1, true)
    }

    private val callback = ObservableTopicListMqttCallback(topicMap)
    private val client = MqttAndroidClient(context, serverUri, clientId).also {
        it.setCallback(callback)
        try {
            val token = it.connect(options)
            token.actionCallback = IMqttSubscriptionListener(topicMap.keys.toList())
        } catch (ex: Exception) {
            Log.e("Connection", ex.message)
        }
    }
    fun publishMessage(topic: String, message: String) {
        client.publish(topic, message.toByteArray(),0,false)
    }
}

class IMqttSubscriptionListener(private val subscriptions: List<String>): IMqttActionListener {
    override fun onSuccess(asyncActionToken: IMqttToken?) {
        Log.d("onCreate","connection success")
        for (sub in subscriptions) asyncActionToken?.client?.subscribe(sub, 0)

    }

    override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
        Log.d("onCreate", "connection failure: ${exception?: "no exception found"}")
    }
}

class ObservableTopicListMqttCallback(private val topicMap: Map<String, (String?) -> Unit>): MqttCallback {

    override fun messageArrived(topic: String?, message: MqttMessage?) {
        topicMap[topic]?.invoke(String(message!!.payload))
    }

    override fun connectionLost(cause: Throwable?) {
        Log.d("connectionLost", "Cause: ${cause.toString()}")
    }

    override fun deliveryComplete(token: IMqttDeliveryToken?) {
        Log.d("deliveryComplete",token.toString())
    }

}
