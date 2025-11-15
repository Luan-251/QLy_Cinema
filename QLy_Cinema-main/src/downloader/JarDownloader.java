package downloader;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JarDownloader {

    public static void download(String url, String filePath) throws IOException, InterruptedException {
        Path path = Paths.get(filePath);

        // Tạo thư mục nếu chưa tồn tại
        Files.createDirectories(path.getParent());
        System.out.println("Thư mục: " + path.getParent() + " đã được đảm bảo.");

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<Path> response = client.send(request,
                HttpResponse.BodyHandlers.ofFile(path));

        System.out.println("Tải thành công vào: " + path.toAbsolutePath());
    }

    public static void main(String[] args) {
        String url = "https://repo1.maven.org/maven2/com/google/code/gson/gson/2.10.1/gson-2.10.1.jar";
        String targetPath = "libs/gson.jar";

        System.out.println("Working dir: " + System.getProperty("user.dir"));
        System.out.println("Target path: " + Paths.get(targetPath).toAbsolutePath());

        try {
            download(url, targetPath);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
