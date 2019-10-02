package kr.saintdev.dcalarm.modules.database

object SQLQueries {
    const val CREATE_TABLE_METASET = "CREATE TABLE dc_gallery_metaset (" +
            "  _id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            "  uuid TEXT NOT NULL," +
            "  url TEXT NOT NULL," +
            "  title TEXT NOT NULL DEFAULT ''," +
            "  writer TEXT NOT NULL DEFAULT ''," +
            "  wdate TEXT NOT NULL DEFAULT '1970-01-01 00:00:00'," +
            "  view_count INTEGER NOT NULL DEFAULT 0," +
            "  is_notified INTEGER NOT NULL DEFAULT 0);"

    const val CREATE_TABLE_TARGETING_GALL = "CREATE TABLE dc_tracking_gallery (" +
            "  _id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            "  gall_name TEXT NOT NULL," +
            "  gall_id TEXT NOT NULL," +
            "  wdate TEXT NOT NULL);"

    const val INSERT_DC_TARGETING_GALLERY = "INSERT INTO dc_tracking_gallery (gall_name, gall_id, wdate) VALUES(?,?,?);"

    const val SELECT_DC_TARGETING_GALLERY_ALL = "SELECT * FROM dc_tracking_gallery ORDER BY _id DESC;"
}