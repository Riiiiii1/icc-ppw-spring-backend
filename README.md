# PRÁCTICA 1

## 1. Captura de verificación de Java

![Evidencia Java Version](src/main/resources/assets/java_version.png)

## 2. Captura del servidor Spring Boot ejecutándose

![Evidencia Servidor Funcional](src/main/resources/assets/server_funcional.jpeg)

## 3. Captura del endpoint /api/status funcionando

![Evidencia Status Funcional](src/main/resources/assets/status_funcional.jpeg)

## 4. Captura del comando ls en terminal

![Evidencia LS Controllers](src/main/resources/assets/mostrar.png)

## 5. Explicación breve escrita por el estudiante

El endpoint de status es para verificar que funcione y este corriendo Spring Boot.

# PRÁCTICA 2

## 1. Captura del id mostrando Fundamentos modulares

![Evidencia Java Version](src/main/resources/assets/carpeta.png)

## 5. Explicación breve 

Es importante tener modulos separados para poder facilitar la migracion del monolito a microservicios.


# PRÁCTICA 3

## 1. GET /api/products
![Evidencia Java Version](src/main/resources/assets/api_products_all.png)

## 2. GET /api/products/:id

![Evidencia Java Version](src/main/resources/assets/api_products_id.png)

## 3. DELETE /api/products/:id Existente
![Evidencia Java Version](src/main/resources/assets/api_products_delete.png)

## 4. DELETE /api/products/:id No Existente

![Evidencia Java Version](src/main/resources/assets/api_products_delete_not_found.png)

# PRÁCTICA 4

## 1. PRODUCTS CONTROLLER
![Evidencia Java Version](src/main/resources/assets/productsController.png)

## 2. PRODUCTS SERVICE IMPLEMENTS

![Evidencia Java Version](src/main/resources/assets/productsServiceImplements.png)

# PRÁCTICA 5

## 1. 5 PRODUCTOS CREADOS (MOSTRAR EN DBEAVER)
![Evidencia Java Version](src/main/resources/assets/five_products.png)

# PRÁCTICA 6

## 1. Captura Respues de Error POST INVALIDO

![Evidencia Java Version](src/main/resources/assets/post_invalido.png)

# PRÁCTICA 7

## 1. Captura Error: GET /api/products/20002222

![Evidencia Java Version](src/main/resources/assets/producto_inexistente.png)

## 2. Captura Error Duplicado: POST /api/products

![Evidencia Java Version](src/main/resources/assets/duplicate_product.png)

## 3. Capturar Error Validación DTO: POST /api/products (BAD REQUEST)

![Evidencia Java Version](src/main/resources/assets/post_invalido.png)

# PRÁCTICA 8

## 1. Captura OBJETO ANIDADO OWNER, CATEGORY Y CAMPOS DE FECHA  POST /api/products

![Evidencia Java Version](src/main/resources/assets/post_anidado.png)

## 2. Captura GET /api/products/category/1  
Esta captura ya tiene cambios hechos del futuro. (Revertir a /api/categories/3/products)
![Evidencia Java Version](src/main/resources/assets/product_categoryget.png)


## 3. ¿Cómo se relaciona ProductEntity con UserEntity y CategoryEntity usando @ManyToOne y @JoinColumn?
ProductEntity se relaciona con UserEntity mediante una asociación @ManyToOne, ya que muchos productos pueden pertenecer a un mismo usuario (owner), pero cada producto tiene un único dueño.

# PRÁCTICA 9

## 1. Captura Varias categorias POST /api/products

![Evidencia Java Version](src/main/resources/assets/varias_categorias.png)


## 2. Captura FILTROS PRODUCTOS GET /api/users/11/products?name=laptop&minPrice=1200

![Evidencia Java Version](src/main/resources/assets/filtro_productos_del_usuario.png)

## 3. Captura FILTROS CATEGORIAS GET /api/categories/2/products?userId=11

![Evidencia Java Version](src/main/resources/assets/categorias_filtros.png)

## 4. ¿Por qué se usa ProductService y ProductRepository para consultar productos aunque el endpoint esté dentro del contexto /users/{id}/products o /categories/{id}/products?

Porque el recurso que se está consultando sigue siendo un Product, sin importar la ruta desde la que se acceda. 

## 5. ¿Qué cambió al pasar de Product N ──── 1 Category a Product N ──── N Category?

Cambió que en lugar de una sola columna foránea (category_id) en la tabla products, ahora se necesita una tabla intermedia (product_category) que guarde múltiples pares producto-categoría, y la relación pasa de @ManyToOne a @ManyToMany con @JoinTable.

