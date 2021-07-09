FROM java:8
ADD ./target/nekocoin-0.0.1-SNAPSHOT.jar /app/nekocoin-0.0.1-SNAPSHOT.jar
ADD ./target/site/jacoco/index.html /app/
ADD runboot.sh /app/
WORKDIR /app
RUN chmod a+x runboot.sh
CMD /app/runboot.sh