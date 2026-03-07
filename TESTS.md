# Test Suite - Barbershop API

Comprehensive suite of unit and integration tests for the Barbershop API.

## Test Structure

### Service Tests (`service/`)

#### 1. **AuthServiceTest**

- ✅ `testRegisterClientSuccess` - Register a client successfully
- ✅ `testRegisterClientWithExistingCredentials` - Verify conflict when registering existing client
- ✅ `testRegisterClientWithNullCpf` - Validate null CPF
- ✅ `testRegisterClientWithBlankCpf` - Validate empty CPF
- ✅ `testRegisterClientWithShortPassword` - Validate password too short
- ✅ `testRegisterClientWithNullPassword` - Validate null password
- ✅ `testRegisterClientWithNullName` - Validate null name
- ✅ `testRegisterClientWithBlankName` - Validate empty name
- ✅ `testRegisterClientWithNullPhone` - Validate null phone
- ✅ `testRegisterClientWithBlankPhone` - Validate empty phone
- ✅ `testRegisterClientCreatesNewClient` - Create new client if CPF doesn't exist

#### 2. **ClientServiceTest**

- ✅ `testRegisterClientSuccess` - Register client successfully
- ✅ `testGetAllClientsSuccess` - Retrieve all clients with pagination
- ✅ `testGetClientByCpfSuccess` - Search client by CPF
- ✅ `testGetClientByCpfNotFound` - Throw exception when client not found
- ✅ `testDeleteClientByIdSuccess` - Delete client by ID
- ✅ `testDeleteClientByIdNotFound` - Throw exception when deleting non-existent client
- ✅ `testGetClientByIdSuccess` - Search client by ID
- ✅ `testGetClientByIdNotFound` - Throw exception when ID not found
- ✅ `testRegisterClientWithNullCpf` - Validate null CPF in registration
- ✅ `testGetAllClientsEmpty` - Return empty page

#### 3. **BarberServiceTest**

- ✅ `testRegisterBarberSuccess` - Register barber successfully
- ✅ `testRegisterBarberWithExistingCpf` - Verify conflict when registering existing CPF
- ✅ `testRegisterBarberWithNullDTO` - Validate null DTO
- ✅ `testRegisterBarberWithNullName` - Validate null name
- ✅ `testRegisterBarberWithBlankName` - Validate empty name
- ✅ `testRegisterBarberWithNullCpf` - Validate null CPF
- ✅ `testRegisterBarberWithBlankCpf` - Validate empty CPF
- ✅ `testRegisterBarberWithNullUsername` - Validate null username
- ✅ `testRegisterBarberWithBlankUsername` - Validate empty username
- ✅ `testRegisterBarberWithNullPassword` - Validate null password
- ✅ `testRegisterBarberWithShortPassword` - Validate password too short
- ✅ `testRegisterBarberSetsCorrectRole` - Verify if BARBER role is assigned

#### 4. **TokenServiceTest**

- ✅ `testGenerateTokenSuccess` - Generate valid JWT token
- ✅ `testValidateTokenSuccess` - Validate token and extract username
- ✅ `testValidateInvalidToken` - Throw exception for invalid token
- ✅ `testValidateNullToken` - Throw exception for null token
- ✅ `testValidateTamperedToken` - Throw exception for tampered token
- ✅ `testGenerateDifferentTokensForDifferentUsers` - Generate different tokens for different users
- ✅ `testTokenContainsUsername` - Verify if token contains username
- ✅ `testGenerateTokenWithBarberRole` - Generate token for barber
- ✅ `testValidateEmptyToken` - Throw exception for empty token
- ✅ `testTokenContainsCorrectIssuer` - Validate token issuer

#### 5. **AppointmentServiceTest**

- ✅ `testGetAvailableTimeSlotsSuccess` - Return available slots successfully
- ✅ `testGetAvailableTimeSlotsBarbernNotWorking` - Throw exception when barber not working
- ✅ `testGetAvailableTimeSlotsServiceNotFound` - Throw exception when service doesn't exist
- ✅ `testGetAvailableTimeSlotsExcludesBreakTime` - Exclude break time
- ✅ `testGetAvailableTimeSlotsExcludesExistingAppointments` - Exclude existing appointments
- ✅ `testGetAvailableTimeSlotsReturnEmptyList` - Return empty list when fully booked

#### 6. **WorkScheduleServiceTest**

- ✅ `testCreateWorkScheduleSuccess` - Create work schedule successfully
- ✅ `testCreateWorkScheduleBarbernNotFound` - Throw exception when barber doesn't exist
- ✅ `testCreateWorkScheduleAlreadyExists` - Throw exception when schedule already exists
- ✅ `testCreateWorkScheduleInvalidTimes` - Validate invalid times
- ✅ `testCreateWorkScheduleInvalidBreakTimes` - Validate invalid break time
- ✅ `testGetWorkScheduleSuccess` - Retrieve work schedule successfully
- ✅ `testGetWorkScheduleNotFound` - Throw exception when schedule doesn't exist
- ✅ `testGetAllBarberSchedulesSuccess` - List all barber schedules
- ✅ `testGetAllBarberSchedulesEmpty` - Return empty list when no schedules
- ✅ `testDeleteWorkScheduleSuccess` - Delete work schedule successfully
- ✅ `testDeleteWorkScheduleNotFound` - Throw exception when deleting non-existent schedule

