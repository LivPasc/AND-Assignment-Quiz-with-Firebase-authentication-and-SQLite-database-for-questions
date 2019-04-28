package com.example.android_assignment;

import android.provider.BaseColumns;

public final class QuizContract {
    public QuizContract() {
    }

    public static class QuestionsView implements BaseColumns {
        public static final String TABLE_NAME = "quiz_questions";
        public static final String QUESTION_COLUMN = "question";
        public static final String OPTION1_COL = "option1";
        public static final String OPTION2_COL = "option2";
        public static final String OPTION3_COL = "option3";
        public static final String OPTION4_COL = "option4";
        public static final String ANSWER_NR_COL = "ans_nr";
    }
}
