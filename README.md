![alt tag](https://github.com/LabPLC/Traxi/raw/master/Traxi/res/drawable-hdpi/ic_launcher.png?raw=true) TRAXI
============

App que te auxilia a la hora de tomar un taxi del Distrito Federal (DF), al ingresar una placa se te mostrarán los datos del vehículo ademas de un termómetro de confianza:

Dado por la app:
* revista vehicular
* infracción de tránsito
* tenencia vehicular
* año del taxi
* verificación vehicular

Dado por los usuarios:
* Calificación de 0 a 5 
* Comentario
                
Adicionalmente puedes ver quien de tus amigos de Facebook han tomado ese taxi y sus calificaciones.

# Fotografía

Puedes ingresar la placa del vehículo tomando una foto a la puerta o a su tablero del taxi.

# Contacto de emergencia y seguridad

Puedes agregar hasta dos contactos de emergencia en los que debes agregar su celular y correo, esto servira para poder activar el modo paranoico o utilizar el botón de panico

* Botón de pánico, durante tu viaje y sólo durante tu viaje puedes apretar 5 veces el botón de encendido del smartphone y se enviara un SMS a tu contacto de emergencia indicándole que revise su correo y n correos electrónicos mostrando tu ubicación y nivel de batería.
* Modo paranoico, este esta desactivado por defecto per se puede hacer que te estén llegando notificaciones preguntando si te encuentras bien!, de no contestarla se envía un SMS a tu contacto de emergencia indicandole que revise su correo y n correos electrónicos mostrando tu ubicación y nivel de batería.

Una vez iniciado el viaje la app te rastreara cada 5 segundos.

# Tips:

Traxi te da consejos de seguridad y para un buen uso de los taxis del DF.


# Mapa

Durante el viaje el usuario puede ver un mapa (googleMaps) donde ve el camino que a recorrido y puede poner el destino, si hace esto se mostrara la ruta posible que puede tomar el taxi, la distancia y el tiempo que le falta por recorrer.

Si configuraste tus contactos de emergencia puedes activar el modo paranoico durante el viaje o desactivarlo.

# Calificación

Al final del viaje el usuario puede calificar el servicio del 0 al 5 y puede hacer un comentario de 50 caracteres.


# Idiomas

La aplicación tiene soporte para idiomas español e ingles.

___________________
___________________
# Pantallas

<p align="center">
<img src="https://github.com/LabPLC/Traxi/blob/master/screenshot/Screenshot_2014-06-02-17-55-17.png?raw=true" alt="Traxi" height="460" width="240"/>
<img src="https://github.com/LabPLC/Traxi/blob/master/screenshot/Screenshot_2014-06-02-17-57-40.png?raw=true" alt="Traxi" height="460" width="240"/>
<img src="https://github.com/LabPLC/Traxi/blob/master/screenshot/Screenshot_2014-06-02-17-57-55.png?raw=true" alt="Traxi" height="460" width="240"/>
<img src="https://github.com/LabPLC/Traxi/blob/master/screenshot/Screenshot_2014-06-02-18-01-00.png?raw=true" alt="Traxi" height="460" width="240"/>
<img src="https://github.com/LabPLC/Traxi/blob/master/screenshot/Screenshot_2014-06-02-18-01-26.png?raw=true" alt="Traxi" height="460" width="240"/>
<img src="https://github.com/LabPLC/Traxi/blob/master/screenshot/Screenshot_2014-06-02-18-01-44.png?raw=true" alt="Traxi" height="460" width="240"/>
<img src="https://github.com/LabPLC/Traxi/blob/master/screenshot/Screenshot_2014-06-02-18-01-59.png?raw=true" alt="Traxi" height="460" width="240"/>
<img src="https://github.com/LabPLC/Traxi/blob/master/screenshot/Screenshot_2014-06-02-18-02-06.png?raw=true" alt="Traxi" height="460" width="240"/>
<img src="https://github.com/LabPLC/Traxi/blob/master/screenshot/Screenshot_2014-06-02-18-02-23.png?raw=true" alt="Traxi" height="460" width="240"/>
<img src="https://github.com/LabPLC/Traxi/blob/master/screenshot/Screenshot_2014-06-02-18-02-41.png?raw=true" alt="Traxi" height="460" width="240"/>
<img src="https://github.com/LabPLC/Traxi/blob/master/screenshot/Screenshot_2014-06-02-18-03-27.png?raw=true" alt="Traxi" height="460" width="240"/>
<img src="https://github.com/LabPLC/Traxi/blob/master/screenshot/Screenshot_2014-06-02-18-03-38.png?raw=true" alt="Traxi" height="460" width="240"/>
<img src="https://github.com/LabPLC/Traxi/blob/master/screenshot/Screenshot_2014-06-02-18-03-55.png?raw=true" alt="Traxi" height="460" width="240"/>
<img src="https://github.com/LabPLC/Traxi/blob/master/screenshot/Screenshot_2014-06-02-18-05-49.png?raw=true" alt="Traxi" height="460" width="240"/>
<img src="https://github.com/LabPLC/Traxi/blob/master/screenshot/Screenshot_2014-06-02-18-07-13.png?raw=true" alt="Traxi" height="460" width="240"/>
<img src="https://github.com/LabPLC/Traxi/blob/master/screenshot/Screenshot_2014-06-02-18-07-20.png?raw=true" alt="Traxi" height="460" width="240"/>
<img src="https://github.com/LabPLC/Traxi/blob/master/screenshot/Screenshot_2014-06-02-18-09-28.png?raw=true" alt="Traxi" height="460" width="240"/>
<img src="https://github.com/LabPLC/Traxi/blob/master/screenshot/Screenshot_2014-06-02-18-10-00.png?raw=true" alt="Traxi" height="460" width="240"/>


</p>


___________________
___________________
Desarrollada en Eclipse Kepler y ADT para android 4.0 y superiores

# BIBLIOTECAS EXTERNAS 

    google_play_services_lib
    FacebookSDK

AYUDA PARA AGREGAR google_play_services_lib
    seleccionar New-> Project -> Android Application Project from Existing Code
    dar doble click en la ruta - "android-sdk\extras\google\google_play_services"
    
AYUDA PARA AGREGAR FacebookSDK
    se encuentra en https://developers.facebook.com/docs/android/


# BIBLIOTECAS INTERNAS

    acra-4.5.jar
    activation.jar
    additionnal.jar
    apache-mime4j-core-0.7.2.jar
    httpclient-4.3.1.jar
    httpcore-4.3.jar
    httpmime-4.3.1.jar
    mail.jar
    socketio.jar
    
Estas se encuentran en la carpeta libs del proyecto.

Debido a problemas de compativilidad con las versiones de Android acra-4.5.jar también debe ser agregada en caso de que no la reconozca dando clic derecho al proyecto-> Properties ->Java Build Path -> Libraries -> add JARs.. y agregarla de la carpeta libs que esta dentro del proyecto. 
___________________
___________________
# ¿Quiéres probar la app?

Estamos en pruebas BETA así que debes enviar un correo (gmail preferentemente) a miguel.moran@codigo.labplc.mx una vez agregado al grupo TRAXI podras descargarla de aqui [Traxi Beta](https://play.google.com/apps/testing/codigo.labplc.mx.traxi﻿)




# Dudas

    @yosoymikesaurio
    miguel.moran@codigo.labplc.mx
    http://www.traxi.mx
