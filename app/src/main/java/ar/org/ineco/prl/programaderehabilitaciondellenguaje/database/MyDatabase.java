package ar.org.ineco.prl.programaderehabilitaciondellenguaje.database;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/*
    To facilitate a database upgrade, increment the version number that you pass to your
    SQLiteAssetHelper constructor:

    - private static final int DATABASE_VERSION = <to_version>;

    Update the initial SQLite database in the project's assets/databases directory with the changes
    and create a text file containing all required SQL commands to upgrade the database from
    its previous version to it's current version and place it in the same folder.

    The required naming convention for this upgrade file is as follows:

    - assets/databases/<database_name>_upgrade_<from_version>-<to_version>.sql

    Generating upgrade scripts

    You can use 3rd party tools to automatically generate the SQL required to modify a database
    from one schema version to another. One such application is SQLite Compare Utility for Windows.
*/

public class MyDatabase extends SQLiteAssetHelper {

    public static final String TABLE_OPTION = "opcion";
    public static final String OPTION_COLUMN_ID = "_id";
    public static final String OPTION_COLUMN_SNDID = "opc_snd";
    public static final String OPTION_COLUMN_IMGID = "opc_img";
    public static final String OPTION_COLUMN_TXT = "opc_txt";
    public static final String OPTION_COLUMN_CORR = "opc_correcta";
    public static final String OPTION_COLUMN_QID = "preg_id";

    public static final String TABLE_QUESTION = "pregunta";
    public static final String QUESTION_COLUMN_ID = "_id";
    public static final String QUESTION_COLUMN_TXT = "preg_consignatxt";
    public static final String QUESTION_COLUMN_CHECK = "preg_check";
    public static final String QUESTION_COLUMN_SND = "preg_consignasnd";
    public static final String QUESTION_COLUMN_QID = "preg_id";
    public static final String QUESTION_COLUMN_LVLID = "niv_id";

    public static final String TABLE_PREGIMG = "preg_img";
    public static final String PREGIMG_COLUMN_ID = "_id";
    public static final String PREGIMG_COLUMN_QID = "preg_id";
    public static final String PREGIMG_COLUMN_IMGID = "img_id";

    public static final String TABLE_REGUSE = "registro_de_uso";
    public static final String REGUSE_COLUMN_ID = "_id";
    public static final String REGUSE_COLUMN_QID = "preg_id";
    public static final String REGUSE_COLUMN_CORR = "ru_correcto";

    public static final String TABLE_SND = "sonido";
    public static final String SND_COLUMN_ID = "_id";
    public static final String SND_COLUMN_NAME = "snd_nombre";

    public static final String TABLE_CATEGORY = "categoria";
    public static final String CATEGORY_COLUMN_ID = "_id";
    public static final String CATEGORY_COLUMN_NAME = "cat_nombre";
    public static final String CATEGORY_COLUMN_CID = "cat_id";
    public static final String CATEGORY_COLUMN_SND = "cat_snd";

    public static final String TABLE_IMG = "imagen";
    public static final String IMG_COLUMN_ID = "_id";
    public static final String IMG_COLUMN_NAME = "img_nombre";
    public static final String IMG_COLUMN_TXTIMG = "txt_nombre";

    public static final String TABLE_LVL = "nivel";
    public static final String LVL_COLUMN_ID = "_id";
    public static final String LVL_COLUMN_NUMBER = "niv_num";
    public static final String LVL_COLUMN_LVLDONE = "niv_term";
    public static final String LVL_COLUMN_CID = "cat_id";

    public static final String TABLE_SETTING = "configuracion";
    public static final String SETTING_COLUMN_ID = "_id";
    public static final String SETTING_COLUMN_NAME = "conf_nombre";
    public static final String SETTING_COLUMN_VALUE = "conf_valor";


    public static String[] allOptColumns = {OPTION_COLUMN_ID, OPTION_COLUMN_SNDID,
            OPTION_COLUMN_IMGID, OPTION_COLUMN_TXT, OPTION_COLUMN_CORR, OPTION_COLUMN_QID};

    public static String[] allQstColumns = {QUESTION_COLUMN_ID, QUESTION_COLUMN_TXT,
            QUESTION_COLUMN_CHECK, QUESTION_COLUMN_SND, QUESTION_COLUMN_QID,
            QUESTION_COLUMN_LVLID};

    public static String[] allPregimgColumns = {PREGIMG_COLUMN_ID, PREGIMG_COLUMN_QID, PREGIMG_COLUMN_IMGID};

    public static String[] allREGUSEColumns = {REGUSE_COLUMN_ID, REGUSE_COLUMN_QID, REGUSE_COLUMN_CORR};

    public static String[] allCategoryColumns = {CATEGORY_COLUMN_ID, CATEGORY_COLUMN_NAME, CATEGORY_COLUMN_CID,
                                                CATEGORY_COLUMN_SND};

    public static String[] allIMGColumns = {IMG_COLUMN_ID, IMG_COLUMN_NAME, IMG_COLUMN_TXTIMG};

    public static String[] allSNDColumns = {SND_COLUMN_ID, SND_COLUMN_NAME};

    public static String[] allLVLColumns = {LVL_COLUMN_ID, LVL_COLUMN_NUMBER, LVL_COLUMN_LVLDONE, LVL_COLUMN_CID};

    private static final String DATABASE_NAME = "database.db";
    private static final int DATABASE_VERSION = 1;

    public MyDatabase (Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}
