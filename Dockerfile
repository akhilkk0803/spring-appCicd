FROM openjdk:17
WORKDIR /
COPY target/*.jar ./ecom.jar

ENTRYPOINT [ "java","-jar","./ecom-backend" ]