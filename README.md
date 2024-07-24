# ForecastNow Application: Bold

## Overview
This project is a Smart ForecastNow Android application built using the MVVM architecture pattern and Kotlin programming language. It leverages RxJava to handle asynchronous events and data streams effectively, ensuring the codebase is reactive and responsive.

## Features
- **MVVM Architecture**: Utilizes Model-View-ViewModel architecture to separate business logic from UI components, promoting maintainability and testability.
- **Kotlin**: Modern programming language used throughout the application for its conciseness and safety features.
- **RxJava**: Integrates RxJava to manage asynchronous operations and data streams in a reactive manner, improving code clarity and responsiveness.
- **Smart ForecastNow**: Provides accurate and real-time weather information based on location using weather APIs.
- **Responsive Design**: Ensures a smooth and intuitive user experience across different devices and screen sizes.

## Components
- **Model**: Contains data classes and business logic responsible for data handling and manipulation.
- **View**: Displays the UI elements to the users and captures user interactions.
- **ViewModel**: Acts as a bridge between the Model and the View, handling the UI-related logic and business logic.

## Dependencies
- **RxJava**: Handles asynchronous tasks and events.
- **LiveData**: Part of Android Architecture Components, used for observing data changes in the ViewModel.
- **Retrofit**: Networking library for making HTTP requests to fetch weather data.
- **Glide**: Image loading library for displaying weather icons and images.

## Getting Started
To run the application locally:
1. Clone the repository from GitHub: [Repository Link](https://github.com/rushikeshty/weather).
2. Open the project in Android Studio.
3. Build and run the application on an Android device or emulator.

## Future Improvements
- Implement caching mechanisms for improved offline usability.
- Enhance UI/UX with animations and transitions.
- Support additional weather APIs for more comprehensive data coverage.

## Contributors
- **Your Name**: Project Lead & Developer
- **Contributor Name**: UI/UX Designer
- **Contributor Name**: QA Tester

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments
- Special thanks to [API Provider](https://weatherapi.com/) for providing weather data.
- Hat tip to the open-source community for the libraries and tools used.
