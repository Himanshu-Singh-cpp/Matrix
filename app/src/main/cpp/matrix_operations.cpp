#include <jni.h>
#include <vector>
#include <stdexcept>
#include <android/log.h>
#include <cmath>

#define LOG_TAG "MatrixNative"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

// Convert JNI double array to C++ 2D vector
std::vector<std::vector<double>> convertToMatrix(JNIEnv *env, jdoubleArray flatData, jint rows, jint cols) {
    std::vector<std::vector<double>> matrix(rows, std::vector<double>(cols, 0.0));
    jdouble *elements = env->GetDoubleArrayElements(flatData, NULL);

    for (int r = 0; r < rows; r++) {
        for (int c = 0; c < cols; c++) {
            matrix[r][c] = elements[r * cols + c];
        }
    }

    env->ReleaseDoubleArrayElements(flatData, elements, JNI_ABORT);
    return matrix;
}

// Convert C++ 2D vector to JNI flat double array
jdoubleArray convertToJNI(JNIEnv *env, const std::vector<std::vector<double>>& matrix) {
    int rows = matrix.size();
    int cols = matrix[0].size();
    jdoubleArray result = env->NewDoubleArray(rows * cols);

    jdouble *elements = new jdouble[rows * cols];
    for (int r = 0; r < rows; r++) {
        for (int c = 0; c < cols; c++) {
            elements[r * cols + c] = matrix[r][c];
        }
    }

    env->SetDoubleArrayRegion(result, 0, rows * cols, elements);
    delete[] elements;

    return result;
}

extern "C" {

JNIEXPORT jdoubleArray JNICALL
Java_com_example_matrix_MatrixNative_addMatrices(JNIEnv *env, jclass clazz,
                                                 jdoubleArray matrixA, jint rowsA, jint colsA,
                                                 jdoubleArray matrixB, jint rowsB, jint colsB) {
    try {
        if (rowsA != rowsB || colsA != colsB) {
            throw std::invalid_argument("Matrices must have the same dimensions for addition");
        }

        auto a = convertToMatrix(env, matrixA, rowsA, colsA);
        auto b = convertToMatrix(env, matrixB, rowsB, colsB);

        std::vector<std::vector<double>> result(rowsA, std::vector<double>(colsA, 0.0));
        for (int r = 0; r < rowsA; r++) {
            for (int c = 0; c < colsA; c++) {
                result[r][c] = a[r][c] + b[r][c];
            }
        }

        return convertToJNI(env, result);
    } catch (const std::exception& e) {
        LOGE("Native exception in addMatrices: %s", e.what());
        return nullptr;
    }
}

JNIEXPORT jdoubleArray JNICALL
Java_com_example_matrix_MatrixNative_subtractMatrices(JNIEnv *env, jclass clazz,
                                                      jdoubleArray matrixA, jint rowsA, jint colsA,
                                                      jdoubleArray matrixB, jint rowsB, jint colsB) {
    try {
        if (rowsA != rowsB || colsA != colsB) {
            throw std::invalid_argument("Matrices must have the same dimensions for subtraction");
        }

        auto a = convertToMatrix(env, matrixA, rowsA, colsA);
        auto b = convertToMatrix(env, matrixB, rowsB, colsB);

        std::vector<std::vector<double>> result(rowsA, std::vector<double>(colsA, 0.0));
        for (int r = 0; r < rowsA; r++) {
            for (int c = 0; c < colsA; c++) {
                result[r][c] = a[r][c] - b[r][c];
            }
        }

        return convertToJNI(env, result);
    } catch (const std::exception& e) {
        LOGE("Native exception in subtractMatrices: %s", e.what());
        return nullptr;
    }
}

JNIEXPORT jdoubleArray JNICALL
Java_com_example_matrix_MatrixNative_multiplyMatrices(JNIEnv *env, jclass clazz,
                                                      jdoubleArray matrixA, jint rowsA, jint colsA,
                                                      jdoubleArray matrixB, jint rowsB, jint colsB) {
    try {
        if (colsA != rowsB) {
            throw std::invalid_argument("Matrix dimensions incompatible for multiplication");
        }

        auto a = convertToMatrix(env, matrixA, rowsA, colsA);
        auto b = convertToMatrix(env, matrixB, rowsB, colsB);

        std::vector<std::vector<double>> result(rowsA, std::vector<double>(colsB, 0.0));
        for (int r = 0; r < rowsA; r++) {
            for (int c = 0; c < colsB; c++) {
                result[r][c] = 0;
                for (int k = 0; k < colsA; k++) {
                    result[r][c] += a[r][k] * b[k][c];
                }
            }
        }

        return convertToJNI(env, result);
    } catch (const std::exception& e) {
        LOGE("Native exception in multiplyMatrices: %s", e.what());
        return nullptr;
    }
}

JNIEXPORT jdoubleArray JNICALL
Java_com_example_matrix_MatrixNative_inverseMatrix(JNIEnv *env, jclass clazz,
                                                   jdoubleArray matrix, jint rows, jint cols) {
    try {
        if (rows != cols) {
            throw std::invalid_argument("Only square matrices can be inverted");
        }

        auto m = convertToMatrix(env, matrix, rows, cols);
        std::vector<std::vector<double>> result;

        // Implement for 2x2
        if (rows == 2) {
            double det = m[0][0] * m[1][1] - m[0][1] * m[1][0];
            if (std::abs(det) < 1e-10) {
                throw std::invalid_argument("Matrix is not invertible (determinant = 0)");
            }

            double invDet = 1.0 / det;
            result = {
                    {m[1][1] * invDet, -m[0][1] * invDet},
                    {-m[1][0] * invDet, m[0][0] * invDet}
            };
        }
        else {
            throw std::invalid_argument("Inverse for matrices larger than 2x2 not implemented in native code");
        }

        return convertToJNI(env, result);
    } catch (const std::exception& e) {
        LOGE("Native exception in inverseMatrix: %s", e.what());
        return nullptr;
    }
}

} // end extern "C"