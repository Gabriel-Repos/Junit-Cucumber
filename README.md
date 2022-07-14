# Junit-Cucumber
Repositorio con ejemplo de como utilizar Junit (SIN Cucumber) para pruebas de Integracion / Unitarias

Para poder realizar una prueba completa (pruebas de integracion) con la base de datos mongo que utiliza el servicio, se debe realizar la instalacion de la misma. 

Instalacion de la base MongoDB Community en MacOS. (Se debe instalar previamente Xcode y Brew)
  - MacOs: https://www.mongodb.com/docs/manual/tutorial/install-mongodb-on-os-x/
  - Para iniciar la base luego de la instalacion: brew services start mongodb-community@5.0
  - Para detener la ejecucion del servicio de la base: brew services stop mongodb-community@5.0
  
  
Instalacion de la base MongoDB Community en Windows.
  - Seguir las instrucciones de: https://www.mongodb.com/docs/manual/tutorial/install-mongodb-on-windows/


Realizar analisis de Jacoco y SonarQube:
Las dependencias para estas herrmamientas ya se encuentran implementadas y configuradas. Se debe tener minimamente una instalacion local "Developer" (para poder utilizarlo)
Para realizar un analisis completo se deben seguir los pasos:
    - mvn clean install
    - mvn jacoco:report
    - mvn sonar:sonar -Dsonar.projectKey=Junit-Cucumber -Dsonar.host.url=http://localhost:9000 -Dsonar.login=sqp_655c1a3d6ae328e04ef598c5a21069df8dd51a2b
