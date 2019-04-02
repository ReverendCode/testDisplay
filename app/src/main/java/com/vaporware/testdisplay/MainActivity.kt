package com.vaporware.testdisplay

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*
import java.lang.Exception


class MainActivity : AppCompatActivity() {

    private var connection: Connection? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        connection = Connection(this, mapOf(
            "topic" to { message: String? ->
                text_example.text = message ?: "Null"
            },
            "toast" to { message ->
                Toast.makeText(this, message!!, Toast.LENGTH_SHORT).show()
            }
        ))
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun handleClick(view: View) {
        connection?.publishMessage("topic","FabPayload")
    }

}
