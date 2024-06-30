package utils.com.gabriel.orders.infra;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class OasConverter {

    public static String convertSpecToJson(String yamlFilePath, String path) throws Exception {
        try (InputStream inputStream = OasConverter.class.getResourceAsStream(yamlFilePath)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("File not found: " + yamlFilePath);
            }
            Yaml yaml = new Yaml();
            Map<String, Object> yamlData = yaml.load(inputStream);

            Map<String, Object> targetNode = navigatePath(yamlData, path.split(":"));

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.valueToTree(targetNode);

            return objectMapper.writeValueAsString(jsonNode);
        }
    }

    private static Map<String, Object> navigatePath(Map<String, Object> currentNode, String[] pathParts) {
        if (pathParts.length == 0 || currentNode == null) {
            return currentNode;
        }

        String currentKey = pathParts[0];
        Object nextNode = currentNode.get(currentKey);

        if (nextNode instanceof Map) {
            String[] remainingPath = new String[pathParts.length - 1];
            System.arraycopy(pathParts, 1, remainingPath, 0, pathParts.length - 1);
            return navigatePath((Map<String, Object>) nextNode, remainingPath);
        }

        return currentNode;
    }
}
