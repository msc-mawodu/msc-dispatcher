package msc.dispatcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class HomeController {

    private FileExplorer fileExplorer;

    @Autowired
    public HomeController(FileExplorer fileExplorer) {
        this.fileExplorer = fileExplorer;
    }

    @GetMapping("/")
    public String index() throws IOException, InterruptedException {
        String listedFiles = fileExplorer.listFiles();
        Profiler profiler = new Profiler();
        profiler.monitor();

        return listedFiles;
    }
}
