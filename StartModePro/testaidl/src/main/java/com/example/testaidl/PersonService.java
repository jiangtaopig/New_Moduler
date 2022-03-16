package com.example.testaidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PersonService extends Service {

    private List<Person> personList = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        personList.add(new Person("张三", 23));
        personList.add(new Person("李莎", 26));
        personList.add(new Person("王八", 43));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private IBinder binder = new IPersonService.Stub() {
        @Override
        public List<Person> getAllPerson() throws RemoteException {
            return personList;
        }

        @Override
        public void addPerson(Person person) throws RemoteException {
            personList.add(person);
        }
    };
}
