package coms309;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
class WelcomeController {

    @GetMapping("/")
    public String welcome() {
        return "Welcome to COMS 309. You have successfully entered the matrix.";
    }

    @GetMapping("/{name}")
    public String welcome(@PathVariable String name) {
        return "Welcome " + name + " to COMS 309. Login to the matrix successful. ";
    }
}
