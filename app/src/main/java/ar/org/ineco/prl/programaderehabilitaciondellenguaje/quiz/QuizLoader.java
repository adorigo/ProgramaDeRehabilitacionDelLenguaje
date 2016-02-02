package ar.org.ineco.prl.programaderehabilitaciondellenguaje.quiz;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import ar.org.ineco.prl.programaderehabilitaciondellenguaje.MyDatabase;

import java.util.ArrayList;
import java.util.List;

public class QuizLoader {
    private MyDatabase dbHelper;
    private SQLiteDatabase database;

    
    public QuizLoader(Context context){
        dbHelper = new MyDatabase(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public List<Question> getAllQuestions() {
        List<Question> allQuestions = new ArrayList<Question>();
        List<Option> allOptions = getAllOptions();
        String query = "SELECT * FROM " + MyDatabase.TABLE_VIEW_QUESTIONS;
        String condition = " WHERE " + MyDatabase.QUESTIONS_COLUMN_CHECK + " = 0";
        Cursor cursor = database.rawQuery(query + condition, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Question Question = cursorToQuestion(cursor);
            while (!cursor.isAfterLast() && Question.getId() == cursor.getLong(0)) {
                Question.addImage(cursor.getString(cursor.getColumnIndex(MyDatabase.QUESTIONS_COLUMN_IMGNAME)));
                cursor.moveToNext();
            }
            List<Option> qOptions = new ArrayList<Option>();
            for(Option opt: allOptions){
                if(opt.getQid()==Question.getId()){
                    qOptions.add(opt);
                }
            }
            Question.setOpts(qOptions);
            allQuestions.add(Question);
        }

        cursor.close();
        return allQuestions;
    }

    public void checkQuestion(Question question) {
        long id = question.getId();
        Log.d(QuizLoader.class.getName(),"Question checked with id: " + id);
        ContentValues contentValues= new ContentValues();
        contentValues.put(MyDatabase.QUESTIONS_COLUMN_CHECK, 1);
        database.update(MyDatabase.TABLE_QUESTIONS, contentValues, MyDatabase.QUESTIONS_COLUMN_ID
                + " = " + id, null);
    }

    public void resetQuestions(){
        ContentValues contentValues= new ContentValues();
        contentValues.put(MyDatabase.QUESTIONS_COLUMN_CHECK,0);
        int rows = database.update(MyDatabase.TABLE_QUESTIONS, contentValues, null, null);
        Log.d(QuizLoader.class.getName(), "Reseting all questions with "+ rows + " affected.");
    }

    private Question cursorToQuestion(Cursor cursor) {
        Question Question= new Question(cursor.getLong(0), cursor.getLong(2),cursor.getString(1));
        return Question;
    }

    private List<Option> getAllOptions() {
        List<Option> Options = new ArrayList<Option>();

        Cursor cursor = database.query(MyDatabase.TABLE_OPTIONS,
                MyDatabase.allOptColumns, null, null, null, null, null);

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
        Option Option = new Option(cursor.getLong(0), cursor.getString(1), cursor.getLong(2));
        return Option;
    }

}
