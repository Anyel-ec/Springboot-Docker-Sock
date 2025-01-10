package top.anyel.sock.utils;


import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;


/*
 * Author: Anyel EC
 * Github: https://github.com/Anyel-ec
 * Creation date: 10/01/2025
 */
@UtilityClass
@Slf4j
public class DockerEnvironmentUtil {

    public static void putEnvIfNotNull(ProcessBuilder processBuilder, String key, String value) {
        if (value != null && !value.isEmpty()) {
            processBuilder.environment().put(key, value);
            log.info("Variable de entorno añadida: {} = {}", key, value);
        } else {
            log.warn("La variable de entorno {} tiene un valor nulo o vacío y no se establecerá.", key);
        }
    }

    public static void configureDatabaseEnv(ProcessBuilder processBuilder, Map<String, String> dbConfig) {
        dbConfig.forEach((key, value) -> putEnvIfNotNull(processBuilder, key, value));
    }
}
