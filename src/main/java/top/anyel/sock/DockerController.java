package top.anyel.sock;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import top.anyel.sock.utils.DockerEnvironmentUtil;

import java.io.InputStreamReader;
import java.util.Map;
/*
 * Author: Anyel EC
 * Github: https://github.com/Anyel-ec
 * Creation date: 10/01/2025
 */
@RestController
@RequestMapping("/api/docker")
@RequiredArgsConstructor
public class DockerController {

    @PostMapping("/configurar")
    public ResponseEntity<String> configurarDocker(@RequestBody Map<String, String> envVariables) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder();
            for (Map.Entry<String, String> entry : envVariables.entrySet()) {
                DockerEnvironmentUtil.putEnvIfNotNull(processBuilder, entry.getKey(), entry.getValue());
            }
            return ResponseEntity.ok("Variables de entorno configuradas correctamente.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al configurar las variables de entorno: " + e.getMessage());
        }
    }

    @PostMapping("/ejecutar")
    public ResponseEntity<String> ejecutarDockerCompose() {
        try {
            // Ejecutar docker-compose usando el archivo .env
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "docker-compose",
                    "-f", "/app/docker/Docker-compose.yml",
                    "--env-file", "/app/docker/.env",
                    "up", "-d"
            );

            processBuilder.inheritIO();
            Process process = processBuilder.start();

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                return ResponseEntity.ok("Docker Compose ejecutado con éxito usando .env.");
            } else {
                return ResponseEntity.status(500).body("Error al ejecutar Docker Compose. Código de salida: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error al ejecutar Docker Compose: " + e.getMessage());
        }
    }






}