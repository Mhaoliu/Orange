package com.liuhao.orange.db.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.liuhao.orange.db.dao.DaoMaster;

import org.greenrobot.greendao.database.Database;

/**
 * Created by liuhao on 2016/10/25.
 */
public class DaoOpenHelper extends DaoMaster.OpenHelper{

    public DaoOpenHelper(Context context, String name) {
        super(context, name);
    }

    @Override
    public void onCreate(Database db) {
        super.onCreate(db);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
    }
}
