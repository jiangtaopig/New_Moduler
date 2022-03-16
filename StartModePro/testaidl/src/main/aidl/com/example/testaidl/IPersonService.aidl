// IPersonService.aidl
package com.example.testaidl;
import com.example.testaidl.Person;


// Declare any non-default types here with import statements

interface IPersonService {
    List<Person> getAllPerson();
    void addPerson(in Person person);
}