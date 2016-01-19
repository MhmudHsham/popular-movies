package com.hisham.mahmoud.popularmovies.model;


import android.net.Uri;

import java.io.Serializable;


public class Trailer {

    private String trailerKey;
    private String trailerName;

    public Trailer() {
    }

    public Trailer(String trailerKey, String trailerName) {
        this.trailerKey = trailerKey;
        this.trailerName = trailerName;
    }

    public String getTrailerKey() {
        return trailerKey;
    }

    public void setTrailerKey(String trailerKey) {
        this.trailerKey = trailerKey;
    }

    public String getTrailerName() {
        return trailerName;
    }

    public void setTrailerName(String trailerName) {
        this.trailerName = trailerName;
    }
}
