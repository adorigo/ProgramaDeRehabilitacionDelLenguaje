package ar.org.ineco.prl.programaderehabilitaciondellenguaje;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class MyDatabase extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "database.sqlite";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_OPTIONS = "co_options";
    public static final String OPTIONS_COLUMN_ID = "opt_id";
    public static final String OPTIONS_COLUMN_TEXT = "opt_text";
    public static final String OPTIONS_COLUMN_QID = "q_id";

    public static final String TABLE_QUESTIONS = "co_questions";
    public static final String TABLE_VIEW_QUESTIONS = "preguntas";
    public static final String QUESTIONS_COLUMN_ID = "q_id";
    public static final String QUESTIONS_COLUMN_TEXT = "q_text";
    public static final String QUESTIONS_COLUMN_CHECK = "q_check";
    public static final String QUESTIONS_COLUMN_ANSID = "q_ansid";
    public static final String QUESTIONS_COLUMN_CATNAME = "cat_name";
    public static final String QUESTIONS_COLUMN_MODNAME = "mod_name";
    public static final String QUESTIONS_COLUMN_IMGNAME = "i_name";

    public static String[] allOptColumns = {OPTIONS_COLUMN_ID,
            OPTIONS_COLUMN_TEXT, OPTIONS_COLUMN_QID};
    public static String[] allQstColumns = {QUESTIONS_COLUMN_ID, QUESTIONS_COLUMN_TEXT,
            QUESTIONS_COLUMN_CHECK, QUESTIONS_COLUMN_ANSID, QUESTIONS_COLUMN_CATNAME,
            QUESTIONS_COLUMN_MODNAME, QUESTIONS_COLUMN_IMGNAME};


    public MyDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}
