package com.monnl.simpleapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.monnl.simpleapp.ui.theme.SimpleappTheme

class SquaredActivity : ComponentActivity() {
    private var squaredCounter: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(this.javaClass.simpleName, "onCreate()")
        super.onCreate(savedInstanceState)
        readExtras()
        setContent {
            SimpleappTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SquaredCounterScreen(squaredValue = squaredCounter)
                }
            }
        }
    }

    private fun readExtras() = intent.extras?.run {
        val counter = getInt(STATE_COUNTER)
        squaredCounter = counter * counter
    }

    override fun onStart() {
        Log.i(this.javaClass.simpleName, "onStart()")
        super.onStart()
    }

    override fun onResume() {
        Log.i(this.javaClass.simpleName, "onResume()")
        super.onResume()
    }

    override fun onPause() {
        Log.i(this.javaClass.simpleName, "onPause()")
        super.onPause()
    }

    override fun onStop() {
        Log.i(this.javaClass.simpleName, "onStop()")
        super.onStop()
    }

    override fun onDestroy() {
        Log.i(this.javaClass.simpleName, "onDestroy()")
        super.onDestroy()
    }

    companion object {
        const val STATE_COUNTER = "state_counter"
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SquaredCounterScreen(squaredValue: Int) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Squared screen")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
                )
            )
        },
        content = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                SquaredCounter(value = squaredValue)
            }
        }
    )
}

@Composable
fun SquaredCounter(value: Int) {
    Text(text = "This is squared counter: $value")
}
