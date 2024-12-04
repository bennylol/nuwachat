package com.example.assistant;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.nuwarobotics.service.IClientId;
import com.nuwarobotics.service.agent.NuwaRobotAPI;
import com.nuwarobotics.service.agent.RobotEventListener;
import com.nuwarobotics.service.agent.VoiceEventListener;
import com.nuwarobotics.service.agent.VoiceResultJsonParser;
import com.nuwarobotics.service.facecontrol.UnityFaceCallback;
import com.nuwarobotics.service.facecontrol.utils.ServiceConnectListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class MainActivity2 extends AppCompatActivity {


    private NuwaRobotAPI mRobotAPI;
    private IClientId mClientId;
    Context mContext;
    boolean mSDKinit = false;
    public static String ThreadID = "";
    public static String RunID = null;
    public static String Status = "1";
    public static String Topic_nam = "";
    public static String User_name = "";

    public static final String API_key = "自己的apikey";
    public static String assistant_id = null;
    private Button StartButton;
    private DatabaseReference mDatabase;
    private Button MathButton;
    private Button ChatButton;
    private Button ma_back;
    private Button ma_main;

    //private TextView tvAnswer;
    //private TextView tvQuestion;
    //private TextView threadID;
    //private Button SendButton;
    private static final long FACE_MOUTH_SPEED = 200;

    private FirebaseAuth mAuth;

    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        GlobalVariable globalVariable = ((GlobalVariable) getApplicationContext());
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // 在這裡可以使用 userId 寫入數據
        } else {
            // 用戶未登錄
        }


        mContext = this;
        //tvAnswer = findViewById(R.id.textView_Answer);
        //tvQuestion = findViewById(R.id.edittext_Input);
        //threadID = findViewById(R.id.thread_id);
        //SendButton = findViewById(R.id.button_Send);
        StartButton = findViewById(R.id.start_button);
        /*MathButton = findViewById(R.id.math_button);
        ChatButton = findViewById(R.id.chat_button);*/
        ma_back = findViewById(R.id.ma2_back);
        ma_main = findViewById(R.id.ma2_main);

        mClientId = new IClientId(this.getPackageName());
        mRobotAPI = new NuwaRobotAPI(this, mClientId);

        Log.d(TAG, "register EventListener ");
        mRobotAPI.registerRobotEventListener(robotEventListener);

        mRobotAPI.registerVoiceEventListener(voiceEventListener);

        //mRobotAPI.UnityFaceManager().showUnity();

        //GlobalVariable globalVariable = ((GlobalVariable) getApplicationContext());
        DecideAst(globalVariable.getAst_num());
        Topic_nam = globalVariable.getTopic_nam();
        ThreadID = "";
        //CreateThreadPost();
        ma_back.setOnClickListener(view -> {
            finish();
        });

        ma_main.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity2.this, WelcomeActivity.class);
            startActivity(intent);
        });

        StartButton.setOnClickListener(view -> {
            CreateThreadPost();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            showface();
            String question = "我是"+globalVariable.getUser_name()+"，我是國小學生，我今天想聊" + Topic_nam;
            //String question = "我是陳北，我是國小學生，我今天想聊" + Topic_nam;
            CreateMessagePost(question);
            //writeNewUser(question, "");
        });




    }



    private void writeNewMessage(String messageText) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Log.w(TAG, "用戶未登錄");
            return;
        }

        String userId = currentUser.getUid();
        String messageId = mDatabase.child("users").child(userId).child("chats").child("chatId1").child("messages").push().getKey();
        long timestamp = new Date().getTime();
        String dateTime = Utils.timestampToDateTime(timestamp); // 轉換時間

        // 創建消息對象（用戶資訊）
        Userdatabase userMessage = new Userdatabase(messageId, messageText, dateTime);

        // 將用戶消息儲存到資料庫中
        mDatabase.child("users").child(userId).child("chats").child("chatId1").child("messages").child(messageId).setValue(userMessage)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "用戶訊息成功");
                    // 用戶訊息成功後倒入機器人
                    saveRobotMessage(userId, messageId, messageText, dateTime);
                })
                .addOnFailureListener(e -> Log.w(TAG, "用戶消息讀取失敗", e));
    }

    private void saveRobotMessage(String userId, String messageId, String messageText, String dateTime) {
        // 創建消息對象（機器人資訊）
        Userdatabase robotMessage = new Userdatabase(messageId, messageText, dateTime);

        // 将机器人消息存储到数据库中
        mDatabase.child("users").child(userId).child("chats").child("chatId1").child("messages").child(messageId).setValue(robotMessage)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "機器人訊息導入成功"))
                .addOnFailureListener(e -> Log.w(TAG, "機器人訊息導入失败", e));
    }


    // private void writeNewUser(String question, String answer) {
      //  String userId = mDatabase.push().getKey();
       // Userdatabase user = new Userdatabase(question, answer);
      //  mDatabase.child("users").child(userId).setValue(user);
   // }

    public void DecideTopic (String topic) {
        CreateThreadPost();
        CreateMessagePost(topic);
    }


    public void DecideAst (String ast) {
        if(ast.equals("1")) {
            assistant_id = "asst_u884xfExBGRhbEBJq84YHsRW";
        }
        else if (ast.equals("2")) {
            assistant_id = "asst_jiViBszldfZxE94VX7mfT2cJ";
        }
        else if (ast.equals("3")) {
            assistant_id = "asst_8WRPjpejIyHa7KIm3yEhX2If";
        }
        else if (ast.equals("4")) {
            assistant_id = "asst_p2JBXGZ5MuApzkNAqoAoOGBH";
        }
        else if (ast.equals("5")) {
            assistant_id = "asst_YSGtcjgSWlpzjmq7e5mpf3gN";
        }
    }


    public void CreateThreadPost() {
        OkHttpClient client = new OkHttpClient()
                .newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
                .build();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "");

        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/threads")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + API_key)
                .addHeader("OpenAI-Beta", "assistants=v1")
                //.addHeader("Cookie", "__cf_bm=zwryXv3SaxA563zFNKOfMWIowlYm5f2VtNlGhOaIK0I-1709440240-1.0.1.1-gAAFqGIN2a4Y74_jvBPDo4iv9sF4xIJYFhvm4EssywY_enV8pOZZ7jNRj2LgfEk51zJf0VFjX.XKGwSXsH0THg; _cfuvid=APbDb7CBucxAPT4mb4dxeKF450p6HEwvfsMZWVhsm68-1709440240738-0.0.1.1-604800000")
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("TAG", "onFailCall: "+ e.getMessage());
                //threadID.setText(e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String res = response.body().string();
                Log.d("TAG", "onOKCall: "+res);
                CreateThread createThread = new Gson().fromJson(res, CreateThread.class);
                ThreadID = createThread.getId();
                //runOnUiThread(() ->{
                    //threadID.setText(createThread.getId());
                    //ThreadID = createThread.getId();
                //});
            }
        });
    }

    public void CreateMessagePost(String question) {
        OkHttpClient client = new OkHttpClient()
                .newBuilder()
                //.connectTimeout(10, TimeUnit.SECONDS)
                //.readTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
                .build();

        MediaType mediaType = MediaType.parse("application/json");
        String content = new Gson().toJson(makeMessage(question));
        RequestBody body = RequestBody.create(mediaType, content);

        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/threads/"+ThreadID+"/messages")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + API_key)
                .addHeader("OpenAI-Beta", "assistants=v1")
                //.addHeader("Cookie", "__cf_bm=LdaY74IAP8NQSabMUWkz7tDsYGqKtgnQ3ACl7dPoSlA-1709448819-1.0.1.1-zKSyep7D89oGyT1GF.SjaOmmM7P1okbMyJO81h6ebYIf64PttBE2u_6A.F0EMA7mDpCVoz_fuXp5ogEbpj32yQ; _cfuvid=APbDb7CBucxAPT4mb4dxeKF450p6HEwvfsMZWVhsm68-1709440240738-0.0.1.1-604800000")
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("TAG", "onFailCall: "+ e.getMessage());
                //tvAnswer.setText(e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String res = response.body().string();
                Log.d("TAG", "onOKCall: "+res);
                CreateMessage createMessage = new Gson().fromJson(res, CreateMessage.class);
                runOnUiThread(() ->{
                    //tvAnswer.setText(createMessage.getId());
                    RunAssistantPost();
                });
            }
        });
    }

    private WeakHashMap<String,Object> makeMessage(String input){
        WeakHashMap<String,Object> weakHashMap = new WeakHashMap<>();
        weakHashMap.put("role","user");
        weakHashMap.put("content",input);
        return weakHashMap;
    }

    public void RunAssistantPost() {
        OkHttpClient client = new OkHttpClient()
                .newBuilder()
                //.connectTimeout(10, TimeUnit.SECONDS)
                //.readTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
                .build();

        MediaType mediaType = MediaType.parse("application/json");
        String content = new Gson().toJson(runAssistant());
        RequestBody body = RequestBody.create(mediaType, content);

        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/threads/"+ThreadID+"/runs")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + API_key)
                .addHeader("OpenAI-Beta", "assistants=v1")
                //.addHeader("Cookie", "__cf_bm=LdaY74IAP8NQSabMUWkz7tDsYGqKtgnQ3ACl7dPoSlA-1709448819-1.0.1.1-zKSyep7D89oGyT1GF.SjaOmmM7P1okbMyJO81h6ebYIf64PttBE2u_6A.F0EMA7mDpCVoz_fuXp5ogEbpj32yQ; _cfuvid=APbDb7CBucxAPT4mb4dxeKF450p6HEwvfsMZWVhsm68-1709440240738-0.0.1.1-604800000")
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("TAG", "onFailCall: "+ e.getMessage());
                //tvAnswer.setText(e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String res = response.body().string();
                Log.d("TAG", "onOKCall: "+res);
                RunAssistant runAssistant = new Gson().fromJson(res, RunAssistant.class);
                runOnUiThread(() ->{
                    //tvAnswer.setText(runAssistant.getModel());
                    //MessageGet();
                    RunID = runAssistant.getId();
                    RunStatusGet();
                    while (!Status.equals("completed")) {
                        Log.d("Tag", "Status: "+ Status);
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        RunStatusGet();
                    }
                    Log.d("Tag", "Status: "+ Status);
                    MessageGet();
                });
            }
        });
    }

    private WeakHashMap<String,Object> runAssistant(){
        WeakHashMap<String,Object> weakHashMap = new WeakHashMap<>();
        weakHashMap.put("assistant_id",assistant_id);
        return weakHashMap;
    }

    public void MessageGet() {

        OkHttpClient client = new OkHttpClient()
                .newBuilder()
                //.connectTimeout(10, TimeUnit.SECONDS)
                //.readTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
                .build();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "");

        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/threads/"+ThreadID+"/messages")
                .method("GET", null)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + API_key)
                .addHeader("OpenAI-Beta", "assistants=v1")
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("TAG", "onFailCall: "+ e.getMessage());
                //tvAnswer.setText(e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String res = response.body().string();
                Log.d("TAG", "onOKCall: "+res);
                AssistantRespond assistantRespond = new Gson().fromJson(res, AssistantRespond.class);
                runOnUiThread(() ->{
                    String answer = assistantRespond.getData().get(0).getContent().get(0).getText().getValue();
                    mouthOn(answer,FACE_MOUTH_SPEED);
                    writeNewMessage(answer);
                });

            }
        });
    }

    public void RunStatusGet() {
        OkHttpClient client = new OkHttpClient()
                .newBuilder()
                //.connectTimeout(10, TimeUnit.SECONDS)
                //.readTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
                .build();


        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "");

        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/threads/"+ThreadID+"/runs/"+RunID)
                .method("GET", null)
                .addHeader("Authorization", "Bearer " + API_key)
                .addHeader("OpenAI-Beta", "assistants=v1")
                //.addHeader("Cookie", "__cf_bm=AmfVE8SNnSgHfWcso25cX4Wz2zakQlF4XyxplY4dohk-1709468117-1.0.1.1-r6qS2Il3pPM.M7NZwyzSYUQKqRpgs1Kldh7E.AGIdvQI_cOU0fz2QD8IuE_1yb03xQ8guvijq4WKfXNUpm8q0A; _cfuvid=APbDb7CBucxAPT4mb4dxeKF450p6HEwvfsMZWVhsm68-1709440240738-0.0.1.1-604800000")
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("TAG", "onFailCall: "+ e.getMessage());
                //tvAnswer.setText(e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String res = response.body().string();
                Log.d("TAG", "onOKCall: "+res);
                RunStatus runStatus = new Gson().fromJson(res, RunStatus.class);
                Status = runStatus.getStatus();
                //runOnUiThread(() ->{
                    //tvAnswer.setText(assistantRespond.getData().get(0).getContent().get(0).getText().getValue());
                    //MessageGet();
                    //tvAnswer.setText(runStatus.getStatus());
                //});
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release resources related to TTS and ASR
        // For example:
        mRobotAPI.release();
    }

    /*private void setText(final String text, final boolean append) {
        runOnUiThread(() -> {
            TextView textViewAnswer = findViewById(R.id.textView_Answer);
            if (append) {
                textViewAnswer.append(text);
            } else {
                textViewAnswer.setText(text);
            }
        });
    }*/

    public static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HH:mm:ss ");
        String currentDateAndTime = sdf.format(new Date());
        return currentDateAndTime;
    }

    ServiceConnectListener FaceControlConnect = new ServiceConnectListener() {
        @Override
        public void onConnectChanged(ComponentName componentName, boolean b) {
            //isBindFace = b;
            Log.d(TAG, "faceService onbind : " + b);
            //Step 4 : register face callback
            mRobotAPI.UnityFaceManager().registerCallback(mUnityFaceCallback);
        }
    };

    RobotEventListener robotEventListener = new RobotEventListener() {
        @Override
        public void onWikiServiceStart() {
            Log.d(TAG, "onWikiServiceStart, robot ready to be control");

            //Step 3 : Start Control Robot after Service ready.
            //Register Voice Callback event
            mRobotAPI.registerVoiceEventListener(voiceEventListener);//listen callback of robot voice related event
            mRobotAPI.initFaceControl(mContext, mContext.getClass().getName(), FaceControlConnect);
            mRobotAPI.requestSensor(NuwaRobotAPI.SENSOR_TOUCH | NuwaRobotAPI.SENSOR_PIR | NuwaRobotAPI.SENSOR_DROP );

            //Allow user start demo after service ready
            //TODO
            //setText(getCurrentTime() + "onWikiServiceStart, robot ready to be control", false);
            mSDKinit = true;

            /*runOnUiThread(() -> {
                Button sendButton = findViewById(R.id.button_Send);
                sendButton.setEnabled(true);*/
            //});
        }

        @Override
        public void onWikiServiceStop() {

        }

        @Override
        public void onWikiServiceCrash() {

        }

        @Override
        public void onWikiServiceRecovery() {

        }

        @Override
        public void onStartOfMotionPlay(String s) {

        }

        @Override
        public void onPauseOfMotionPlay(String s) {

        }

        @Override
        public void onStopOfMotionPlay(String s) {

        }

        @Override
        public void onCompleteOfMotionPlay(String s) {

        }

        @Override
        public void onPlayBackOfMotionPlay(String s) {

        }

        @Override
        public void onErrorOfMotionPlay(int i) {

        }

        @Override
        public void onPrepareMotion(boolean b, String s, float v) {

        }

        @Override
        public void onCameraOfMotionPlay(String s) {

        }

        @Override
        public void onGetCameraPose(float v, float v1, float v2, float v3, float v4, float v5, float v6, float v7, float v8, float v9, float v10, float v11) {

        }

        @Override
        public void onTouchEvent(int type, int touch) {
            Log.d(TAG,"onTouchEvent type="+type+" touch="+touch);
            switch(type) {
                case 1 :
                    break;
                case 2:
                    mRobotAPI.startSpeech2Text(false);
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    break;
                default:
                    break;
            }


        }

        @Override
        public void onPIREvent(int i) {

        }

        @Override
        public void onTap(int i) {

        }

        @Override
        public void onLongPress(int i) {

        }

        @Override
        public void onWindowSurfaceReady() {

        }

        @Override
        public void onWindowSurfaceDestroy() {

        }

        @Override
        public void onTouchEyes(int i, int i1) {

        }

        @Override
        public void onRawTouch(int i, int i1, int i2) {

        }

        @Override
        public void onFaceSpeaker(float v) {

        }

        @Override
        public void onActionEvent(int i, int i1) {

        }

        @Override
        public void onDropSensorEvent(int i) {

        }

        @Override
        public void onMotorErrorEvent(int i, int i1) {

        }
    };

    private void hideface() {
        if (mRobotAPI != null) {
            mRobotAPI.UnityFaceManager().hideUnity();//hide face
            ThreadID = "";
            mRobotAPI.stopSensor(NuwaRobotAPI.SENSOR_TOUCH | NuwaRobotAPI.SENSOR_PIR | NuwaRobotAPI.SENSOR_DROP);
            onDestroy();
            finish();
        } else {
            Log.d(TAG, " === mNuwaRobotAPI null ===  please init");
        }
    }
    private void showface() {
        if (mRobotAPI != null) {
            mRobotAPI.UnityFaceManager().showUnity();//lunch face
            //mRobotAPI.startSpeech2Text(false);

        } else {
            Log.d(TAG, " === mNuwaRobotAPI null ===  please init");
        }
    }

    private void mouthOn(String tts, long speed) {
        GlobalVariable globalVariable = ((GlobalVariable)getApplicationContext());
        String Motion;
        int Mouth;
        String Emoji;
        if (mRobotAPI != null) {
            mRobotAPI.stopTTS();
            mRobotAPI.startTTS(tts);
            //mRobotAPI.UnityFaceManager().mouthOn(speed);
            Mouth = globalVariable.SelectMouth(globalVariable.MouthRanNum());
            Emoji = globalVariable.SelectEmoji(globalVariable.FaceRanNum());
            mRobotAPI.UnityFaceManager().mouthEmotionOn(speed, Mouth);
            mRobotAPI.UnityFaceManager().playFaceAnimation(Emoji);
            Motion = globalVariable.SelectMotion(globalVariable.RanNum());
            Log.d(TAG,"Motion: " + Motion);
            mRobotAPI.motionPlay(Motion,false);
        } else {
            Log.d(TAG, " === mNuwaRobotAPI null ===  please init");
        }
    }

    private void mouthOff() {
        if (mRobotAPI != null) {
            mRobotAPI.UnityFaceManager().playFaceAnimationDefault();
            mRobotAPI.UnityFaceManager().mouthOff();
            //mRobotAPI.mouthEmotionOn(FACE_MOUTH_SPEED,0);
            mRobotAPI.stopTTS();
        } else {
            Log.d(TAG, " === mNuwaRobotAPI null ===  please init");
        }
    }


    VoiceEventListener voiceEventListener = new VoiceEventListener() {
        @Override
        public void onWakeup(boolean isError, String score, float direction) {

        }

        @Override
        public void onTTSComplete(boolean isError) {
            Log.d(TAG, "onTTSComplete:" + !isError);
            //setText("onTTSComplete, " + !isError, false);


            runOnUiThread(() -> {
                mouthOff();
                Status = "waiting";
                mRobotAPI.startSpeech2Text(false);
            });
        }

        @Override
        public void onSpeechRecognizeComplete(boolean isError, ResultType iFlyResult, String json) {

        }

        @Override
        public void onSpeech2TextComplete(boolean isError, String json) {
            String question = VoiceResultJsonParser.parseVoiceResult(json);
            if (question.equals("結束對話")) {
                hideface();
            }
            else if (!question.isEmpty()) {
                CreateMessagePost(question);
                writeNewMessage(question);
            }
        }
        @Override
        public void onMixUnderstandComplete(boolean isError, ResultType resultType, String s) {

        }

        @Override
        public void onSpeechState(ListenType listenType, SpeechState speechState) {

        }

        @Override
        public void onSpeakState(SpeakType speakType, SpeakState speakState) {

        }

        @Override
        public void onGrammarState(boolean isError, String s) {

        }

        @Override
        public void onListenVolumeChanged(ListenType listenType, int i) {

        }

        @Override
        public void onHotwordChange(HotwordState hotwordState, HotwordType hotwordType, String s) {

        }
    };

    UnityFaceCallback mUnityFaceCallback = new UnityFaceCallback(){
        @Override
        public void on_touch_left_eye() {
            Log.d("FaceControl", "on_touch_left_eye()");
        }

        @Override
        public void on_touch_right_eye() {
            Log.d("FaceControl", "on_touch_right_eye()");
        }

        @Override
        public void on_touch_nose() {
            Log.d("FaceControl", "on_touch_nose()");
        }

        @Override
        public void on_touch_mouth() {
            Log.d("FaceControl", "on_touch_mouth()");
        }

        @Override
        public void on_touch_head() {
            Log.d("FaceControl", "on_touch_head()");
        }

        @Override
        public void on_touch_left_edge() {
            Log.d("FaceControl", "on_touch_left_edge()");
        }

        @Override
        public void on_touch_right_edge() {
            Log.d("FaceControl", "on_touch_right_edge()");
        }

        @Override
        public void on_touch_bottom() {
            Log.d("FaceControl", "on_touch_bottom()");
        }

    };
}
