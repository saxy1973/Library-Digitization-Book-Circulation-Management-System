FROM tomcat:9.0-jdk17-temurin

RUN rm -rf /usr/local/tomcat/webapps/*

# Copy web files
COPY src/main/webapp /usr/local/tomcat/webapps/ROOT

# Copy Java source
COPY src/main/java /tmp/src/main/java

# Compile Java source inside Docker
RUN mkdir -p /usr/local/tomcat/webapps/ROOT/WEB-INF/classes && \
    javac -cp "/usr/local/tomcat/webapps/ROOT/WEB-INF/lib/*" \
    -d /usr/local/tomcat/webapps/ROOT/WEB-INF/classes \
    $(find /tmp/src/main/java -name "*.java")

EXPOSE 8080

CMD ["catalina.sh", "run"]