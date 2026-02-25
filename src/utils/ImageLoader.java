package utils;
import javafx.scene.image.Image;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ImageLoader{
    public static Image load(String pathFromResources){
        // Try classpath locations first
        URL url = ImageLoader.class.getResource(pathFromResources);
        if (url == null) {
            // Try without leading "/resources" when resources are placed at resources root
            String alt = pathFromResources.startsWith("/resources/")
                    ? pathFromResources.replaceFirst("^/resources/", "/")
                    : pathFromResources;
            url = ImageLoader.class.getResource(alt);
        }
        if (url == null) {
            // Try inside files/resources when not on classpath but packaged in project tree
            url = ImageLoader.class.getResource("/files" + (pathFromResources.startsWith("/") ? "" : "/") + pathFromResources);
            if (url == null && pathFromResources.startsWith("/resources/")) {
                url = ImageLoader.class.getResource("/files" + pathFromResources.replaceFirst("^/resources/", "/"));
            }
        }
        if (url != null) {
            return new Image(url.toExternalForm());
        }

        // Filesystem fallbacks relative to working directory
        String rel = pathFromResources.startsWith("/") ? pathFromResources.substring(1) : pathFromResources;
        String relNoRes = rel.startsWith("resources/") ? rel.substring("resources/".length()) : rel;
        String[] prefixes = new String[] {
            "", "files/", "src/", "src/files/", "src/main/resources/", "src/resources/"
        };
        for (String p : prefixes) {
            for (String candidate : new String[]{rel, relNoRes}) {
                Path path = Paths.get(p + candidate);
                if (Files.exists(path)) {
                    return new Image(path.toUri().toString());
                }
            }
        }

        System.err.println("ImageLoader: image not found: " + pathFromResources);
        return null;
    }
}