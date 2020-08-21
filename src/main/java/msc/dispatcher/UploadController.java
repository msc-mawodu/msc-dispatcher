package msc.dispatcher;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/file")
public class UploadController {

    private ServletContext servletContext;

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file) throws IOException {

        Files.write(getFilePath(file), file.getBytes());
        return "OK";
    }

    private Path getFilePath(MultipartFile file) {
        return Paths.get(servletContext.getRealPath("uploads") + file.getOriginalFilename());
    }
}