# PRÁCTICA 10


## 1. Captura GET /api/products/page?page=0&size=5

![Evidencia Java Version](src/main/resources/assets/paginacion_con_filtros.png)

## 2. Captura GET /api/products/slice?page=0&size=5

![Evidencia Java Version](src/main/resources/assets/slice_filtro.png)

## 3. Captura GET /api/products/page?page=-1&size=0 PAGINACION INVALIDA


![Evidencia Java Version](src/main/resources/assets/paginacion_invalida.png)

## 4. Captura GET /api/categories/2/products/page?page=110&size=5

![Evidencia Java Version](src/main/resources/assets/categoria_paginado.png)

## 5. Captura GET /api/categories/2/products/slice?page=10&size=5

![Evidencia Java Version](src/main/resources/assets/slice_paginado.png)


## 6. ¿Cuál es la diferencia entre Page y Slice?

Page: Devuelve los datos de una página más información del total de registros y páginas.
Slice: Devuelve solo los datos de la página actual y no calcula el total, por lo que es más rápido

## 7. ¿Por qué la paginación debe aplicarse en el repositorio y no después de traer todos los datos en memoria?

Porque el repositorio hace la paginación directamente en la base de datos, reduciendo el uso de memoria y mejorando el rendimiento.

# PRÁCTICA 11

## 1. POST /api/auth/register

![Evidencia Java Version](src/main/resources/assets/register.png)

## 2. POST /api/auth/login

![Evidencia Java Version](src/main/resources/assets/login.png)

## 3. GET /api/products/page?page=0&size=5 NO AUTORIZADO

![Evidencia Java Version](src/main/resources/assets/401.png)

## 4. GET /api/products/page?page=0&size=5 AUTORIZADO CON TOKEN

![Evidencia Java Version](src/main/resources/assets/protegido.png)

# PRÁCTICA 12

## 1. GET /api/users/me

![Evidencia Java Version](src/main/resources/assets/me.png)

## 2. GET /api/products ACCEDO DENEGADO POR ROL

![Evidencia Java Version](src/main/resources/assets/forbbiden.png)

## 3. GET /api/products ACCEDO PERMITIDO POR ROL DE ADMINISTRADOR

![Evidencia Java Version](src/main/resources/assets/no_forbbiden.png)

## 4. ¿Cuál es la diferencia entre autenticación y autorización?

Autenticación: Verifica la identidad del usuario, es decir, confirma quién es (por ejemplo, mediante usuario y contraseña o un token).

Autorización: Determina qué acciones o recursos puede acceder un usuario una vez que ya ha sido autenticado.

## 5. ¿Por qué GET /api/products debe ser solo para ADMIN, mientras GET /api/products/page puede ser consumido por cualquier usuario autenticado? RESPONDE ESTA, ALGO CORTO

Porque GET /api/products trae todos los productos sin paginar , mientras que /page ya viene paginado y filtrado por deleted = false, lo cual es seguro y eficiente para exponerlo a cualquier usuario; el listado sin restricciones se reserva para ADMIN por motivos de rendimiento y control administrativo.

# PRÁCTICA 13

## 1. POST /api/products AUTENTICADO

![Evidencia Java Version](src/main/resources/assets/api_products_auth.png)

forbiden_get_unitorized.png

## 2. PUT /api/products/{id} FORBIDEN
![Evidencia Java Version](src/main/resources/assets/forbiden_get_unitorized.png)
## 3. DELETE /api/products/{id} FORBIDEN
![Evidencia Java Version](src/main/resources/assets/delete_forbbiden.png)
## 4. PUT /api/products/{id} AUTORIZADO POR ADMINISTRADOR


![Evidencia Java Version](src/main/resources/assets/admin.png)

## 5. ¿Qué es ownership?
Es el principio de que un recurso  pertenece a un usuario específico, y solo ese usuario  tiene permiso para modificarlo o eliminarlo.
## 6. ¿Por qué no es seguro recibir userId en CreateProductDto?
Porque permitiría que cualquier usuario autenticado creara productos a nombre de otra persona, con solo cambiar el valor de userId en el body de la petición
## 7. ¿Cuál es la diferencia entre autorización por rol y autorización por ownership?
La autorización por rol valida qué tipo de usuario es , sin importar a quién pertenece el recurso. La autorización por ownership valida si el usuario autenticado es el dueño específico del recurso que intenta modificar, sin importar su rol.
