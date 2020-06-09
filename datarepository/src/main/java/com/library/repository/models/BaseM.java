package com.library.repository.models;

import com.google.gson.Gson;

public class BaseM {

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
