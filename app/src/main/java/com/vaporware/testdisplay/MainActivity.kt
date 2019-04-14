package com.vaporware.testdisplay

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var mqttBrokerConnection: MqttBrokerConnection? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        mqttBrokerConnection = MqttBrokerConnection(this, mapOf(

            "+/foo" to { topic, message ->
                Toast.makeText(this, "Any temp: $topic", Toast.LENGTH_SHORT).show()
                cargo_temp_text.text = "$message F"
            },
            "foo/+" to { topic, message ->
                cargo_temp_text.text = message
                Toast.makeText(this, "the Topic: $topic", Toast.LENGTH_SHORT).show()
            }
        ))
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun handleClick(view: View) {
        mqttBrokerConnection?.publishMessage("foo/temp","aNumber")
    }

}
