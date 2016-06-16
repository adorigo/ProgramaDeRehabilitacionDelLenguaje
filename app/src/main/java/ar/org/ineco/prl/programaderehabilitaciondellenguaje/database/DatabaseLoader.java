package ar.org.ineco.prl.programaderehabilitaciondellenguaje.database;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.ApplicationContext;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.Category;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.ImageFile;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.Level;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.Option;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.Question;
import ar.org.ineco.prl.programaderehabilitaciondellenguaje.classes.SoundFile;

public class DatabaseLoader {

    private static DatabaseLoader ourInstance;

    public static DatabaseLoader getInstance () {

        if (ourInstance == null) {
            ourInstance = new DatabaseLoader();
        }
        return ourInstance;
    }

    private DatabaseLoader () {

        dbHelper = new MyDatabase(ApplicationContext.get().getApplicationContext());
    }

    private MyDatabase dbHelper;
    private SQLiteDatabase database;

    public void openWritable () throws SQLException {

        database = dbHelper.getWritableDatabase();
    }

    public void openReadable () throws SQLException {

        database = dbHelper.getReadableDatabase();
    }

    public void close () {

        dbHelper.close();
    }

    public List<Question> getAllQuestions (Level thisLevel) {

        openReadable();

        Map<Long, Question> allQuestions = new HashMap<>();

        String query = "SELECT * FROM " + MyDatabase.TABLE_QUESTION;
        String condition = " WHERE " + MyDatabase.QUESTION_COLUMN_CHECK + " = 0";
        condition += " AND " + MyDatabase.QUESTION_COLUMN_LVLID + " = " + thisLevel.getId();
        Cursor cursor = database.rawQuery(query + condition, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            Question Question = cursorToQuestion(cursor);

            Question.addImages(getAllQuestionsImages(Question.getId()));

            allQuestions.put(Question.getId(), Question);

            cursor.moveToNext();
        }

        if (allQuestions.size() > 0) {

            List<Option> allOptions = getAllOptions(allQuestions.keySet());

            for (Option opt : allOptions) {
                allQuestions.get(opt.getQid()).addOption(opt);
            }
        }

        cursor.close();

        // Load question hierarchy
        List<Question> questions = new ArrayList<>();

        for (Question question : allQuestions.values()) {

            if (question.hasParent()) {

                allQuestions.get(question.getParentQuestion()).addChildQuestion(question);

            } else {

                questions.add(question);
            }
        }

        close();

        return questions;
    }

    private List<ImageFile> getAllQuestionsImages (Long questionID) {

        Set<Long> idsImages = new HashSet<>();

        List<ImageFile> images = new ArrayList<>();

        String query = "SELECT * FROM " + MyDatabase.TABLE_PREGIMG;
        String condition = " WHERE " + MyDatabase.PREGIMG_COLUMN_QID + " = " + questionID;
        Cursor cursor = database.rawQuery(query + condition, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            Long imgId = cursor.getLong(cursor.getColumnIndex(MyDatabase.PREGIMG_COLUMN_IMGID));

            idsImages.add(imgId);

            cursor.moveToNext();
        }

        cursor.close();

        if (idsImages.size() > 0) {

            images.addAll(getAllImages(idsImages));
        }

        return images;
    }

    private List<ImageFile> getAllImages (Set<Long> idsImages) {

        List<ImageFile> images = new ArrayList<>();

        String query = "SELECT * FROM " + MyDatabase.TABLE_IMG;
        String condition = " WHERE " + MyDatabase.IMG_COLUMN_ID + " IN (";

        for (Long idImg : idsImages) {
            condition += String.valueOf(idImg) + ",";
        }
        condition = condition.substring(0, condition.length() - 1);
        condition += ");";

        Cursor cursor = database.rawQuery(query + condition, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            ImageFile image = cursorToImage(cursor);
            images.add(image);
            cursor.moveToNext();
        }

        cursor.close();

        return images;
    }

