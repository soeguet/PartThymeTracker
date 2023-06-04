# PartThymeTracker

PartThymeTracker is a comprehensive tool to track and manage working hours for individuals. The primary interface is through an NFC card system that communicates with a server via HTTP POST requests to log hours worked. There is also a web interface for manual entries and overall management. It leverages the power of Spring Boot for the back end and PostgreSQL running on Docker for data persistence.

![img.png](img.png)

## Features
1. **NFC Card System**: Each user should be assigned a NFC card with a unique code saved on it. By scanning this card at an NFC-capable device, a HTTP POST request should be sent to the server, logging the timestamp.
2. **Automatic Login/Logout**: The system automatically determines whether the user is logging in or out based on their last action. This simplifies the process for users - all they need to do is scan their card. Audio feedback possible.
3. **Work Duration Feedback**: Upon logout, the server calculates the total work duration for the session and sends this information back. If the NFC device has audio capabilities, this information can be read out loud to the user.
4. **Web Interface**: In addition to the NFC system, a web interface is available for manual entry and management of working hours.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

- JDK 17
- Gradle 8
- Spring Boot 3.1
- Docker
- An NFC-capable device for testing or any other HTTP client (e.g. Postman)

### Installation

1. Clone the repository to your local machine
```bash
git clone https://github.com/soeguet/PartThymeTracker.git
```
2. Navigate to the project directory
```bash
cd PartThymeTracker
```
3. Build the Docker image for PostgreSQL
```bash
./gradlew bootRun
```

Spring Boot 3.1 will start up the docker-compose.yml and start up the PostgreSQL database. The server will be running at [http://localhost:8080](http://localhost:8080).

## License

[MIT](https://choosealicense.com/licenses/mit/)

## Contact

If you have any questions, feel free to reach out.

Enjoy tracking with PartThymeTracker!
