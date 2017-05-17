package br.com.senior.tp2;

import android.app.Application;

import br.com.senior.tp2.domain.DomainObject;

/**
 * Created by cristiano.franco on 23/02/2017.
 */
public class MessageApp extends Application {

    private DomainObject domainObject;


    public DomainObject getDomainObject() {
        return domainObject;
    }

    public void setDomainObject(DomainObject domainObject) {

        this.domainObject = domainObject;
    }
}
