package com.example.android_assignment;

import android.os.Parcel;
import android.os.Parcelable;

public class QuizQuestion implements Parcelable {
    private String q;
    private String o1;
    private String o2;
    private String o3;
    private String o4;
    private int ansNr;

    public QuizQuestion() {
    }

    public QuizQuestion(String q, String o1, String o2, String o3, String o4, int ansNr) {
        this.q = q;
        this.o1 = o1;
        this.o2 = o2;
        this.o3 = o3;
        this.o4 = o4;
        this.ansNr = ansNr;
    }

    protected QuizQuestion(Parcel in) {
        q = in.readString();
        o1 = in.readString();
        o2 = in.readString();
        o3 = in.readString();
        o4 = in.readString();
        ansNr = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(q);
        dest.writeString(o1);
        dest.writeString(o2);
        dest.writeString(o3);
        dest.writeString(o4);
        dest.writeInt(ansNr);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<QuizQuestion> CREATOR = new Creator<QuizQuestion>() {
        @Override
        public QuizQuestion createFromParcel(Parcel in) {
            return new QuizQuestion(in);
        }

        @Override
        public QuizQuestion[] newArray(int size) {
            return new QuizQuestion[size];
        }
    };

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public String getO1() {
        return o1;
    }

    public void setO1(String o1) {
        this.o1 = o1;
    }

    public String getO4() {
        return o4;
    }

    public void setO4(String o4) {
        this.o4 = o4;
    }

    public String getO2() {
        return o2;
    }

    public void setO2(String o2) {
        this.o2 = o2;
    }

    public String getO3() {
        return o3;
    }

    public void setO3(String o3) {
        this.o3 = o3;
    }

    public int getAnsNr() {
        return ansNr;
    }

    public void setAnsNr(int ansNr) {
        this.ansNr = ansNr;
    }
}
