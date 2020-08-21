package msc.dispatcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    private FileExplorer fileExplorer;

    @Autowired
    public HomeController(FileExplorer fileExplorer) {
        this.fileExplorer = fileExplorer;
    }

    @GetMapping("/")
    public String index() {
        String listedFiles = fileExplorer.listFiles();
        return listedFiles;
    }
}
