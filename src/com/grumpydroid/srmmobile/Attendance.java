package com.grumpydroid.srmmobile;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ashik on 9/1/13.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Attendance {
    public static class Subjects
    {
        @JsonProperty("attnmax")
        protected int attnmax;
        protected int attnhrs;
        protected int abshrs;
        protected float aver;
        protected float odml;
        @JsonProperty("total")
        protected float total;
        @JsonProperty("subj")
        protected String subj="";
    }
    public static class Total
    {
        protected int attnmax;
        protected int attnhrs;
        protected int abshrs;
        protected float aver;
        protected float odml;
        @JsonProperty("total")
        protected float total;
    }
    List<Subjects> mSubjects = new ArrayList<Subjects>();
    Total total = new Total();
    public float getTotal()
    {
        return total.total;
    }
}
