package coms309.people;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;


import java.util.HashMap;

/**
 * Controller used to showcase Create and Read from a LIST
 *
 * @author Vivek Bengre
 */

@RestController
public class PeopleController {

    // Note that there is only ONE instance of PeopleController in 
    // Springboot system.
    HashMap<String, Person> peopleList = new  HashMap<>();

    //CRUDL (create/read/update/delete/list)
    // use POST, GET, PUT, DELETE, GET methods for CRUDL

    // THIS IS THE LIST OPERATION
    // gets all the people in the list and returns it in JSON format
    // This controller takes no input. 
    // Springboot automatically converts the list to JSON format 
    // in this case because of @ResponseBody
    // Note: To LIST, we use the GET method
    @GetMapping("/people")
    public  HashMap<String,Person> getAllPersons() {
        return peopleList;
    }

    // THIS IS THE CREATE OPERATION
    // springboot automatically converts JSON input into a person object and 
    // the method below enters it into the list.
    // It returns a string message in THIS example.
    // in this case because of @ResponseBody
    // Note: To CREATE we use POST method
    @PostMapping("/people")
    public  String createPerson(@RequestBody Person person) {
        System.out.println(person);
        peopleList.put(person.getFirstName(), person);
        return "New person "+ person.getFirstName() + " Saved";
    }

    // THIS IS THE READ OPERATION
    // Springboot gets the PATHVARIABLE from the URL
    // We extract the person from the HashMap.
    // springboot automatically converts Person to JSON format when we return it
    // in this case because of @ResponseBody
    // Note: To READ we use GET method
    @GetMapping("/people/{firstName}")
    public Person getPerson(@PathVariable String firstName) {
        Person p = peopleList.get(firstName);
        return p;
    }

    // THIS IS THE UPDATE OPERATION
    // We extract the person from the HashMap and modify it.
    // Springboot automatically converts the Person to JSON format
    // Springboot gets the PATHVARIABLE from the URL
    // Here we are returning what we sent to the method
    // in this case because of @ResponseBody
    // Note: To UPDATE we use PUT method

    //Changing put mappings to replace the entire object so it is named the new name
    //Also implementing the ability to specify what part of the person object is being changed

    //Replace entire person
    @PutMapping("/people/{firstName}")
    public Person updatePerson(@PathVariable String firstName, @RequestBody Person p) {
        peopleList.remove(firstName);
        peopleList.put(p.getFirstName(), p);
        return p;
    }

    //Replace first name, also delete and recreate person object so the object is named the new first name
    @PutMapping("/people/{firstName}/change/firstName")
    public Person updateFirstName(@PathVariable String firstName, @RequestBody String name) {
        Person p = peopleList.get(firstName);
        peopleList.remove(firstName);
        p.setFirstName(name);
        peopleList.put(p.getFirstName(), p);
        return p;
    }

    //Replace last name
    @PutMapping("/people/{firstName}/change/lastName")
    public Person updateLastName(@PathVariable String firstName, @RequestBody String lastName) {
        Person p = new Person(peopleList.get(firstName).getFirstName(), lastName, peopleList.get(firstName).getAddress(), peopleList.get(firstName).getTelephone());
        peopleList.replace(firstName, p);
        return p;
    }

    //Replace Address
    @PutMapping("/people/{firstName}/change/address")
    public Person updateAddress(@PathVariable String firstName, @RequestBody String address) {
        Person p = new Person(peopleList.get(firstName).getFirstName(), peopleList.get(firstName).getLastName(), address, peopleList.get(firstName).getTelephone());
        peopleList.replace(firstName, p);
        return p;
    }

    //Replace Telephone
    @PutMapping("/people/{firstName}/change/telephone")
    public Person updateTelephone(@PathVariable String firstName, @RequestBody String telephone) {
        Person p = new Person(peopleList.get(firstName).getFirstName(), peopleList.get(firstName).getLastName(), peopleList.get(firstName).getAddress(), telephone);
        peopleList.replace(firstName, p);
        return p;
    }

    // THIS IS THE DELETE OPERATION
    // Springboot gets the PATHVARIABLE from the URL
    // We return the entire list -- converted to JSON
    // in this case because of @ResponseBody
    // Note: To DELETE we use delete method
    
    @DeleteMapping("/people/{firstName}")
    public HashMap<String, Person> deletePerson(@PathVariable String firstName) {
        peopleList.remove(firstName);
        return peopleList;
    }
}

