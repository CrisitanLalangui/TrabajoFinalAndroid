package com.example.studybro.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CentrosRegistrados {

    @SerializedName("data")
    public List<CentrosDjangoObtener> filesList;

    public static class CentrosDjangoObtener {

        @SerializedName("name")
        public String name;
    }
}
