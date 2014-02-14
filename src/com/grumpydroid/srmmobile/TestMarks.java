package com.grumpydroid.srmmobile;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Optimus on 10/12/13.
 */
public class TestMarks {
   public static class Marks {
   @JsonProperty("code")
   String code;
   @JsonProperty("subject")
   String Subject;
   @JsonProperty("marks")
   String Marks;
   }
List<Marks>  marksList = new ArrayList<Marks>();
}
