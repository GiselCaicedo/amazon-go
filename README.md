[![Build Status](https://dev.azure.com/aquality-automation/aquality-automation/_apis/build/status/aquality-automation.aquality-appium-mobile-java-template?branchName=master)](https://dev.azure.com/aquality-automation/aquality-automation/_build/latest?definitionId=11&branchName=master)
[![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=aquality-automation_aquality-appium-mobile-java-template&metric=alert_status)](https://sonarcloud.io/dashboard?id=aquality-automation_aquality-appium-mobile-java-template)
[![Allure report](https://github.com/GiselCaicedo/amazon-go/actions/workflows/test-with-allure-report.yml/badge.svg)](https://aquality-automation.github.io/aquality-appium-mobile-java-template/)

# Amazon Go Mobile Project

### Estructura del proyecto
- **aquality-appium-mobile-template** - parte del proyecto relacionada con PageObjects, modelos y utilidades
  - **configuration/**: clases utilizadas para obtener la configuración del proyecto desde la carpeta [src/main/resources/environment](https://github.com/GiselCaicedo/amazon-go/tree/main/aquality-appium-mobile-template/src/main/resources/environment)
  - **screens/**: Page Objects (objetos de página)
  - **models/**: clases que representan los modelos de datos de la aplicación bajo prueba (clases POJO)
  - **utilities/**: clases utilitarias
  - **src/main/resources/**: archivos de recursos como configuraciones y datos de prueba
- **aquality-appium-mobile-template-cucumber** - implementación de pruebas con Cucumber
  - **features/**: archivos de características Gherkin con escenarios de prueba
  - **hooks/**: [hooks](https://specflow.org/documentation/Hooks/) de Cucumber
  - **stepdefinitions/**: clases de definición de pasos
  - **transformations/**: [transformaciones de datos](https://cucumber.io/docs/cucumber/configuration/) de Cucumber
  - **objectfactory/**: configuración del [contenedor de inyección de dependencias (DI)](https://cucumber.io/docs/cucumber/state/#how-to-use-di)

### Configuración
El archivo [settings.json](https://github.com/GiselCaicedo/amazon-go/blob/main/aquality-appium-mobile-template/src/main/resources/settings.json) contiene las configuraciones de la biblioteca Aquality Appium Mobile.

[allure.properties](https://github.com/GiselCaicedo/amazon-go/blob/main/aquality-appium-mobile-template/src/main/resources/allure.properties) es una parte de la configuración del reporte de Allure. Consulta más detalles [aquí](https://docs.qameta.io/allure/).

### Ejecución de pruebas
Los escenarios de los archivos de características se pueden ejecutar con el plugin de TestNG para el IDE (IntelliJ Idea, Eclipse) o con el comando de Maven ```mvn clean test``` donde se puede especificar todos los argumentos necesarios.

### Reportes


### License
Library's source code is made available under the [Apache 2.0 license](https://github.com/GiselCaicedo/amazon-go/blob/main/LICENSE).
