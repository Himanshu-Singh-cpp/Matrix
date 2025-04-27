package com.example.matrix

object MatrixNative {
    init {
        System.loadLibrary("matrix_operations")
    }

    // Native methods declarations
    external fun addMatrices(matrixA: DoubleArray, rowsA: Int, colsA: Int,
                             matrixB: DoubleArray, rowsB: Int, colsB: Int): DoubleArray

    external fun subtractMatrices(matrixA: DoubleArray, rowsA: Int, colsA: Int,
                                  matrixB: DoubleArray, rowsB: Int, colsB: Int): DoubleArray

    external fun multiplyMatrices(matrixA: DoubleArray, rowsA: Int, colsA: Int,
                                  matrixB: DoubleArray, rowsB: Int, colsB: Int): DoubleArray

    external fun inverseMatrix(matrix: DoubleArray, rows: Int, cols: Int): DoubleArray
}