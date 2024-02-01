package coms309;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.ArrayList;

@RestController
class WelcomeController {

    ArrayList<String> Inputs = new ArrayList<String>();     //ArrayList to track each of the users "inputs" with /{name}
    @GetMapping("/")
    public String welcome() {
        return "Hello and welcome to COMS 309";
    }

    @GetMapping("/{name}")
    public String welcome(@PathVariable String name) {
        Inputs.add(name);       //adds name to the list of "inputs"
        return "Hello and welcome to COMS 309: " + name;

    }

    @GetMapping("/previous")
    public String array() {
        //Create a string and add each element of Inputs separated by ", "
        String output = "";
        for (String input : Inputs) {
            output += input + ", ";
        }
        return "Previous inputs: " + output;
    }

    @GetMapping("/remove")
    public String remove(){
        String element = Inputs.get(Inputs.size() - 1);
        Inputs.remove(Inputs.size() - 1);
        return element + " was removed";
    }

}
