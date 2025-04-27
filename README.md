# Vector Operations Android App

An Android application that demonstrates the integration of C++ native code with Kotlin/Java for vector operations.

## Project Overview

This application showcases:
- Android UI with interactive elements
- User input handling for vector operations
- C++ library implementation for efficient vector calculations
- JNI (Java Native Interface) integration between Kotlin/Java and C++

## Features

### Activity with UI (10 marks)
- Modern Material Design interface
- Result visualization
- Operation selection controls
- Vector input fields

### Input Interface (10 marks)
- Vector element input fields
- Operation selection dropdown
- Input validation
- Error handling and feedback

### C++ Vector Library (5 marks)
- Custom C++ implementation for vector operations:
  - Addition/subtraction
  - Dot/cross products
  - Scalar multiplication
  - Normalization
  - Magnitude calculation

### Native Code Integration (15 marks)
- JNI implementation for C++/Kotlin communication
- Efficient data transfer between languages
- Error handling across language boundary
- Performance optimization for vector calculations

## Setup Instructions

1. Clone the repository
2. Open in Android Studio
3. Sync Gradle files
4. Build the native code with CMake
5. Run the application on an emulator or physical device

## Project Structure

```
app/
├── src/main/
│   ├── java/       # Kotlin/Java code
│   ├── cpp/        # Native C++ implementation
│   │   ├── vector_ops.cpp
│   │   └── vector_ops.h
│   ├── res/        # UI resources
│   └── AndroidManifest.xml
├── CMakeLists.txt  # Native build configuration
└── build.gradle    # App build configuration
```

## Implementation Details

- Uses Android NDK for native code compilation
- JNI bindings to expose C++ functionality to Kotlin/Java
- Demonstrates performance advantages of C++ for mathematical operations
- Shows proper memory management between Java and native environments
