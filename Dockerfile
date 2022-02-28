FROM openjdk:11
EXPOSE 8080:8080
RUN mkdir /app
COPY ./build/install/com.octopus.vivira-task/ /app/
WORKDIR /app/bin
CMD ["./com.octopus.vivira-task"]