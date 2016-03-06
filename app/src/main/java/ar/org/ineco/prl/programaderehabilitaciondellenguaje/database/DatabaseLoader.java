package ar.org.ineco.prl.programaderehabilitaciondellenguaje.database;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.ApplicationContext;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.ImageFile;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.Option;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.Question;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.Category;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.Level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DatabaseLoader {

    private static DatabaseLoader ourInstance;

    public static DatabaseLoader getInstance() {
        if(ourInstance == null){
            ourInstance = new DatabaseLoader();
        }
        return ourInstance;
    }

    private DatabaseLoader() {
        dbHelper = new MyDatabase(ApplicationContext.get().getApplicationContext());
    }

    private MyDatabase dbHelper;
    private SQLiteDatabase database;

    public void openWritable() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void openReadable() throws SQLException {
        database = dbHelper.getReadableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public List<Question> getAllQuestions(long thisLevel) {

        openReadable();

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

        close();

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

    public Map<Long, Level> getLevels() {

        openReadable();

        Map<Long, Level> levels = new HashMap();

        String query = "SELECT * FROM " + MyDatabase.TABLE_LVL;
        Cursor cursor = database.rawQuery(query, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Level lvl = cursorToLevel(cursor);
            levels.put(lvl.getLvlId(), lvl);
            cursor.moveToNext();
        }

        cursor.close();

        close();

        return levels;
    }

    private Level cursorToLevel(Cursor cursor) {
        return new Level(cursor.getLong(cursor.getColumnIndex(MyDatabase.LVL_COLUMN_ID)),
                cursor.getInt(cursor.getColumnIndex(MyDatabase.LVL_COLUMN_NUMBER)),
                cursor.getLong(cursor.getColumnIndex(MyDatabase.LVL_COLUMN_CID))
        );
    }

    public Map<Long, Category> getCategories() {

        openReadable();

        Map<Long, Category> categories = new HashMap();

        String query = "SELECT * FROM " + MyDatabase.TABLE_CATEGORY;
        Cursor cursor = database.rawQuery(query, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Category cat = cursorToCategory(cursor);
            categories.put(cat.getCatNumber(), cat);
            cursor.moveToNext();
        }

        cursor.close();

        query = "SELECT "+MyDatabase.CATEGORY_COLUMN_ID+", "+MyDatabase.CATEGORY_COLUMN_CID+" FROM " + MyDatabase.TABLE_CATEGORY;
        String condition = " WHERE " + MyDatabase.CATEGORY_COLUMN_CID + "!= ''";
        cursor = database.rawQuery(query + condition, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            Long idChildCategory = cursor.getLong(cursor.getColumnIndex(MyDatabase.CATEGORY_COLUMN_ID));
            Long idParentCategory = cursor.getLong(cursor.getColumnIndex(MyDatabase.CATEGORY_COLUMN_CID));

            categories.get(idParentCategory).addChildren(categories.get(idChildCategory));
            Log.d(DatabaseLoader.class.getName(), "Adding " + idChildCategory.toString() + " to " + idParentCategory.toString());
            cursor.moveToNext();
        }

        cursor.close();

        close();

        return categories;
    }

    private Category cursorToCategory(Cursor cursor) {
        return new Category(cursor.getLong(cursor.getColumnIndex(MyDatabase.CATEGORY_COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(MyDatabase.CATEGORY_COLUMN_NAME)),
                cursor.getLong(cursor.getColumnIndex(MyDatabase.CATEGORY_COLUMN_CID))
        );
    }
}
