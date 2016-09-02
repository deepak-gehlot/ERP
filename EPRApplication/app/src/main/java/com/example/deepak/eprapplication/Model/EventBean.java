package com.example.deepak.eprapplication.Model;

import java.util.ArrayList;

/**
 * Created by mercury-five on 02/09/16.
 */
public class EventBean {

/*
* {
    "Calender": [
        {
            "id": "7",
            "title": "Hoilday4",
            "last_updated": "02/09/201617: 48: 34",
            "maindate": "03/09/201600: 00: 00",
            "ShowDate": "03September2016"
        }
    ],
    "TotalRows": "1"
}
* */

    public ArrayList<Calender> Calender;

    public class Calender {
        public String id;
        public String title;
        public String last_updated;
        public String maindate;
        public String ShowDate;
    }
}
