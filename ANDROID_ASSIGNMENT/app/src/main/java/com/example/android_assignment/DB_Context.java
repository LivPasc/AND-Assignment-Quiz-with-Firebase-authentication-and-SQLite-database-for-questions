package com.example.android_assignment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import com.example.android_assignment.QuizContract.*;

public class DB_Context extends SQLiteOpenHelper {
    private static final String DB_NAME = "QuizDataBase.db";
    private static final int DB_VERSION = 1;

    private SQLiteDatabase db;

    public DB_Context(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        final String SQL_CREATE_QUESTION_TABLE = "CREATE TABLE " +
                QuestionsView.TABLE_NAME + "( " +
                QuestionsView._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuestionsView.QUESTION_COLUMN + " TEXT, " +
                QuestionsView.OPTION1_COL + " TEXT, " +
                QuestionsView.OPTION2_COL + " TEXT, " +
                QuestionsView.OPTION3_COL + " TEXT, " +
                QuestionsView.OPTION4_COL + " TEXT, " +
                QuestionsView.ANSWER_NR_COL + " INTEGER" +
                ")";
        db.execSQL(SQL_CREATE_QUESTION_TABLE);
        addQuestions();
    }

    private void addQuestions() {
        QuizQuestion q1 = new QuizQuestion("Is it possible to have an activity without UI to perform action/actions? ", "Not possible", "Wrong question", "Yes, it is possible", "None of the above", 3);
        addToDB(q1);
        QuizQuestion q2 = new QuizQuestion("What is splash screen in android?", "Initial activity of an application", "Initial service of an application", "Initial method of an application", "Initial screen of an application", 4);
        addToDB(q2);
        QuizQuestion q3 = new QuizQuestion("How to stop the services in android? ", "finish()", "system.exit().", "By manually", "stopSelf() and stopService()", 4);
        addToDB(q3);
        QuizQuestion q4 = new QuizQuestion("What is sleep mode in android?", "Only Radio interface layer and alarm are in active mode", "Switched off", "Air plane mode", "None of the above", 1);
        addToDB(q4);
        QuizQuestion q5 = new QuizQuestion("Persist data can be stored in Android through", "Shared Preferences", "Internal/External storage and SQlite", "Network servers", "All of above", 4);
        addToDB(q5);
        QuizQuestion q6 = new QuizQuestion("What is LastKnownLocation in android?", "To find the last location of a phone", "To find known location of a phone", "To find the last known location of a phone", "None of the above", 3);
        addToDB(q6);
        QuizQuestion q7 = new QuizQuestion("What does httpclient.execute() returns in android?", "Http entity", "Http response", "Http result", "None of the above", 2);
        addToDB(q7);
        QuizQuestion q8 = new QuizQuestion("What is fragment in android?", "JSON", "Peace of Activity", "Layout", "None of the above", 2);
        addToDB(q8);
        QuizQuestion q9 = new QuizQuestion("What are return types of startActivityForResult() in android?", "RESULT_OK", "RESULT_CANCEL", "RESULT_CRASH", "A & B", 4);
        addToDB(q9);
        QuizQuestion q10 = new QuizQuestion("Can a class be immutable in android?", "No, it can't", "Yes, Class can be immutable", "Can't make the class as final class", "How?", 2);
        addToDB(q10);
    }

    private void addToDB(QuizQuestion question) {
        ContentValues cv = new ContentValues();
        cv.put(QuestionsView.QUESTION_COLUMN, question.getQ());
        cv.put(QuestionsView.OPTION1_COL, question.getO1());
        cv.put(QuestionsView.OPTION2_COL, question.getO2());
        cv.put(QuestionsView.OPTION3_COL, question.getO3());
        cv.put(QuestionsView.OPTION4_COL, question.getO4());
        cv.put(QuestionsView.ANSWER_NR_COL, question.getAnsNr());
        db.insert(QuestionsView.TABLE_NAME, null, cv);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + QuestionsView.TABLE_NAME);
        onCreate(db);
    }

    public ArrayList<QuizQuestion> getAllQuestions() {
        ArrayList<QuizQuestion> questionList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + QuestionsView.TABLE_NAME, null);
        if (c.moveToFirst()) {
            do {
                QuizQuestion question = new QuizQuestion();
                question.setQ(c.getString(c.getColumnIndex(QuestionsView.QUESTION_COLUMN)));
                question.setO1(c.getString(c.getColumnIndex(QuestionsView.OPTION1_COL)));
                question.setO2(c.getString(c.getColumnIndex(QuestionsView.OPTION2_COL)));
                question.setO3(c.getString(c.getColumnIndex(QuestionsView.OPTION3_COL)));
                question.setO4(c.getString(c.getColumnIndex(QuestionsView.OPTION4_COL)));
                question.setAnsNr(c.getInt(c.getColumnIndex(QuestionsView.ANSWER_NR_COL)));
                questionList.add(question);
            } while (c.moveToNext());
        }
        c.close();
        return questionList;
    }
}
