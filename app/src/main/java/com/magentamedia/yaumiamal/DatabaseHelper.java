package com.magentamedia.yaumiamal;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TABLE_AMALAN = "CREATE TABLE tb_list_amalan (id_la INTEGER NOT NULL " +
            "PRIMARY KEY AUTOINCREMENT, amalan TEXT UNIQUE, tipe TEXT, group_amal INTEGER);";

    private static final String TABLE_AMALKU = "CREATE TABLE tb_amalanku (id_a INTEGER " +
            "PRIMARY KEY AUTOINCREMENT, id_la INTEGER, my_target TEXT, col_sun INTEGER DEFAULT 1, " +
            "col_mon INTEGER DEFAULT 1, col_tue INTEGER DEFAULT 1, col_wed INTEGER DEFAULT 1, " +
            "col_thu INTEGER DEFAULT 1, col_fri INTEGER DEFAULT 1, " +
            "col_sat INTEGER DEFAULT 1, notification_time TEXT NOT NULL DEFAULT '08:00', " +
            "notification_enabled INTEGER DEFAULT 0);";

    //for saved cleared task amal
    private static final String TABLE_TODO_AMAL = "CREATE TABLE tb_passed_amal (id_pa INTEGER " +
            "PRIMARY KEY AUTOINCREMENT, id_a INTEGER, id_la INTEGER UNIQUE, " +
            "date_passed TEXT DEFAULT (date('now','localtime')) , " +
            "status_passed INTEGER NOT NULL DEFAULT 0);";

    //for point
    private static final String TABLE_POINT_AMAL = "CREATE TABLE tb_mypoint (id_pt INTEGER " +
            "PRIMARY KEY AUTOINCREMENT, date_today TEXT UNIQUE DEFAULT (date('now','localtime')), " +
            "target_today INTEGER, done_today INTEGER);";

    //group list amalan
    //1 sholat, 2 hafalan surat, 3 buku, 4 lain, 5 buku dan quran

    //tipe list amalan
    // p = primary, s = secondary, c = user custom made

    private static final String sqlAMalan = "INSERT INTO tb_list_amalan (id_la,amalan,tipe,group_amal) VALUES " +
            " (1,'shalat tahajud','p',1),\n" +
            " (2,'shalat qobliah subuh','p',1),\n" +
            " (3,'shalat subuh berjamaah','p',1),\n" +
            " (4,'dzikir pagi','p',4),\n" +
            " (5,'shalat dhuha','p',1),\n" +
            " (6,'shalat qobliyah dzuhur','p',1),\n" +
            " (7,'shalat dzuhur berjamaah','p',1),\n" +
            " (8,'shalat badiyah dzuhur','p',1),\n" +
            " (9,'shalat ashar berjamaah','p',1),\n" +
            " (10,'dzikir petang','p',4),\n" +
            " (11,'shalat maghrib berjamaah','p',1),\n" +
            " (12,'shalat badiyah maghrib','p',1),\n" +
            " (13,'shalat isya berjamaah','p',1),\n" +
            " (14,'shalat badiyah isya','p',1),\n" +
            " (15,'shalat witir','p',1),\n" +
            " (16,'tilawah','p',2),\n" +
            " (17,'murojaah','p',5),\n" +
            " (18,'hafalan surat','p',2),\n" +
            " (19,'menghadiri kajian','s',4),\n" +
            " (20,'silaturahim','s',4),\n" +
            " (21,'sedekah/infaq','s',4),\n" +
            " (22,'membantu orang tua','s',4),\n" +
            " (23,'membantu orang lain','s',4),\n" +
            " (24,'puasa sunnah','s',4),\n" +
            " (25,'membaca buku','s',3),\n" +
            " (26,'shalat tahiyatul masjid','s',1),\n" +
            " (27,'shalat syuruq','s',1),\n" +
            " (28,'kerja bakti','s',4),\n" +
            " (29,'membaca al kahfi (khusus hari jumat)','s',4),\n" +
            " (30,'bersiwak sebelum shalat','s',4),\n" +
            " (31,'berwudhu sebelum tidur','s',4),\n" +
            " (32,'sholawat','s',4),\n" +
            " (33,'istighfar','s',4);";

    public DatabaseHelper(Context context) {
        super(context, "db_yawmi", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TABLE_AMALAN);
        sqLiteDatabase.execSQL(TABLE_AMALKU);
        sqLiteDatabase.execSQL(TABLE_TODO_AMAL);
        sqLiteDatabase.execSQL(TABLE_POINT_AMAL);
        sqLiteDatabase.execSQL(trigger_insert_after_amal());
        sqLiteDatabase.execSQL(trigger_update_after_amalanku());
        sqLiteDatabase.execSQL(trigger_delete_listAmalan());

        //batch insert
        try {
            sqLiteDatabase.execSQL(sqlAMalan);
        } catch (SQLException e) {
            Log.d("ERROR", ""+e.getMessage());
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    //triger DB sqlite

    public String trigger_delete_listAmalan() {
        String sql = "CREATE TRIGGER delete_list_amalan " +
                "AFTER DELETE ON [tb_amalanku] " +
                "for each row " +
                "BEGIN " +
                "delete from tb_passed_amal where id_la = old.id_la;" +
                "UPDATE tb_mypoint SET target_today = target_today - 1 WHERE date_today = date('now');" +
                "END;";

        return sql;
    }

    public String trigger_insert_after_amal() {
        String sql = "CREATE TRIGGER insert_point_amal AFTER INSERT ON [tb_amalanku] " +
                "for each row " +
                "BEGIN " +
                "INSERT OR IGNORE INTO tb_mypoint (target_today, done_today) " +
                "VALUES (0,0); " +
                "UPDATE tb_mypoint SET target_today = target_today + 1 WHERE date_today = date('now');" +
                "END;";

        return  sql;
    }

    public String trigger_update_after_amalanku() {
        String sql = "CREATE TRIGGER update_passed_amal AFTER UPDATE OF status_passed ON [tb_passed_amal] " +
                "for each row " +
                "WHEN old.status_passed != new.status_passed " +
                "BEGIN " +
                "UPDATE tb_mypoint SET done_today = done_today + 1 WHERE date_today = date('now'); " +
                "END;";

        return  sql;
    }

}
