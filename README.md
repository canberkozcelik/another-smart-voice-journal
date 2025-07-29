# Another Smart Voice Journal

A privacy-focused voice journal application that leverages on-device AI to automatically summarize voice recordings, providing users with intelligent insights from their personal reflections without requiring internet connectivity.

## Features

### MVP Features
- **Voice Recording**: High-quality audio capture with real-time visualization
- **Speech-to-Text**: On-device speech recognition with multi-language support
- **AI Summarization**: GenAI-powered text summarization using ML Kit
- **Offline-First**: All core functionality works without internet connection

### Technical Highlights
- **Privacy-First**: All processing happens on-device
- **Modern Architecture**: MVVM with Clean Architecture principles
- **Multi-Module**: Scalable modular project structure
- **Material 3**: Modern UI with Material Design 3

## Tech Stack

- **UI Framework**: Jetpack Compose
- **Dependency Injection**: Dagger Hilt
- **Concurrency**: Kotlin Coroutines + Flow
- **Architecture**: MVVM with Clean Architecture
- **Database**: Room for local storage
- **Audio**: MediaRecorder + AudioManager
- **AI**: ML Kit GenAI Summarization API
- **Speech Recognition**: Google Speech Recognition API
- **Testing**: MockK for mocking, JUnit for unit tests
- **Build System**: Gradle with AGP 8.11.1

## Project Structure

```
app/
├── :app (Main Application Module)
├── :core:ui (Shared UI Components)
├── :core:common (Shared Utilities)
├── :core:data (Data Layer)
├── :core:domain (Domain Layer)
├── :feature:recording (Voice Recording Feature)
├── :feature:transcription (Speech-to-Text Feature)
├── :feature:summarization (AI Summarization Feature)
├── :feature:journal (Journal Management Feature)
└── :feature:settings (App Settings Feature)
```

## Getting Started

### Prerequisites
- Android Studio Hedgehog or later
- Android SDK 34
- Minimum SDK: 26 (Android 8.0)
- Java 21 (recommended for modern Android development)

### Setup
1. Clone the repository
2. Open the project in Android Studio
3. Sync Gradle files
4. Build and run the project

### Build Configuration
- **Java Version**: 21 (latest LTS for modern Android development)
- **Android Gradle Plugin**: 8.11.1
- **Gradle Version**: 8.13
- **Kotlin Version**: 1.9.22
- **Target SDK**: 34
- **Minimum SDK**: 26 (Android 8.0)

## Architecture

### Clean Architecture Layers
- **Presentation Layer**: Compose UI components and ViewModels
- **Domain Layer**: Business logic and use cases
- **Data Layer**: Repository implementations and data sources

### Module Dependencies
- Feature modules depend on core modules
- Core modules are independent of each other
- App module orchestrates all features

## Key Components

### Voice Recording
- High-quality audio capture (44.1 kHz, 16-bit, Mono)
- Real-time waveform visualization
- Background recording support
- Pause/resume functionality

### Speech Recognition
- On-device speech recognition
- Multi-language support (English, Japanese, Korean)
- Real-time transcription display
- Confidence scoring

### AI Summarization
- ML Kit GenAI Summarization API
- Support for article and conversation input types
- 1-3 bullet point summary formats
- Automatic model management

## Development Phases

### Phase 1: Foundation (Week 1-2)
- [x] Project setup with multi-module architecture
- [x] Build configuration with Java 21 and AGP 8.11.1
- [ ] Basic UI framework with Compose
- [x] Dependency injection setup
- [x] Database schema and repository pattern
- [x] Unit and integration testing setup with MockK

### Phase 2: Core Features (Week 3-4)
- [ ] Voice recording implementation
- [ ] Speech-to-text integration
- [ ] Basic journal entry management

### Phase 3: AI Integration (Week 5-6)
- [ ] GenAI Summarization API integration
- [ ] Model management and error handling
- [ ] Summary generation and display

### Phase 4: Polish & Testing (Week 7-8)
- [ ] UI/UX refinement
- [ ] Performance optimization
- [ ] Comprehensive testing
- [ ] Bug fixes and final polish

## Contributing

This is a learning project for AI/ML applications in mobile development. The focus is on:

1. **Privacy-conscious AI integration**
2. **Offline-first architecture**
3. **Modern Android development practices**
4. **Clean, maintainable code**

## License

This project is for educational purposes and learning AI/ML integration in mobile applications.

## Acknowledgments

- [ML Kit GenAI Summarization API](https://developers.google.com/ml-kit/genai/summarization/android)
- Jetpack Compose for modern UI development
- Material Design 3 for design system 