# FSAMarsWorld

FSAMarsWorld is a Java Spring Boot application for simulating and calculating prime system aspects, designed for Mars World scenarios. It provides RESTful APIs for prime calculations and related operations.

## Project Structure

- `PrimeSystem/FSAMarsWorld/` - Main application directory
  - `src/main/java/com/btbk/primesystem/` - Java source code
    - `FsaMarsWorldApplication.java` - Main entry point
    - `api/PrimeController.java` - REST API controller for prime calculations
    - `engine/PrimeCalculator.java`, `PrimeEngine.java` - Core calculation logic
    - `model/` - Data models (e.g., Aspect, Attributes, ComputeResult)
    - `config/EngineConfig.java` - Configuration classes
  - `src/main/resources/` - Application resources
    - `application.properties`, `application.yml` - Configuration files
    - `static/index.html` - Static web resources
  - `src/test/java/com/btbk/primesystem/` - Unit and integration tests
    - `FsaMarsWorldApplicationTests.java` - Main test class
    - `api/PrimeControllerGatingTests.java` - API tests
  - `pom.xml` - Maven build configuration

## How to Build and Run

1. **Build the project:**
   ```sh
   ./mvnw clean install
   ```
2. **Run the application:**
   ```sh
   ./mvnw spring-boot:run
   ```
   The application will start on the default port (usually 8080).

## API Usage

- The main API endpoint is exposed via `PrimeController`.
- Example endpoint: `/api/prime/calculate` (see controller for details).
- Send requests with appropriate JSON payloads as defined in the model classes.

## Testing

Run all tests with:
```sh
./mvnw test
```

## License
See `LICENSE` and `PrimeSystem/FSAMarsWorld/LICENSE.txt` for details.

## Contributing
Pull requests and issues are welcome. Please follow standard Java and Spring Boot best practices.
