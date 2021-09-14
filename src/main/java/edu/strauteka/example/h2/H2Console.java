package edu.strauteka.example.h2;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Profile("h2-console")
public class H2Console {
    private Server webServer;
    @Value("${h2.console.web.port}")
    private Integer h2ConsoleWebPort;

    @SneakyThrows
    @EventListener(ContextRefreshedEvent.class)
    public void start() {
        log.info("Starting h2 console at port: {}", h2ConsoleWebPort);
        this.webServer = org.h2.tools.Server.createWebServer(
                "-webPort",
                h2ConsoleWebPort.toString(),
                "-tcpAllowOthers",
                "-webSSL")
                .start();
    }

    @EventListener(ContextClosedEvent.class)
    public void stop() {
        log.info("Stopping h2 console at port: {}", h2ConsoleWebPort);
        this.webServer.stop();
    }
}
