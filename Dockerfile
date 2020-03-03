FROM openjdk:11.0.4-jdk

ARG JAR_FILE
ADD target/${JAR_FILE} /usr/share/application.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Doracle.jdbc.timezoneAsRegion=false","-jar","/usr/share/application.jar"]