    private ImageFile cursorToImage (Cursor cursor) {

        return new ImageFile(cursor.getLong(cursor.getColumnIndex(MyDatabase.IMG_COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(MyDatabase.IMG_COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(MyDatabase.IMG_COLUMN_TXTIMG))
        );
    }

    private SoundFile cursorToSound(Cursor cursor) {

        return new SoundFile(cursor.getLong(cursor.getColumnIndex(MyDatabase.SND_COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(MyDatabase.SND_COLUMN_NAME))
        );
    }

    private Question cursorToQuestion (Cursor cursor) {

        return new Question(cursor.getLong(cursor.getColumnIndex(MyDatabase.QUESTION_COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(MyDatabase.QUESTION_COLUMN_TXT)),
                cursor.getInt(cursor.getColumnIndex(MyDatabase.QUESTION_COLUMN_CHECK)),
                cursor.getLong(cursor.getColumnIndex(MyDatabase.QUESTION_COLUMN_QID))
        );
    }

    public List<Option> getAllOptions(Set<Long> idsQuestions) {

        List<Option> Options = new ArrayList<>();

        String query = "SELECT *" +
                " FROM " + MyDatabase.TABLE_OPTION + "" +
                " LEFT JOIN " + MyDatabase.TABLE_IMG + " ON " + MyDatabase.TABLE_IMG + "." + MyDatabase.IMG_COLUMN_ID +
                " = " + MyDatabase.TABLE_OPTION + "." + MyDatabase.OPTION_COLUMN_IMGID +
                " LEFT JOIN " + MyDatabase.TABLE_SND + " ON " + MyDatabase.TABLE_SND + "." + MyDatabase.SND_COLUMN_ID +
                " = " + MyDatabase.TABLE_OPTION + "." + MyDatabase.OPTION_COLUMN_SNDID;
        String condition = " WHERE " + MyDatabase.OPTION_COLUMN_QID + " IN (";

        for (Long idQst : idsQuestions) {
            condition += String.valueOf(idQst) + ",";
        }

        condition = condition.substring(0, condition.length() - 1); // remove trailing comma
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

    private Option cursorToOption (Cursor cursor) {
        Log.d(DatabaseLoader.class.getName(), "For Option: " + cursor.getLong(cursor.getColumnIndex(MyDatabase.OPTION_COLUMN_ID)) + ' ' +
                cursor.getString(cursor.getColumnIndex(MyDatabase.OPTION_COLUMN_TXT)));
        return new Option(cursor.getLong(cursor.getColumnIndex(MyDatabase.OPTION_COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(MyDatabase.OPTION_COLUMN_TXT)),
                cursor.getInt(cursor.getColumnIndex(MyDatabase.OPTION_COLUMN_CORR)),
                cursor.getLong(cursor.getColumnIndex(MyDatabase.OPTION_COLUMN_QID)),
                (cursor.getLong(cursor.getColumnIndex(MyDatabase.OPTION_COLUMN_IMGID)) == 0) ? null : cursorToOptionImage(cursor),
                (cursor.getLong(cursor.getColumnIndex(MyDatabase.OPTION_COLUMN_SNDID)) == 0) ? null : cursorToOptionSound(cursor)
        );
    }

    private ImageFile cursorToOptionImage (Cursor cursor) {

        return new ImageFile(cursor.getLong(cursor.getColumnIndex(MyDatabase.OPTION_COLUMN_IMGID)),
                cursor.getString(cursor.getColumnIndex(MyDatabase.IMG_COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(MyDatabase.IMG_COLUMN_TXTIMG))
        );
    }

    private SoundFile cursorToOptionSound (Cursor cursor) {

        return new SoundFile(cursor.getLong(cursor.getColumnIndex(MyDatabase.OPTION_COLUMN_SNDID)),
                cursor.getString(cursor.getColumnIndex(MyDatabase.SND_COLUMN_NAME))
        );
    }

    public List<Level> getAllLevels (Category thisCategory) {

        List<Level> levels = new ArrayList<>();

        String query = "SELECT * FROM " + MyDatabase.TABLE_LVL;
        String condition = " WHERE " + MyDatabase.LVL_COLUMN_CID + " = " + thisCategory.getId();
        String order = " ORDER BY " + MyDatabase.LVL_COLUMN_NUMBER + " ASC";
        Cursor cursor = database.rawQuery(query + condition + order, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            Level lvl = cursorToLevel(cursor);

            //lvl.addAllQuestions(getAllQuestions(lvl));

            levels.add(lvl);

            cursor.moveToNext();
        }

        cursor.close();

        return levels;
    }

    private Level cursorToLevel (Cursor cursor) {

        return new Level(cursor.getLong(cursor.getColumnIndex(MyDatabase.LVL_COLUMN_ID)),
                cursor.getInt(cursor.getColumnIndex(MyDatabase.LVL_COLUMN_NUMBER)),
                cursor.getInt(cursor.getColumnIndex(MyDatabase.LVL_COLUMN_LVLDONE)),
                cursor.getLong(cursor.getColumnIndex(MyDatabase.LVL_COLUMN_CID))
        );
    }

    public Category getCategories () {

        openReadable();

        Map<Long, Category> categories = new HashMap<>();

        String query = "SELECT * FROM " + MyDatabase.TABLE_CATEGORY;
        Cursor cursor = database.rawQuery(query, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            Category cat = cursorToCategory(cursor);
            categories.put(cat.getId(), cat);
            cursor.moveToNext();
        }

        cursor.close();

        //Load Category sounds
        query = "SELECT " + MyDatabase.TABLE_CATEGORY +'.'+MyDatabase.CATEGORY_COLUMN_ID + ", " + MyDatabase.CATEGORY_COLUMN_SND + ", " + MyDatabase.SND_COLUMN_NAME + ' ' +
                "FROM " + MyDatabase.TABLE_CATEGORY + " LEFT JOIN " + MyDatabase.TABLE_SND + ' ' +
                "ON " + MyDatabase.TABLE_SND +'.'+MyDatabase.SND_COLUMN_ID + " = " + MyDatabase.TABLE_CATEGORY +'.'+MyDatabase.CATEGORY_COLUMN_SND;
        cursor = database.rawQuery(query, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            if(cursor.getLong(cursor.getColumnIndex(MyDatabase.CATEGORY_COLUMN_SND)) != 0 ){

                Long idCategory = cursor.getLong(cursor.getColumnIndex(MyDatabase.CATEGORY_COLUMN_ID));
                SoundFile snd = cursorToCategorySound(cursor);
                categories.get(idCategory).setSnd(snd);
            }

            cursor.moveToNext();
        }

        cursor.close();

        // Load Category hierarchy
        query = "SELECT " + MyDatabase.CATEGORY_COLUMN_ID + ", " + MyDatabase.CATEGORY_COLUMN_CID + " FROM " + MyDatabase.TABLE_CATEGORY;
        String condition = " WHERE " + MyDatabase.CATEGORY_COLUMN_CID + "!= ''";
        cursor = database.rawQuery(query + condition, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            Long idChildCategory = cursor.getLong(cursor.getColumnIndex(MyDatabase.CATEGORY_COLUMN_ID));
            Long idParentCategory = cursor.getLong(cursor.getColumnIndex(MyDatabase.CATEGORY_COLUMN_CID));

            Log.d(DatabaseLoader.class.getName(), "Adding " + idChildCategory.toString() + " to " + idParentCategory.toString());

            categories.get(idChildCategory).setParentCategory(categories.get(idParentCategory));

            categories.get(idParentCategory).addChildren(categories.get(idChildCategory));

            cursor.moveToNext();
        }

        List<Category> tempList = new ArrayList<>(categories.values());

        for (Category category : tempList) {

            if (!category.hasChildren() && category.hasParent()) {

                category.addAllLevels(getAllLevels(category));

                categories.remove(category.getId());

            } else if (category.hasChildren() && category.hasParent()) {

                categories.remove(category.getId());
            }
        }

        cursor.close();

        close();

        return new ArrayList<>(categories.values()).get(0);
    }

    private Category cursorToCategory (Cursor cursor) {

        return new Category(cursor.getLong(cursor.getColumnIndex(MyDatabase.CATEGORY_COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(MyDatabase.CATEGORY_COLUMN_NAME)),
                null
        );
    }

    private SoundFile cursorToCategorySound (Cursor cursor) {

        return new SoundFile(cursor.getLong(cursor.getColumnIndex(MyDatabase.CATEGORY_COLUMN_SND)),
                cursor.getString(cursor.getColumnIndex(MyDatabase.SND_COLUMN_NAME))
        );
    }

    public void checkLevel (Level level) {

        openWritable();

        long id = level.getId();

        Log.d(DatabaseLoader.class.getName(), "Level checked with id: " + id);
        ContentValues contentValues = new ContentValues();
        contentValues.put(MyDatabase.LVL_COLUMN_LVLDONE, 1);
        database.update(MyDatabase.TABLE_LVL, contentValues, MyDatabase.LVL_COLUMN_ID
                + " = " + id, null);

        close();
    }

    public void checkQuestion (Question question) {

        openWritable();

        long id = question.getId();

        Log.d(DatabaseLoader.class.getName(), "Question checked with id: " + id);
        ContentValues contentValues = new ContentValues();
        contentValues.put(MyDatabase.QUESTION_COLUMN_CHECK, 1);
        database.update(MyDatabase.TABLE_QUESTION, contentValues, MyDatabase.QUESTION_COLUMN_ID
                + " = " + id, null);

        close();
    }

    public void resetLevel (Level thisLevel) {

        openWritable();

        ContentValues contentValues = new ContentValues();
        contentValues.put(MyDatabase.QUESTION_COLUMN_CHECK, 0);

        String condition = MyDatabase.QUESTION_COLUMN_LVLID + " = " + thisLevel.getId();

        int rows = database.update(MyDatabase.TABLE_QUESTION, contentValues, condition, null);
        Log.d(DatabaseLoader.class.getName(), "Reseting all questions for level " + thisLevel.getId()
                + " with " + rows + " affected.");

        close();
    }
}
