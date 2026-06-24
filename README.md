# Proyecto Fundamentos01

## 1. Funcionamiento de Spring Boot

![Evidencia Status Funcional](src/main/resources/assets/server_funcional.jpeg)

## 2. Endpoint de Status (`/api/status`)

![Evidencia Status Funcional](src/main/resources/assets/status_funcional.jpeg)

## 3. Endpoints de Estudiantes (`StudentController`)

### Lista de Estudiantes (`/api/students`)

![Evidencia Students Funcional](src/main/resources/assets/students_funcional.jpeg)

### Conteo de Estudiantes (`/api/students/count`)

![Evidencia Count Funcional](src/main/resources/assets/count_funcionamiento.jpeg)

## 4. Endpoints de Productos (`ProductController`)

### Validación de datos con `@Valid` (price, stock y name inválidos)

![Evidencia Validacion de Productos](src/main/resources/assets/error_products_validation.jpeg)

### Reglas de negocio: eliminación lógica (`GET`/`DELETE` sobre producto eliminado)

![Evidencia Validacion de Productos - ERROR UPDATE](src/main/resources/assets/error_get_delete_product.jpeg)

### Endpoint FindAll no agrega los productos eliminados

![Evidencia FindAll sin productos eliminados](src/main/resources/assets/delete_findall_evidence.jpeg)

## Conclusión
Esta implementación demuestra
la eficiencia de Spring Boot
para estructurar aplicaciones web.
A través de anotaciones simples como `@RestController` y `@GetMapping`, logramos separar la lógica de enrutamiento y exponer múltiples servicios (estado de salud, consulta de datos y métricas simples) de manera clara, modular y lista para integrarse con cualquier cliente frontend.