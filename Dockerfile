# Step 1: Base image
FROM eclipse-temurin:21-jdk

COPY target/*.jar rtoms.jar

EXPOSE 8080

# Step 4: Set startup command
ENTRYPOINT ["java", "-jar", "rtoms.jar"]



# ADDED IMAGE => docker build -t rtoms-v1 .
# RUNNING THE CONTAINER => docker run -p 8080:8080 rtoms-v1

# TO CHECK RUNNING PROGRAMS => docker ps

