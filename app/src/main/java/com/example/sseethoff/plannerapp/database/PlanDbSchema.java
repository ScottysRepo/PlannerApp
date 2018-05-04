package com.example.sseethoff.plannerapp.database;

public class PlanDbSchema {
    public static final class PlanTable {
        public static final String NAME = "plans";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String ACTIVITY = "activity";
        }
    }
    public static final class DateTable {
        public static final String NAME = "dates";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String DATE = "date";
        }
    }
}