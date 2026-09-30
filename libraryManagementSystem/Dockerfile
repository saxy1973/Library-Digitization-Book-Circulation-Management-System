FROM tomcat:9.0-jdk17-temurin

RUN rm -rf /usr/local/tomcat/webapps/*

COPY src/main/webapp /usr/local/tomcat/webapps/ROOT

COPY src/main/java /tmp/src/main/java

RUN mkdir -p /usr/local/tomcat/webapps/ROOT/WEB-INF/classes && \
    javac -cp "/usr/local/tomcat/webapps/ROOT/WEB-INF/lib/*" \
    -d /usr/local/tomcat/webapps/ROOT/WEB-INF/classes \
    $(find /tmp/src/main/java -name "*.java")

CMD sed -i 's/port="8005"/port="-1"/' /usr/local/tomcat/conf/server.xml && \
    sed -i "s/port=\"8080\"/port=\"${PORT:-10000}\"/" /usr/local/tomcat/conf/server.xml && \
    export JAVA_OPTS="$JAVA_OPTS -Djava.net.preferIPv4Stack=true" && \
    catalina.sh run

EXPOSE 10000