#### 7. **ServiceOfferedServiceTest**

- ✅ `testCreateServiceSuccess` - Create service successfully
- ✅ `testCreateServiceAlreadyExists` - Throw exception when service already exists
- ✅ `testCreateServiceWithNullName` - Validate null name
- ✅ `testCreateServiceWithBlankName` - Validate empty name
- ✅ `testCreateServiceWithNullPrice` - Validate null price
- ✅ `testCreateServiceWithNegativePrice` - Validate negative price
- ✅ `testCreateServiceWithNullDuration` - Validate null duration
- ✅ `testCreateServiceWithInvalidDuration` - Validate duration too short
- ✅ `testGetServiceByIdSuccess` - Search service by ID
- ✅ `testGetServiceByIdNotFound` - Throw exception when service doesn't exist
- ✅ `testGetAllServicesSuccess` - List all services
- ✅ `testGetAllServicesEmpty` - Return empty list
- ✅ `testUpdateServiceSuccess` - Update service successfully
- ✅ `testUpdateServiceNotFound` - Throw exception when updating non-existent service
- ✅ `testDeleteServiceSuccess` - Delete service successfully
- ✅ `testDeleteServiceNotFound` - Throw exception when deleting non-existent service

### Controller Tests (`controller/`)

#### 1. **AuthControllerTest**

- ✅ `testRegisterClientSuccess` - Register client via API
- ✅ `testRegisterClientWithInvalidCpf` - Validate invalid CPF in request
- ✅ `testRegisterClientWithInvalidPassword` - Validate invalid password in request
- ✅ `testLoginSuccess` - Successful user login

#### 2. **ClientControllerTest**

- ✅ `testGetAllClientsSuccess` - List clients with pagination
- ✅ `testGetClientByCpfSuccess` - Search client by CPF via API
- ✅ `testGetClientByCpfNotFound` - Return 404 when client doesn't exist
- ✅ `testGetClientByIdSuccess` - Search client by ID via API
- ✅ `testGetClientByIdNotFound` - Return 404 when ID doesn't exist
- ✅ `testDeleteClientSuccess` - Delete client via API
- ✅ `testDeleteClientNotFound` - Return 404 when deleting non-existent client

#### 3. **BarberControllerTest**

- ✅ `testRegisterBarberSuccess` - Register barber via API
- ✅ `testRegisterBarberWithInvalidData` - Validate invalid data
- ✅ `testRegisterBarberWithExistingCpf` - Return 409 for duplicate CPF

#### 4. **ServiceOfferedControllerTest**

- ✅ `testCreateServiceSuccess` - Create service via API
- ✅ `testGetAllServicesSuccess` - List services via API
- ✅ `testGetServiceByIdSuccess` - Search service by ID
- ✅ `testGetServiceByIdNotFound` - Return 404 when doesn't exist
- ✅ `testUpdateServiceSuccess` - Update service via API
- ✅ `testDeleteServiceSuccess` - Delete service via API
- ✅ `testDeleteServiceNotFound` - Return 404 when deleting non-existent service

### Configuration Tests (`config/`)

#### 1. **CustomUserDetailsServiceTest**

- ✅ `testLoadUserByUsernameSuccess` - Load user by username
- ✅ `testLoadUserByUsernameNotFound` - Throw exception when user doesn't exist
- ✅ `testLoadBarberUserSuccess` - Load barber user with correct role

### Exception Tests (`exception/`)

#### 1. **ExceptionClassesTest**

- ✅ `testValidationException` - Test ValidationException
- ✅ `testConflictException` - Test ConflictException
- ✅ `testResourceNotFoundException` - Test ResourceNotFoundException
- ✅ `testUnauthorizedException` - Test UnauthorizedException
- ✅ `testBadRequestException` - Test BadRequestException
- ✅ `testErrorResponse` - Test ErrorResponse class

## Running the Tests

### Run all tests:

```bash
mvn test
```

### Run specific tests:

```bash
# Service tests
mvn test -Dtest=AuthServiceTest

# Controller tests
mvn test -Dtest=AuthControllerTest

# Specific class tests
mvn test -Dtest=ClientServiceTest
```

### Run with code coverage:

```bash
mvn test jacoco:report
```

## Test Coverage

- **Total Tests:** 107+
- **Services:** 7 classes with comprehensive tests
- **Controllers:** 4 classes with integration tests
- **Configuration:** 1 class of tests
- **Exceptions:** 1 class of tests

## Patterns Used

### Frameworks

- **JUnit 5** - Testing framework
- **Mockito** - Dependency mocking
- **Spring Boot Test** - Spring integration
- **MockMvc** - Controller testing

### Conventions

- Test name: `test + MethodName + Scenario`
- Structure: Arrange-Act-Assert
- Parameterized tests for multiple scenarios
- Input and output validations

**Date:** March 7, 2026
**Version:** 1.0
