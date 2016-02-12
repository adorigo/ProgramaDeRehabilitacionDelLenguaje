package ar.org.ineco.prl.programaderehabilitaciondellenguaje.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.ImageFile;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.Option;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.Question;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.Category;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.Level;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.Module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DatabaseLoader {
    private MyDatabase dbHelper;
    private SQLiteDatabase database;


    public DatabaseLoader(Context context) {
        dbHelper = new MyDatabase(context);
    }

    public void openWritable() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void openReadable() throws SQLException {
        database = dbHelper.getReadableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public List<Question> getAllQuestions(String thisLevel) {

        Map<Long, Question> allQuestions = new HashMap<>();

        String query = "SELECT * FROM " + MyDatabase.TABLE_QUESTION;
        String condition = " WHERE " + MyDatabase.QUESTION_COLUMN_CHECK + " = 0";
        condition += " AND " + MyDatabase.QUESTION_COLUMN_LVLID + " = " + thisLevel;
        Cursor cursor = database.rawQuery(query + condition, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            Question Question = cursorToQuestion(cursor);

            while (!cursor.isAfterLast() && Question.getId() == cursor.getLong(0)) {

                Question.addImages(getAllQuestionsImages(Question.getId()));

                cursor.moveToNext();
            }

            allQuestions.put(Question.getId(), Question);
        }

        List<Option> allOptions = getAllOptions(allQuestions.keySet());

        for (Option opt : allOptions) {
            allQuestions.get(opt.getQid()).addOption(opt);
        }

        cursor.close();

        return (List<Question>) allQuestions.values();
    }

    private List<ImageFile> getAllQuestionsImages(Long questionID) {

        Set<Long> idsImages = new HashSet<>();

        String query = "SELECT * FROM " + MyDatabase.TABLE_PREGIMG;
        String condition = " WHERE " + MyDatabase.QUESTION_COLUMN_ID + " = " + questionID;
        Cursor cursor = database.rawQuery(query + condition, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            Long imgId = cursor.getLong(cursor.getColumnIndex(MyDatabase.PREGIMG_COLUMN_IMGID));

            idsImages.add(imgId);

            cursor.moveToNext();
        }

        cursor.close();

        return getAllImages(idsImages);
    }

    private List<ImageFile> getAllImages(Set<Long> idsImages) {

        List<ImageFile> images = new ArrayList<>();

        String query = "SELECT * FROM " + MyDatabase.TABLE_IMG;
        String condition = " WHERE " + MyDatabase.IMG_COLUMN_ID + " IN (";

        for(Long idImg : idsImages){
            condition += String.valueOf(idImg) + ",";
        }
        condition = condition.substring(0, condition.length() - 1);
        condition += ");";

        Cursor cursor = database.rawQuery(query + condition, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()){

            ImageFile image = cursorToImage(cursor);
            images.add(image);
            cursor.moveToNext();
        }

        cursor.close();

        return images;
    }

    private ImageFile cursorToImage(Cursor cursor) {
        return new ImageFile(cursor.getLong(cursor.getColumnIndex(MyDatabase.IMG_COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(MyDatabase.IMG_COLUMN_NAME)));
    }

    public void checkQuestion(Question question) {
        long id = question.getId();
        Log.d(DatabaseLoader.class.getName(), "Question checked with id: " + id);
        ContentValues contentValues = new ContentValues();
        contentValues.put(MyDatabase.QUESTION_COLUMN_CHECK, 1);
        database.update(MyDatabase.TABLE_QUESTION, contentValues, MyDatabase.QUESTION_COLUMN_ID
                + " = " + id, null);
    }

    public void resetQuestions() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MyDatabase.QUESTION_COLUMN_CHECK, 0);
        int rows = database.update(MyDatabase.TABLE_QUESTION, contentValues, null, null);
        Log.d(DatabaseLoader.class.getName(), "Reseting all questions with " + rows + " affected.");
    }

    private Question cursorToQuestion(Cursor cursor) {
        return new Question(cursor.getLong(cursor.getColumnIndex(MyDatabase.QUESTION_COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(MyDatabase.QUESTION_COLUMN_TXT)));
    }

    private List<Option> getAllOptions(Set<Long> idsQuestions) {
        List<Option> Options = new ArrayList<>();

        String query = "SELECT * FROM " + MyDatabase.TABLE_OPTION;
        String condition = " WHERE " + MyDatabase.OPTION_COLUMN_QID + " IN (";

        for(Long idQst : idsQuestions){
            condition += String.valueOf(idQst) + ",";
        }
        condition = condition.substring(0, condition.length() - 1);
        condition += ");";

        Cursor cursor = database.rawQuery(query + condition, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            Option Option = cursorToOption(cursor);

            Options.add(Option);

            cursor.moveToNext();
        }

        cursor.close();

        return Options;
    }

    private Option cursorToOption(Cursor cursor) {
        return new Option(cursor.getLong(cursor.getColumnIndex(MyDatabase.OPTION_COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(MyDatabase.OPTION_COLUMN_TXT)),
                (int) cursor.getLong(cursor.getColumnIndex(MyDatabase.OPTION_COLUMN_CORR)),
                cursor.getLong(cursor.getColumnIndex(MyDatabase.OPTION_COLUMN_QID))
        );
    }

    public List<Level> getAllLevels(String thisCategory) {
        List<Level> allLevels = new ArrayList<>();
        String query = "SELECT * FROM " + MyDatabase.TABLE_LVL;
        String condition = " WHERE " + MyDatabase.CATEGORY_COLUMN_CID + " = " + thisCategory;
        Cursor cursor = database.rawQuery(query + condition, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Level lvl = cursorToLevel(cursor);
            allLevels.add(lvl);
            cursor.moveToNext();
        }

        cursor.close();
        return allLevels;
    }

    private Level cursorToLevel(Cursor cursor) {
        return new Level(cursor.getInt(0));
    }

    public String getCategoryName(String thisCategory) {
        String categoryName = "";
        String query = "SELECT cat_name FROM " + MyDatabase.TABLE_CATEGORY;
        String condition = " WHERE " + MyDatabase.CATEGORY_COLUMN_CID + " = " + thisCategory;
        Cursor cursor = database.rawQuery(query + condition, null);

        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            categoryName = cursor.getString(0);
        }
        cursor.close();
        return categoryName;
    }

    public List<Category> getAllCategories(String moduleID) {
        List<Category> allCategories = new ArrayList<>();
        String query = "SELECT * FROM " + MyDatabase.TABLE_CATEGORY;
        String condition = " WHERE " + MyDatabase.CATEGORY_COLUMN_MID + " = " + moduleID;
        Cursor cursor = database.rawQuery(query + condition, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Category cat = cursorToCategory(cursor);
            allCategories.add(cat);
            cursor.moveToNext();
        }

        cursor.close();
        return allCategories;
    }

    private Category cursorToCategory(Cursor cursor) {
        return new Category(cursor.getInt(0), cursor.getString(1));
    }

    public List<Module> getAllModules() {
        List<Module> allModules = new ArrayList<>();
        String query = "SELECT * FROM " + MyDatabase.TABLE_MODULE;
        Cursor cursor = database.rawQuery(query, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Module mod = cursorToModule(cursor);
            allModules.add(mod);
            cursor.moveToNext();
        }

        cursor.close();
        return allModules;
    }

    private Module cursorToModule(Cursor cursor) {
        return new Module(cursor.getInt(0), cursor.getString(1));
    }

    public String getModuleName(String thisModule) {
        String moduleName = "";
        String query = "SELECT mod_name FROM " + MyDatabase.TABLE_MODULE;
        String condition = " WHERE " + MyDatabase.MODULE_COLUMN_ID + " = " + thisModule;
        Cursor cursor = database.rawQuery(query + condition, null);

        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            moduleName = cursor.getString(0);
        }

        cursor.close();
        return moduleName;
    }
}
