package edu.senla.helper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.sql.Connection;

@RequiredArgsConstructor
@Component
@Getter
public class ConnectionHolder {

    private final Connection connection;

    @SneakyThrows
    @PreDestroy
    protected void closeConnection()  {
        connection.close();
    }

}
