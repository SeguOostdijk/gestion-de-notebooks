# Gestión de Notebooks - Aplicación de Escritorio

Sistema de gestión de prestamos y devoluciones de notebooks, cargadores y mouse para uso en clases.
Esta aplicación esta construida como app desktop con JavaFX + Spring Boot, con persistencia en MySQL.

## Descripción

El sistema permite:

- Registrar un nuevo prestamo para un docente, materia, aula y turno.
- Asociar equipos al prestamo por escaneo de QR o carga manual.
- Gestionar devoluciones (por QR o manual).
- Consultar prestamos activos.
- Consultar historico de prestamos cerrados.
- Generar un formulario PDF por cada prestamo creado.
- Visualizar conteo de equipos disponibles por tipo en la pantalla principal.

## Arquitectura de la aplicacion

Es una aplicacion monolitica de escritorio:

- **UI**: JavaFX con pantallas FXML.
- **Capa de negocio**: componentes Spring (`@Component`, `@Service`).
- **Persistencia**: Spring Data JPA + Hibernate.
- **Base de datos**: MySQL.

Flujo principal de pantallas:

1. `VentanaPrincipal.fxml`
2. `NuevoPrestamo.fxml`
3. `Escaneo.fxml`
4. `PrestamosActivos.fxml` -> `Devolucion.fxml`
5. `Historico.fxml`

## Tecnologias utilizadas

- Java 17
- Spring Boot 3.5.7
- Spring Data JPA
- Hibernate
- JavaFX (controls, fxml, graphics)
- MySQL 8+
- iText 7 (generacion de PDF)
- Maven

## Requisitos

- JDK 17
- Maven (o usar `mvnw`/`mvnw.cmd`)
- MySQL en ejecucion


Ejemplo de preparacion minima en MySQL:


## Ejecutar la aplicacion

Desde la raiz del proyecto (`gestiondenotebooks`):

### Opcion 1: ejecutar en desarrollo (JavaFX)

```powershell
.\mvnw.cmd clean javafx:run
```

### Opcion 2: empaquetar JAR

```powershell
.\mvnw.cmd clean package
```

Luego:

```powershell
java -jar target\gestiondenotebooks-0.0.1-SNAPSHOT.jar
```

## Estructura del proyecto

```text
gestiondenotebooks
|- pom.xml
|- src
|  |- main
|  |  |- java/com/application/gestiondenotebooks
|  |  |  |- controller
|  |  |  |- model
|  |  |  |- repository
|  |  |  |- service
|  |  |  |- pdf
|  |  |  |- enums
|  |  |- resources
|  |     |- application.properties
|  |     |- com.application.gestiondenotebooks/*.fxml
|  |     |- com.application.gestiondenotebooks/style/*.css
|  |- test
|     |- java/com/application/gestiondenotebooks
```

## Modelo de dominio (resumen)

Entidades principales:

- `Prestamo`: referencia, docente, materia, aula, turno, estado, fecha, fecha fin.
- `PrestamoEquipo`: relacion entre prestamo y equipo + estado de devolucion.
- `Equipo`: tipo (`NOTEBOOK`, `CARGADOR`, `MOUSE`), numero y codigo QR.
- `Docente`, `Materia`, `Aula`.

Estados:

- Prestamo: `ABIERTO`, `CERRADO`.
- Devolucion de equipo: `EN_PRESTAMO`, `DEVUELTO`.

## Funcionalidades destacadas

- Alta rapida de docente/materia desde el flujo de nuevo prestamo.
- Deteccion de duplicados al agregar equipos en una misma operacion.
- Control de pertenencia de equipo durante devolucion (validacion por prestamo).
- Generacion de PDF de prestamo en:
  - `%USERPROFILE%\GestionNotebooks\prestamos\prestamo_<REF>.pdf`

## Autores

Segundo Oostdijk, Francisco Viera.

Proyecto desarrollado para la Práctica Profesional Supervisada (PPS) - Universidad CAECE.
