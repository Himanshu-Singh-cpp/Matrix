# For src/main/cpp/CMakeLists.txt
cmake_minimum_required(VERSION 3.22.1)

project("matrix_operations")

add_library(matrix_operations SHARED
        matrix_operations.cpp)

target_link_libraries(matrix_operations
        android
        log)