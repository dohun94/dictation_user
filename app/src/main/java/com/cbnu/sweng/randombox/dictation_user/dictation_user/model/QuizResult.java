
package com.cbnu.sweng.randombox.dictation_user.dictation_user.model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QuizResult implements Serializable{

    @SerializedName("rank")
    @Expose
    private Integer rank;
    @SerializedName("quiz")
    @Expose
    private Quiz quiz;
    @SerializedName("student_name")
    @Expose
    private String studentName;
    @SerializedName("score")
    @Expose
    private Integer score;
    @SerializedName("question_results")
    @Expose
    private List<QuestionResult> questionResult = null;
    @SerializedName("rectify_count")
    @Expose
    private RectifyCount rectifyCount;

    public Integer getRank() {
      return rank;
    }
    public void setRank(Integer rank) {
      this.rank = rank;
    }
    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public List<QuestionResult> getQuestionResult() {
        return questionResult;
    }

    public void setQuestionResult(List<QuestionResult> questionResult) {
        this.questionResult = questionResult;
    }

    public RectifyCount getRectifyCount() {
      return rectifyCount;
    }

    public void setRectifyCount(RectifyCount rectifyCount) {
      this.rectifyCount = rectifyCount;
    }

}
