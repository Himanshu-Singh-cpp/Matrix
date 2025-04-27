package com.example.matrix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.matrix.ui.theme.MatrixTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MatrixTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MatrixCalculator(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MatrixCalculator(modifier: Modifier = Modifier) {
    var rowsA by remember { mutableStateOf("2") }
    var colsA by remember { mutableStateOf("2") }
    var rowsB by remember { mutableStateOf("2") }
    var colsB by remember { mutableStateOf("2") }

    var matrixAValues by remember { mutableStateOf(List(4) { "0.0" }) }
    var matrixBValues by remember { mutableStateOf(List(4) { "0.0" }) }

    var result by remember { mutableStateOf<Matrix?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text("Matrix Calculator", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        // Matrix A Dimensions
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Matrix A dimensions:", modifier = Modifier.width(150.dp))
            TextField(
                value = rowsA,
                onValueChange = {
                    rowsA = it
                    if (it.isNotEmpty() && colsA.isNotEmpty()) {
                        val rows = it.toIntOrNull() ?: 1
                        val cols = colsA.toIntOrNull() ?: 1
                        matrixAValues = List(rows * cols) { "0.0" }
                    }
                },
                modifier = Modifier.width(60.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Text(" × ", modifier = Modifier.padding(horizontal = 8.dp))
            TextField(
                value = colsA,
                onValueChange = {
                    colsA = it
                    if (rowsA.isNotEmpty() && it.isNotEmpty()) {
                        val rows = rowsA.toIntOrNull() ?: 1
                        val cols = it.toIntOrNull() ?: 1
                        matrixAValues = List(rows * cols) { "0.0" }
                    }
                },
                modifier = Modifier.width(60.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        // Matrix B Dimensions
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Matrix B dimensions:", modifier = Modifier.width(150.dp))
            TextField(
                value = rowsB,
                onValueChange = {
                    rowsB = it
                    if (it.isNotEmpty() && colsB.isNotEmpty()) {
                        val rows = it.toIntOrNull() ?: 1
                        val cols = colsB.toIntOrNull() ?: 1
                        matrixBValues = List(rows * cols) { "0.0" }
                    }
                },
                modifier = Modifier.width(60.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Text(" × ", modifier = Modifier.padding(horizontal = 8.dp))
            TextField(
                value = colsB,
                onValueChange = {
                    colsB = it
                    if (rowsB.isNotEmpty() && it.isNotEmpty()) {
                        val rows = rowsB.toIntOrNull() ?: 1
                        val cols = it.toIntOrNull() ?: 1
                        matrixBValues = List(rows * cols) { "0.0" }
                    }
                },
                modifier = Modifier.width(60.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Matrix A Input
        Text("Matrix A:", style = MaterialTheme.typography.titleMedium)
        MatrixInput(
            rows = rowsA.toIntOrNull() ?: 2,
            cols = colsA.toIntOrNull() ?: 2,
            values = matrixAValues,
            onValueChange = { index, value ->
                val newList = matrixAValues.toMutableList()
                newList[index] = value
                matrixAValues = newList
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Matrix B Input
        Text("Matrix B:", style = MaterialTheme.typography.titleMedium)
        MatrixInput(
            rows = rowsB.toIntOrNull() ?: 2,
            cols = colsB.toIntOrNull() ?: 2,
            values = matrixBValues,
            onValueChange = { index, value ->
                val newList = matrixBValues.toMutableList()
                newList[index] = value
                matrixBValues = newList
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Operation Buttons
        // Operation Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                val (newResult, newError) = performOperation(rowsA, colsA, rowsB, colsB, matrixAValues, matrixBValues) { a, b -> a + b }
                result = newResult
                errorMessage = newError
            }) {
                Text("Add")
            }
            Button(onClick = {
                val (newResult, newError) = performOperation(rowsA, colsA, rowsB, colsB, matrixAValues, matrixBValues) { a, b -> a - b }
                result = newResult
                errorMessage = newError
            }) {
                Text("Subtract")
            }
            Button(onClick = {
                val (newResult, newError) = performOperation(rowsA, colsA, rowsB, colsB, matrixAValues, matrixBValues) { a, b -> a * b }
                result = newResult
                errorMessage = newError
            }) {
                Text("Multiply")
            }
            Button(onClick = {
                val (newResult, newError) = performOperation(rowsA, colsA, rowsB, colsB, matrixAValues, matrixBValues) { a, b -> a / b }
                result = newResult
                errorMessage = newError
            }) {
                Text("Divide")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Result
        errorMessage?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        result?.let {
            Text("Result:", style = MaterialTheme.typography.titleMedium)
            Text(it.toString())
        }
    }
}

@Composable
fun MatrixInput(
    rows: Int,
    cols: Int,
    values: List<String>,
    onValueChange: (Int, String) -> Unit
) {
    Column {
        for (r in 0 until rows) {
            Row {
                for (c in 0 until cols) {
                    val index = r * cols + c
                    TextField(
                        value = if (index < values.size) values[index] else "0.0",
                        onValueChange = { onValueChange(index, it) },
                        modifier = Modifier
                            .width(80.dp)
                            .padding(2.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                    )
                }
            }
        }
    }
}

// Convert from @Composable to regular function
fun performOperation(
    rowsA: String,
    colsA: String,
    rowsB: String,
    colsB: String,
    matrixAValues: List<String>,
    matrixBValues: List<String>,
    operation: (Matrix, Matrix) -> Matrix
): Pair<Matrix?, String?> {
    return try {
        val rA = rowsA.toIntOrNull() ?: throw NumberFormatException("Invalid rows for Matrix A")
        val cA = colsA.toIntOrNull() ?: throw NumberFormatException("Invalid columns for Matrix A")
        val rB = rowsB.toIntOrNull() ?: throw NumberFormatException("Invalid rows for Matrix B")
        val cB = colsB.toIntOrNull() ?: throw NumberFormatException("Invalid columns for Matrix B")

        val valuesA = matrixAValues.map { it.toDoubleOrNull() ?: 0.0 }
        val valuesB = matrixBValues.map { it.toDoubleOrNull() ?: 0.0 }

        val matrixA = Matrix.createFromFlatList(rA, cA, valuesA)
        val matrixB = Matrix.createFromFlatList(rB, cB, valuesB)

        Pair(operation(matrixA, matrixB), null) // Return result with no error
    } catch (e: Exception) {
        Pair(null, "Error: ${e.message}") // Return error with no result
    }
}