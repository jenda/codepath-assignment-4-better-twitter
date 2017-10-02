package com.codepath.apps.bluebirdone;

import com.raizlabs.android.dbflow.annotation.Database;

@Database(name = MyDatabase.NAME, version = MyDatabase.VERSION)
public class MyDatabase {

    public static final String NAME = "TwitterDB3";

    public static final int VERSION = 3;
}
