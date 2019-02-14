#Proyecto Springboot Fuse 


Este proyecto consiste en una API ReST expuesta mediante rutas Camel utilizando el componente Rest DSL.

El objetivo es proporcionar un quickstart siguiendo convenciones y buenas prácticas en cuanto a la estructura y contenido de una aplicación springboot con Fuse, ideal para armar integraciones entre diferentes sistemas.

* Importa dependencias del proyecto galicia-parent-fuse
* Define la versión 1.5.x como base de springboot
* Sugiere la utilización de la especificación Swagger, mediante la implementación de Camel, para exponer la documentación de la API


Para ejecutar el proyecto en forma local basta con ejecutar el siguiente comando:

```bash
mvn spring-boot:run
```


El proyecto cuenta con la posibilidad de simular un ambiente similar al openshift desplegado en el banco.

Como requisito previo se requiere la instalación de [docker](https://docs.docker.com/install/) y [docker-compose](https://docs.docker.com/compose/install/).

Luego se deberán ejecutar los siguientes comandos:

```bash
mvn clean package

docker-compose up --build
```

Esto permite el despliegue de la aplicación en un container docker conjuntamente con otros entornos, como por ejemplo una base de datos. Esto falicitará realizar tests de integracion entre sistemas.


* [Documentación de la API](http://localhost:8080/webjars/swagger-ui/index.html?url=/camel/api-docs)
* [Soporte de Red Hat para Springboot](https://access.redhat.com/articles/3442361)
* [Apache Camel in Springboot](https://access.redhat.com/documentation/en-us/red_hat_fuse/7.0/html/fuse_on_openshift_guide/camel-spring-boot)
* [Documentación Docker](https://docs.docker.com/)