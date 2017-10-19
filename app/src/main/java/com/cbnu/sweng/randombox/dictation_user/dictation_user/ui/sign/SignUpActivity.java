package com.cbnu.sweng.randombox.dictation_user.dictation_user.ui.sign;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.cbnu.sweng.randombox.dictation_user.dictation_user.ui.SelectExamOrPractice;
import com.cbnu.sweng.randombox.dictation_user.dictation_user.ui.base.BaseActivity;
import com.cbnu.sweng.randombox.dictation_user.dictation_user.utils.ApiRequester;

import com.cbnu.sweng.randombox.dictation_user.dictation_user.R;
import com.cbnu.sweng.randombox.dictation_user.dictation_user.model.School;
import com.cbnu.sweng.randombox.dictation_user.dictation_user.model.Student;

import com.sdsmdg.tastytoast.TastyToast;
import com.shawnlin.numberpicker.NumberPicker;

import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class SignUpActivity extends BaseActivity {

    SharedPreferences setting;
    SharedPreferences.Editor editor;

    private Handler mHandler;
    private Runnable mRunnable;

    private Button schoolsearch; // 다이얼로그 학교검색 버튼
    private EditText schoolname; // 다이얼로그에 있는 학교이름 란
    private String selectedschool; // 학교검색 API 로 넘어가는 학교 값
    private int temp;

    private String myname; // 실제 가입할 때 넘어가는 값들
    private String myschool;// 실제 가입할 때 넘어가는 값들
    private String myinfo; // 실제 가입할 때 넘어가는 값들
    private String myclass; // 실제 가입할 때 넘어가는 값들
    private String mygrade; // 실제 가입할 때 넘어가는 값들
    private int myStudentId; // 번호
    private String myteacher; // 실제 가입할 때 넘어가는 값들

    private String name[];
    private SweetAlertDialog pDialog;
    Spinner spState = null;

    Boolean isApplyMatching = false;

    @BindArray(R.array.strArrayCity)
    String [] strArrayCity;
    @BindView(R.id.etSchoolNameUp) EditText etSchoolNameUp;
    @BindView(R.id.etStudentInfoUp) EditText etStudentInfoUp;
    @BindView(R.id.etStudentNameUp) EditText etStudentNameUp;
    @BindView(R.id.etTeacherNameUp) EditText etTeacherNameUp;
    @BindView(R.id.btSignUp) Button btSignUp;
    @BindView(R.id.toolbar) Toolbar toolbar;



    @OnClick(R.id.etSchoolNameUp)
    void onClickEtSchoolNameUp(){

        final Spinner spCity = new Spinner(this);
        spState = new Spinner(this);

        final Button btSearchShool = new Button(this);
        btSearchShool.setBackground(this.getResources().getDrawable(R.drawable.ic_search));
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());
        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());
        btSearchShool.setLayoutParams(new LinearLayout.LayoutParams(width, height));

        final EditText etSchoolName = new EditText(this);
        etSchoolName.setHint("학교명을 입력해주세요.      ");
        etSchoolName.setHintTextColor(Color.rgb(255,158,27));
        etSchoolName.setTextColor(Color.rgb(255,158,27));
        etSchoolName.setTextSize(16);
        etSchoolName.setMaxLines(12);

        LinearLayout l1 = new LinearLayout(getApplicationContext());
        l1.setOrientation(LinearLayout.VERTICAL);

        LinearLayout l2 = new LinearLayout(getApplicationContext());
        l2.setOrientation(LinearLayout.HORIZONTAL);
        l2.addView(spCity);
        l2.addView(spState);

        LinearLayout l3 = new LinearLayout(getApplicationContext());
        l3.setOrientation(LinearLayout.HORIZONTAL);
        l3.addView(etSchoolName);
        l3.addView(btSearchShool);

        l1.addView(l2);
        l1.addView(l3);

        pDialog = new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE);
        pDialog.setCustomView(l1);
        pDialog.setTitleText("학교를 검색합니다.");
        pDialog.setCancelText("취소");
        pDialog.setConfirmText("확인");
        pDialog.showCancelButton(true);
        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                etSchoolNameUp.setText(myschool);
                sDialog.dismiss();
            }
        });
        pDialog.show();

        schoolsearch.setOnClickListener( // 검색하기 버튼 누르는 부분
                new Button.OnClickListener() {
                    public void onClick(View v) {

                        String strCity = spCity.getSelectedItem().toString();
                        if(strCity.equals("시/도"))
                        {
                            strCity = null;
                        }

                        String strState = spState.getSelectedItem().toString();
                        if(strState.equals("시/군/구"))
                        {
                            strState = null;
                        }

                        selectedschool = schoolname.getText().toString();
                        Log.d("TAG", selectedschool);

                        ApiRequester.getInstance().searchSchools(strCity, strState, selectedschool, new ApiRequester.UserCallback<List<School>>() {
                            @Override
                            public void onSuccess(List<School> result)
                            {
                                if(result==null)
                                {
                                    TastyToast.makeText(getApplicationContext(), "해당학교가 존재하지 않습니다.", TastyToast.LENGTH_SHORT, TastyToast.CONFUSING);
                                }
                                else
                                {
                                    int size = result.size();
                                    name = new String[size];
                                    int i = -1;

                                    Log.d("TAG", "성공");

                                    for(School school : result){
                                        i++;
                                        System.out.println(school.getName());
                                        name[i] = school.getName();
                                    }

                                    AlertDialog.Builder builder3 =
                                            new AlertDialog.Builder(SignUpActivity.this);
                                    builder3.setTitle("학교를 선택해 주세요.")
                                            .setPositiveButton("선택완료", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    schoolname.setText(name[temp]);
                                                    myschool = name[temp];
                                                }
                                            })
                                            .setSingleChoiceItems
                                                    ( name,// 리스트배열 목록
                                                            -1, // 기본 설정값
                                                            new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog,
                                                                                    int which) {
                                                                    temp = which;
                                                                }
                                                            }).setNegativeButton("취소", null);    // 리스너

                                    AlertDialog dialog = builder3.create();
                                    dialog.show();
                                }
                            }
                            @Override
                            public void onFail()
                            {
                                Log.d("TAG", "실패");

                            }
                        });
                    }
                });
        ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(SignUpActivity.this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.strArrayCity));
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCity.setAdapter(cityAdapter);
        spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK);
                if(spCity.getSelectedItem().toString().equals("시/도")){
                    spState.setClickable(false);
                    spState.setSelection(0);
                }
                else if(spCity.getSelectedItem().toString().equals(strArrayCity[1])){
                    setStateApdapter(R.array.strArraySeoulState);
                } else if (spCity.getSelectedItem().toString().equals(strArrayCity[2])) {
                    setStateApdapter(R.array.strArrayBusanState);
                } else if (spCity.getSelectedItem().toString().equals(strArrayCity[3])) {
                    setStateApdapter(R.array.strArrayDagueState);
                } else if (spCity.getSelectedItem().toString().equals(strArrayCity[4])) {
                    setStateApdapter(R.array.strArrayIncheonState);
                } else if (spCity.getSelectedItem().toString().equals(strArrayCity[5])) {
                    setStateApdapter(R.array.strArrayGwangJuState);
                } else if (spCity.getSelectedItem().toString().equals(strArrayCity[6])) {
                    setStateApdapter(R.array.strArrayDaJeonState);
                } else if (spCity.getSelectedItem().toString().equals(strArrayCity[7])) {
                    setStateApdapter(R.array.strArrayUlsanState);
                } else if (spCity.getSelectedItem().toString().equals(strArrayCity[8])) {
                    setStateApdapter(R.array.strArraySeJongState);
                } else if (spCity.getSelectedItem().toString().equals(strArrayCity[9])) {
                    setStateApdapter(R.array.strArrayGyeonGgiDo);
                } else if (spCity.getSelectedItem().toString().equals(strArrayCity[10])) {
                    setStateApdapter(R.array.strArrayGangWonDoState);
                } else if (spCity.getSelectedItem().toString().equals(strArrayCity[11])) {
                    setStateApdapter(R.array.strArrayChungCheongBukDoState);
                } else if (spCity.getSelectedItem().toString().equals(strArrayCity[12])) {
                    setStateApdapter(R.array.strArrayChungCheongNamDoState);
                } else if (spCity.getSelectedItem().toString().equals(strArrayCity[13])) {
                    setStateApdapter(R.array.strArrayJeolLaBukDoState);
                } else if (spCity.getSelectedItem().toString().equals(strArrayCity[14])) {
                    setStateApdapter(R.array.strArrayJeolLaNamDoState);
                } else if (spCity.getSelectedItem().toString().equals(strArrayCity[15])) {
                    setStateApdapter(R.array.strArrayGyeongSangBukDoState);
                } else if (spCity.getSelectedItem().toString().equals(strArrayCity[16])) {
                    setStateApdapter(R.array.strArrayGyeongSangNamDoState);
                } else if (spCity.getSelectedItem().toString().equals(strArrayCity[17])) {
                    setStateApdapter(R.array.strArrayJeJuState);
                }
                spState.setClickable(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // onNothingSelected logic
            }
        });

        setStateApdapter(R.array.strArraySeoulState);
        spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // onNothingSelected logic
            }
        });


    }
    @OnClick(R.id.etStudentInfoUp)
    void onClickEtStudentInfoUp(){
        View mView = getLayoutInflater().inflate(R.layout.dialog_select_student_info, null);
        final NumberPicker npGrade = ButterKnife.findById(mView, R.id.npGrade);
        final NumberPicker npClass = ButterKnife.findById(mView, R.id.npClass);
        final NumberPicker npAttendenceNum = ButterKnife.findById(mView, R.id.npAttendenceNum);

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogTheme);
        builder.setTitle("학년 / 반 / 이름을 선택해주세요.");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which){
                int strGrade = npGrade.getValue();
                int strClass = npClass.getValue();
                int strAttendenceNum = npAttendenceNum.getValue();
                myStudentId = strAttendenceNum;
                etStudentInfoUp.setText(Integer.toString(strGrade) + "학년 " + Integer.toString(strClass) + "반 " + Integer.toString(strAttendenceNum) + "번 "
                );
                myinfo = Integer.toString(strGrade) + "학년 " + Integer.toString(strClass) + "반 " + Integer.toString(strAttendenceNum) + "번 ";
                mygrade = Integer.toString(strGrade);
                myclass = Integer.toString(strClass);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // negative button logic
            }
        });
        builder.setView(mView);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @OnClick(R.id.btSignUp)
    void onClickBtSignUp()
    {
        myname = etStudentNameUp.getText().toString(); // 기재한 이름을 변수에 담음
        myteacher = etTeacherNameUp.getText().toString(); // 기재한 선생님ID를 변수에 담음

        if(TextUtils.isEmpty(myname) || TextUtils.isEmpty(myschool) || TextUtils.isEmpty(mygrade))
        {
            TastyToast.makeText(getApplicationContext(), "정보를 전부 입력해주세요.", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
        }
        else {
            Student.getInstance().setName(myname);
            Student.getInstance().setSchool(myschool);
            Student.getInstance().setGrade(mygrade);
            Student.getInstance().setClass_(myclass);
            Student.getInstance().setStudentId(myStudentId);

            ApiRequester.getInstance().checkDuplicateStudent(Student.getInstance(), new ApiRequester.UserCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean result) {
                    if(result){
                        TastyToast.makeText(getApplicationContext(), "해당 정보는 이미 존재합니다.", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    }
                    else{
                        if(TextUtils.isEmpty(myteacher)){
                            signUp();
                        }
                        else{
                            ApiRequester.getInstance().checkDuplicateTeacher(myteacher, new ApiRequester.UserCallback<Boolean>() {
                                @Override
                                public void onSuccess(Boolean result) {
                                    if(result){
                                        signUp();
                                    }
                                    else{
                                        TastyToast.makeText(getApplicationContext(), "등록된 선생님이 없습니다.", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                                    }
                                }
                                @Override
                                public void onFail() {

                                }
                            });
                        }
                    }
                }
                @Override
                public void onFail() {
                }
            });
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        setting = getSharedPreferences("setting", 0);
        editor= setting.edit();

    }

    private void setStateApdapter(int state){
        ArrayAdapter<String> StateAdapter = new ArrayAdapter<String>(SignUpActivity.this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(state));
        StateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spState.setAdapter(StateAdapter);
    }

    private void signUp(){
        ApiRequester.getInstance().signUpStudent(Student.getInstance().getStudent(), new ApiRequester.UserCallback<Student>() {
            @Override
            public void onSuccess(Student result) {
                Student.getInstance().setStudent(result);
                ApiRequester.getInstance().applyMatching(myteacher, Student.getInstance().getId(), new ApiRequester.UserCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean result) {

                        editor.putString("myname", myname);
                        editor.putString("myschool", myschool);
                        editor.putString("mygrade", mygrade);
                        editor.putString("myclass", myclass);
                        editor.putString("myStudentId", String.valueOf(myStudentId));
                        editor.putBoolean("Auto_Login_enabled", true);
                        editor.commit();

                        TastyToast.makeText(getApplicationContext(), "회원가입이 완료되었습니다..", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                        Intent e = new Intent(SignUpActivity.this, SelectExamOrPractice.class);
                        startActivity(e);
                    }
                    @Override
                    public void onFail() {
                    }
                });
            }
            @Override
            public void onFail() {
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.context_menu).setVisible(false);
        return true;
    }
}