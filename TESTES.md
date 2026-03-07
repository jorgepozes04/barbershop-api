# Bateria de Testes - Barbershop API

Bateria completa de testes unitários e de integração para a API de Barbershop.

## Estrutura de Testes

### Testes de Serviço (`service/`)

#### 1. **AuthServiceTest**

- ✅ `testRegisterClientSuccess` - Registra um cliente com sucesso
- ✅ `testRegisterClientWithExistingCredentials` - Verifica conflito ao registrar cliente existente
- ✅ `testRegisterClientWithNullCpf` - Valida CPF nulo
- ✅ `testRegisterClientWithBlankCpf` - Valida CPF vazio
- ✅ `testRegisterClientWithShortPassword` - Valida senha muito curta
- ✅ `testRegisterClientWithNullPassword` - Valida senha nula
- ✅ `testRegisterClientWithNullName` - Valida nome nulo
- ✅ `testRegisterClientWithBlankName` - Valida nome vazio
- ✅ `testRegisterClientWithNullPhone` - Valida telefone nulo
- ✅ `testRegisterClientWithBlankPhone` - Valida telefone vazio
- ✅ `testRegisterClientCreatesNewClient` - Cria novo cliente se CPF não existe

#### 2. **ClientServiceTest**

- ✅ `testRegisterClientSuccess` - Registra cliente com sucesso
- ✅ `testGetAllClientsSuccess` - Recupera todos os clientes com paginação
- ✅ `testGetClientByCpfSuccess` - Busca cliente por CPF
- ✅ `testGetClientByCpfNotFound` - Lança exceção quando cliente não encontrado
- ✅ `testDeleteClientByIdSuccess` - Deleta cliente por ID
- ✅ `testDeleteClientByIdNotFound` - Lança exceção ao deletar cliente inexistente
- ✅ `testGetClientByIdSuccess` - Busca cliente por ID
- ✅ `testGetClientByIdNotFound` - Lança exceção quando ID não encontrado
- ✅ `testRegisterClientWithNullCpf` - Valida CPF nulo no registro
- ✅ `testGetAllClientsEmpty` - Retorna página vazia

#### 3. **BarberServiceTest**

- ✅ `testRegisterBarberSuccess` - Registra barbeiro com sucesso
- ✅ `testRegisterBarberWithExistingCpf` - Verifica conflito ao registrar CPF existente
- ✅ `testRegisterBarberWithNullDTO` - Valida DTO nulo
- ✅ `testRegisterBarberWithNullName` - Valida nome nulo
- ✅ `testRegisterBarberWithBlankName` - Valida nome vazio
- ✅ `testRegisterBarberWithNullCpf` - Valida CPF nulo
- ✅ `testRegisterBarberWithBlankCpf` - Valida CPF vazio
- ✅ `testRegisterBarberWithNullUsername` - Valida usuário nulo
- ✅ `testRegisterBarberWithBlankUsername` - Valida usuário vazio
- ✅ `testRegisterBarberWithNullPassword` - Valida senha nula
- ✅ `testRegisterBarberWithShortPassword` - Valida senha muito curta
- ✅ `testRegisterBarberSetsCorrectRole` - Verifica se role BARBER é atribuído

#### 4. **TokenServiceTest**

- ✅ `testGenerateTokenSuccess` - Gera token JWT válido
- ✅ `testValidateTokenSuccess` - Valida token e extrai username
- ✅ `testValidateInvalidToken` - Lança exceção para token inválido
- ✅ `testValidateNullToken` - Lança exceção para token nulo
- ✅ `testValidateTamperedToken` - Lança exceção para token alterado
- ✅ `testGenerateDifferentTokensForDifferentUsers` - Gera tokens diferentes para usuários diferentes
- ✅ `testTokenContainsUsername` - Verifica se token contém username
- ✅ `testGenerateTokenWithBarberRole` - Gera token para barbeiro
- ✅ `testValidateEmptyToken` - Lança exceção para token vazio
- ✅ `testTokenContainsCorrectIssuer` - Valida issuer do token

#### 5. **AppointmentServiceTest**

- ✅ `testGetAvailableTimeSlotsSuccess` - Retorna slots disponíveis com sucesso
- ✅ `testGetAvailableTimeSlotsBarbernNotWorking` - Lança exceção quando barbeiro não trabalha
- ✅ `testGetAvailableTimeSlotsServiceNotFound` - Lança exceção quando serviço não existe
- ✅ `testGetAvailableTimeSlotsExcludesBreakTime` - Exclui horário de intervalo
- ✅ `testGetAvailableTimeSlotsExcludesExistingAppointments` - Exclui compromissos existentes
- ✅ `testGetAvailableTimeSlotsReturnEmptyList` - Retorna lista vazia quando lotado

#### 6. **WorkScheduleServiceTest**

- ✅ `testCreateWorkScheduleSuccess` - Cria cronograma com sucesso
- ✅ `testCreateWorkScheduleBarbernNotFound` - Lança exceção quando barbeiro não existe
- ✅ `testCreateWorkScheduleAlreadyExists` - Lança exceção quando cronograma já existe
- ✅ `testCreateWorkScheduleInvalidTimes` - Valida horários inválidos
- ✅ `testCreateWorkScheduleInvalidBreakTimes` - Valida intervalo inválido
- ✅ `testGetWorkScheduleSuccess` - Recupera cronograma com sucesso
- ✅ `testGetWorkScheduleNotFound` - Lança exceção quando cronograma não existe
- ✅ `testGetAllBarberSchedulesSuccess` - Lista todos os cronogramas do barbeiro
- ✅ `testGetAllBarberSchedulesEmpty` - Retorna lista vazia quando sem cronogramas
- ✅ `testDeleteWorkScheduleSuccess` - Deleta cronograma com sucesso
- ✅ `testDeleteWorkScheduleNotFound` - Lança exceção ao deletar cronograma inexistente

