# Contributing to Another Smart Voice Journal

Thank you for your interest in contributing to this educational project! This is a learning project focused on AI/ML integration in mobile applications.

## Project Goals

This project aims to demonstrate:
- **Privacy-conscious AI integration** using on-device processing
- **Offline-first architecture** for data privacy
- **Modern Android development practices** with Clean Architecture
- **Clean, maintainable code** following best practices

## Development Setup

### Prerequisites
- Android Studio Hedgehog or later
- Android SDK 34
- Java 21
- Git

### Getting Started
1. Fork the repository
2. Clone your fork locally
3. Open the project in Android Studio
4. Sync Gradle files
5. Run the tests to ensure everything works

## Code Style & Standards

### Kotlin
- Follow [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use meaningful variable and function names
- Prefer expression bodies for simple functions
- Use data classes for models

### Architecture
- Follow Clean Architecture principles
- Keep modules loosely coupled
- Use dependency injection with Dagger Hilt
- Implement MVVM pattern in presentation layer

### Testing
- Write unit tests for business logic
- Write integration tests for data layer
- Use MockK for mocking in tests
- Aim for high test coverage

## Development Workflow

### Branching Strategy
- `main` - Stable, production-ready code
- `develop` - Integration branch for features
- `feature/*` - Feature branches
- `bugfix/*` - Bug fix branches

### Commit Messages
Use conventional commit format:
```
type(scope): description

[optional body]

[optional footer]
```

Types:
- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation changes
- `style`: Code style changes
- `refactor`: Code refactoring
- `test`: Test changes
- `chore`: Build/tooling changes

### Pull Request Process
1. Create a feature branch from `develop`
2. Make your changes
3. Write/update tests
4. Update documentation if needed
5. Submit a pull request to `develop`
6. Ensure all tests pass
7. Request review from maintainers

## Testing Guidelines

### Unit Tests
- Test business logic in domain layer
- Test repository implementations
- Use MockK for mocking
- Test both success and failure scenarios

### Integration Tests
- Test database interactions
- Test API integrations
- Use in-memory databases for testing

### UI Tests
- Test critical user flows
- Test accessibility features
- Test different screen sizes

## Documentation

### Code Documentation
- Document public APIs
- Use KDoc comments
- Include examples for complex functions

### Architecture Documentation
- Keep README.md updated
- Document architectural decisions
- Update setup instructions

## Issues & Bug Reports

### Reporting Bugs
- Use the issue template
- Include steps to reproduce
- Include device/OS information
- Include logs if applicable

### Feature Requests
- Describe the feature clearly
- Explain the use case
- Consider privacy implications
- Discuss implementation approach

## Privacy & Security

### Privacy-First Approach
- All processing should happen on-device
- Minimize data collection
- Use local storage for user data
- Implement proper data encryption

### Security Considerations
- Follow Android security best practices
- Use secure storage for sensitive data
- Implement proper input validation
- Regular security audits

## Getting Help

- Check existing issues and discussions
- Review the documentation
- Ask questions in issues
- Join the community discussions

## Code of Conduct

- Be respectful and inclusive
- Help others learn and grow
- Provide constructive feedback
- Follow the project's coding standards

Thank you for contributing to this educational project! 🚀 