package com.example.matrix

data class Matrix(val rows: Int, val cols: Int, val data: List<List<Double>>) {
    companion object {
        // Create a matrix from a flat list of values
        fun createFromFlatList(rows: Int, cols: Int, values: List<Double>): Matrix {
            require(values.size == rows * cols) { "Invalid number of values for $rows x $cols matrix" }
            val data = List(rows) { r ->
                List(cols) { c ->
                    values[r * cols + c]
                }
            }
            return Matrix(rows, cols, data)
        }
    }

    // Convert Matrix to flat DoubleArray for native code
    private fun toFlatDoubleArray(): DoubleArray {
        val result = DoubleArray(rows * cols)
        for (r in 0 until rows) {
            for (c in 0 until cols) {
                result[r * cols + c] = data[r][c]
            }
        }
        return result
    }

    // Convert flat DoubleArray back to Matrix
    private fun fromFlatDoubleArray(flatData: DoubleArray, rows: Int, cols: Int): Matrix {
        val data = List(rows) { r ->
            List(cols) { c ->
                flatData[r * cols + c]
            }
        }
        return Matrix(rows, cols, data)
    }

    // Check if dimensions match for addition/subtraction
    fun hasSameDimensions(other: Matrix): Boolean {
        return rows == other.rows && cols == other.cols
    }

    // Check if matrices can be multiplied
    fun canMultiply(other: Matrix): Boolean {
        return cols == other.rows
    }

    // Addition using native code
    operator fun plus(other: Matrix): Matrix {
        require(hasSameDimensions(other)) { "Cannot add matrices with different dimensions" }

        val flatA = this.toFlatDoubleArray()
        val flatB = other.toFlatDoubleArray()

        val flatResult = MatrixNative.addMatrices(flatA, rows, cols, flatB, other.rows, other.cols)
        return fromFlatDoubleArray(flatResult, rows, cols)
    }

    // Subtraction using native code
    operator fun minus(other: Matrix): Matrix {
        require(hasSameDimensions(other)) { "Cannot subtract matrices with different dimensions" }

        val flatA = this.toFlatDoubleArray()
        val flatB = other.toFlatDoubleArray()

        val flatResult = MatrixNative.subtractMatrices(flatA, rows, cols, flatB, other.rows, other.cols)
        return fromFlatDoubleArray(flatResult, rows, cols)
    }

    // Multiplication using native code
    operator fun times(other: Matrix): Matrix {
        require(canMultiply(other)) { "Cannot multiply matrices with incompatible dimensions" }

        val flatA = this.toFlatDoubleArray()
        val flatB = other.toFlatDoubleArray()

        val flatResult = MatrixNative.multiplyMatrices(flatA, rows, cols, flatB, other.rows, other.cols)
        return fromFlatDoubleArray(flatResult, rows, other.cols)
    }

    // Division using native code (via inverse)
    operator fun div(other: Matrix): Matrix {
        require(other.rows == other.cols) { "Can only divide by a square matrix" }
        val inverse = other.inverse()
        return this * inverse
    }

    // Matrix inverse using native code
    fun inverse(): Matrix {
        require(rows == cols) { "Only square matrices can be inverted" }

        val flatMatrix = this.toFlatDoubleArray()
        val flatResult = MatrixNative.inverseMatrix(flatMatrix, rows, cols)
        return fromFlatDoubleArray(flatResult, rows, cols)
    }

    override fun toString(): String {
        return data.joinToString("\n") { row ->
            row.joinToString(" ") { "%.2f".format(it) }
        }
    }
}