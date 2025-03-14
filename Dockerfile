FROM openjdk:17
WORKDIR /
COPY target/*.jar ./ecom-backend.jar

ENTRYPOINT [ "java","-jar","./ecom-backend" ]