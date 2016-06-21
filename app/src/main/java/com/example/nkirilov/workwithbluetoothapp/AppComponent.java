package com.example.nkirilov.workwithbluetoothapp;

import com.example.nkirilov.workwithbluetoothapp.data.Repository;
import com.example.nkirilov.workwithbluetoothapp.data.RxBleSingleton;
import com.example.nkirilov.workwithbluetoothapp.data.dataModule.DataModule;
import com.example.nkirilov.workwithbluetoothapp.domain.IntetactorImpl;
import com.example.nkirilov.workwithbluetoothapp.domain.domainModule.DomainModule;
import com.example.nkirilov.workwithbluetoothapp.presentation.Adapter;
import com.example.nkirilov.workwithbluetoothapp.presentation.MainActivity;
import com.example.nkirilov.workwithbluetoothapp.presentation.Presenter;
import com.example.nkirilov.workwithbluetoothapp.presentation.presentationModule.PresentationModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by n.kirilov on 21.06.2016.
 */
@Singleton
@Component(modules =
        {
                AppModule.class,
                RxBleSingleton.class,
                DataModule.class,
                DomainModule.class,
                PresentationModule.class
        })
public interface AppComponent {

        void inject(Repository repository);

        void inject(IntetactorImpl intetactor);

        void inject(Presenter presenter);

        void inject(Adapter adapter);

        void inject(MainActivity mainActivity);
}