#### 7. **ServiceOfferedServiceTest**

- ✅ `testCreateServiceSuccess` - Cria serviço com sucesso
- ✅ `testCreateServiceAlreadyExists` - Lança exceção quando serviço já existe
- ✅ `testCreateServiceWithNullName` - Valida nome nulo
- ✅ `testCreateServiceWithBlankName` - Valida nome vazio
- ✅ `testCreateServiceWithNullPrice` - Valida preço nulo
- ✅ `testCreateServiceWithNegativePrice` - Valida preço negativo
- ✅ `testCreateServiceWithNullDuration` - Valida duração nula
- ✅ `testCreateServiceWithInvalidDuration` - Valida duração muito curta
- ✅ `testGetServiceByIdSuccess` - Busca serviço por ID
- ✅ `testGetServiceByIdNotFound` - Lança exceção quando serviço não existe
- ✅ `testGetAllServicesSuccess` - Lista todos os serviços
- ✅ `testGetAllServicesEmpty` - Retorna lista vazia
- ✅ `testUpdateServiceSuccess` - Atualiza serviço com sucesso
- ✅ `testUpdateServiceNotFound` - Lança exceção ao atualizar inexistente
- ✅ `testDeleteServiceSuccess` - Deleta serviço com sucesso
- ✅ `testDeleteServiceNotFound` - Lança exceção ao deletar inexistente

### Testes de Controller (`controller/`)

#### 1. **AuthControllerTest**

- ✅ `testRegisterClientSuccess` - Registra cliente via API
- ✅ `testRegisterClientWithInvalidCpf` - Valida CPF inválido na requisição
- ✅ `testRegisterClientWithInvalidPassword` - Valida senha inválida na requisição
- ✅ `testLoginSuccess` - Login de usuário bem-sucedido

#### 2. **ClientControllerTest**

- ✅ `testGetAllClientsSuccess` - Lista clientes com paginação
- ✅ `testGetClientByCpfSuccess` - Busca cliente por CPF via API
- ✅ `testGetClientByCpfNotFound` - Retorna 404 quando cliente não existe
- ✅ `testGetClientByIdSuccess` - Busca cliente por ID via API
- ✅ `testGetClientByIdNotFound` - Retorna 404 quando ID não existe
- ✅ `testDeleteClientSuccess` - Deleta cliente via API
- ✅ `testDeleteClientNotFound` - Retorna 404 ao deletar inexistente

#### 3. **BarberControllerTest**

- ✅ `testRegisterBarberSuccess` - Registra barbeiro via API
- ✅ `testRegisterBarberWithInvalidData` - Valida dados inválidos
- ✅ `testRegisterBarberWithExistingCpf` - Retorna 409 para CPF duplicado

#### 4. **ServiceOfferedControllerTest**

- ✅ `testCreateServiceSuccess` - Cria serviço via API
- ✅ `testGetAllServicesSuccess` - Lista serviços via API
- ✅ `testGetServiceByIdSuccess` - Busca serviço por ID
- ✅ `testGetServiceByIdNotFound` - Retorna 404 quando não existe
- ✅ `testUpdateServiceSuccess` - Atualiza serviço via API
- ✅ `testDeleteServiceSuccess` - Deleta serviço via API
- ✅ `testDeleteServiceNotFound` - Retorna 404 ao deletar inexistente

### Testes de Configuração (`config/`)

#### 1. **CustomUserDetailsServiceTest**

- ✅ `testLoadUserByUsernameSuccess` - Carrega usuário por username
- ✅ `testLoadUserByUsernameNotFound` - Lança exceção quando usuário não existe
- ✅ `testLoadBarberUserSuccess` - Carrega usuário barbeiro com role correto

### Testes de Exceções (`exception/`)

#### 1. **ExceptionClassesTest**

- ✅ `testValidationException` - Testa ValidationException
- ✅ `testConflictException` - Testa ConflictException
- ✅ `testResourceNotFoundException` - Testa ResourceNotFoundException
- ✅ `testUnauthorizedException` - Testa UnauthorizedException
- ✅ `testBadRequestException` - Testa BadRequestException
- ✅ `testErrorResponse` - Testa classe ErrorResponse

## Executar os Testes

### Executar todos os testes:

```bash
mvn test
```

### Executar testes específicos:

```bash
# Testes de serviço
mvn test -Dtest=AuthServiceTest

# Testes de controller
mvn test -Dtest=AuthControllerTest

# Testes de uma classe específica
mvn test -Dtest=ClientServiceTest
```

### Executar com cobertura:

```bash
mvn test jacoco:report
```

## Cobertura de Testes

- **Total de Testes:** 107+
- **Serviços:** 7 classes com testes abrangentes
- **Controllers:** 4 classes com testes de integração
- **Configurações:** 1 classe de teste
- **Exceções:** 1 classe de teste

## Padrões Utilizados

### Frameworks

- **JUnit 5** - Framework de testes
- **Mockito** - Mock de dependências
- **Spring Boot Test** - Integração com Spring
- **MockMvc** - Testes de controllers

### Convenções

- Nome do teste: `test + NomeDoMétodo + Cenário`
- Estrutura: Arrange-Act-Assert
- Testes parametrizados para múltiplos cenários
- Validações de entrada e saída

## Próximos Passos

1. ✅ Criar bateria completa de testes unitários
2. ⬜ Integração contínua (CI/CD)
3. ⬜ Testes de integração E2E
4. ⬜ Testes de performance
5. ⬜ Testes de segurança

---

**Data:** 07 de março de 2026
**Versão:** 1.0
