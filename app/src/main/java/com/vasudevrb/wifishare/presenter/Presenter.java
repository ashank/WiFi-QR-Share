package com.vasudevrb.wifishare.presenter;

public interface Presenter<V> {

    void attachView(V view);

    void detachView();
}
