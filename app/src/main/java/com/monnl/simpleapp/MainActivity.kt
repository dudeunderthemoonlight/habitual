package com.monnl.simpleapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.monnl.simpleapp.ui.theme.SimpleappTheme

class MainActivity : ComponentActivity() {
    private var counter = mutableStateOf(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(this.javaClass.simpleName, "onCreate()")
        super.onCreate(savedInstanceState)
        setContent {
            SimpleappTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CounterScreen(counter)
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        counter.value++
        outState.putInt(STATE_COUNTER, counter.value)
        Log.i(this.javaClass.simpleName, "Counter is increased and saved: ${counter.value}")
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        counter.value = savedInstanceState.getInt(STATE_COUNTER)
        Log.i(this.javaClass.simpleName, "Counter is restored: ${counter.value}")
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
fun CounterScreen(counterState: MutableState<Int>) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Counter screen")
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
                Counter(counterState)
                Button(onClick = {
                    val intent = Intent(context, SquaredActivity::class.java)
                    intent.putExtra(MainActivity.STATE_COUNTER, counterState.value)
                    context.startActivity(intent)
                }) {
                    Text("Square it!")
                }
            }
        }
    )
}

@Composable
fun Counter(counterState: MutableState<Int>) {
    val counter: MutableState<Int> = remember { counterState }
    Text(text = "Current count is ${counter.value}")
}

