package com.abrahammontes.prueba;

public enum ApplicationHolder {
    INSTANCE;

    private PruebaApplication application;

    ApplicationHolder() {

    }

    void setApplication(PruebaApplication app) {
        this.application = app;
    }

    public PruebaApplication getApplication() {
        return application;
    }
}
