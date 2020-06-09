package com.tencent.qcloud.uikit.business.chat.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.library.common.utils.ContextManager;
import com.tencent.imsdk.TIMManager;
import com.tencent.qcloud.uikit.R;
import com.tencent.qcloud.uikit.TUIKit;
import com.tencent.qcloud.uikit.business.chat.model.MessageInfo;
import com.tencent.qcloud.uikit.business.chat.model.MessageInfoUtil;
import com.tencent.qcloud.uikit.business.chat.view.widget.ChatActionsFragment;
import com.tencent.qcloud.uikit.business.chat.view.widget.MessageOperaUnit;
import com.tencent.qcloud.uikit.common.IUIKitCallBack;
import com.tencent.qcloud.uikit.common.UIKitConstants;
import com.tencent.qcloud.uikit.common.component.audio.UIKitAudioArmMachine;
import com.tencent.qcloud.uikit.common.component.face.Emoji;
import com.tencent.qcloud.uikit.common.component.face.FaceFragment;
import com.tencent.qcloud.uikit.common.component.face.FaceManager;
import com.tencent.qcloud.uikit.common.component.picture.Matisse;
import com.tencent.qcloud.uikit.common.component.video.CameraActivity;
import com.tencent.qcloud.uikit.common.component.video.JCameraView;
import com.tencent.qcloud.uikit.common.utils.SoundPlayUtils;
import com.tencent.qcloud.uikit.common.utils.UIUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * 聊天界面，底部发送图片、拍照、摄像、文件面板
 */

public class ChatBottomInputGroup extends LinearLayout implements View.OnClickListener, UIKitAudioArmMachine.AudioRecordCallback, TextWatcher {

    /**
     * 语音/文字切换输入按钮
     */
    public ImageView switchBtn;
    /**
     * 表情按钮
     */
    public ImageView faceBtn;
    /**
     * 更多按钮
     */
    public ImageView moreBtn;

    /**
     * 消息发送按钮
     */
    public ImageView sendIv;

    /**
     * 语音长按按钮
     */
    public Button voiceBtn;
    /**
     * 文本输入框
     */
    public EditText msgEditor;
    public ConstraintLayout clInput;

    private ChatActionsFragment actionsFragment;

    private FaceFragment faceFragment;

    private View moreGroup;
    private ChatInputHandler inputHandler;
    private MessageHandler msgHandler;
    private AppCompatActivity activity;
    private FragmentManager fragmentManager;
    private List<MessageOperaUnit> actions = new ArrayList<>();

    private static final int STATE_NONE_INPUT = -1;
    private static final int STATE_SOFT_INPUT = 0;
    private static final int STATE_VOICE_INPUT = 1;
    private static final int STATE_FACE_INPUT = 2;
    private static final int STATE_ACTION_INPUT = 3;
    private boolean sendAble, audioCancel;
    private int currentState;
    private int lastMsgLineCount;
    private float startRecordY;

    private AlertDialog mPermissionDialog;
    private String mPackName = "com.fjx.mg";
    private boolean isEmoji;
    private PersonalInfo mPersonalInfo;

    public ChatBottomInputGroup(Context context) {
        super(context);
        init();
    }

    public ChatBottomInputGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ChatBottomInputGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init() {
        activity = (AppCompatActivity) getContext();
        inflate(getContext(), R.layout.chat_bottom_group, this);
        clInput = findViewById(R.id.clInput);
        moreGroup = findViewById(R.id.more_groups);
        voiceBtn = findViewById(R.id.chat_voice_input);
        switchBtn = findViewById(R.id.voice_input_switch);
        switchBtn.setOnClickListener(this);
        faceBtn = findViewById(R.id.face_btn);
        faceBtn.setOnClickListener(this);
        moreBtn = findViewById(R.id.more_btn);
        moreBtn.setOnClickListener(this);
        sendIv = findViewById(R.id.send_iv);
        sendIv.setOnClickListener(this);
        msgEditor = findViewById(R.id.chat_message_input);
        msgEditor.addTextChangedListener(this);
        msgEditor.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                showSoftInput();
                return false;
            }
        });

        msgEditor.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                return false;
            }
        });

        msgEditor.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                return false;
            }
        });

        voiceBtn.setOnTouchListener(new OnTouchListener() {
            private long start;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (!EasyPermissions.hasPermissions(activity, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    EasyPermissions.requestPermissions(activity, "需要您的内存卡和录音权限，是否允许？", 1,
                            Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE);
                    return false;
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    voiceBtn.setText("松开结束");
                    audioCancel = true;
                    startRecordY = motionEvent.getY();
                    if (inputHandler != null)
                        inputHandler.startRecording();
                    start = System.currentTimeMillis();
                    UIKitAudioArmMachine.getInstance().startRecord(ChatBottomInputGroup.this);

                } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    if (motionEvent.getY() - startRecordY < -100) {
                        audioCancel = true;
                        if (inputHandler != null)
                            inputHandler.cancelRecording();
                    } else {
                        audioCancel = false;
//                        if (inputHandler != null)
//                            inputHandler.startRecording();
                    }

                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    voiceBtn.setText("按住说话");
                    if (motionEvent.getY() - startRecordY < -100) {
                        audioCancel = true;
                    } else {
                        audioCancel = false;
                    }
                    UIKitAudioArmMachine.getInstance().stopRecord();
                }
                return false;
            }
        });

        initDefaultActions();

    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        msgEditor.removeTextChangedListener(this);
    }

    public void iniDefaultActions(boolean isGroup) {
        actions.clear();
        MessageOperaUnit action = new MessageOperaUnit();
        action.setIconResId(R.drawable.tim_album);
        action.setTitleId(R.string.pic);
        action.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startSendPhoto();
            }
        });
        actions.add(action);

        action = new MessageOperaUnit();
        action.setIconResId(R.drawable.tim_camara);
        action.setTitleId(R.string.take_photo);
        action.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startTakeShot();
            }
        });
        actions.add(action);

        action = new MessageOperaUnit();
        action.setIconResId(R.drawable.tim_redpack);
        action.setTitleId(R.string.red_pack);
        action.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = TIMManager.getInstance().getLoginUser() + ContextManager.getContext().getString(R.string.red_pack);
                final MessageInfo redpacket = MessageInfoUtil.buildTransferCustomMessage(MessageInfoUtil.TRANSFER_RED_PACKET_UN_RECEIVED, message);
                if (msgHandler != null)
                    msgHandler.sendMessage(redpacket);
            }
        });

//            actions.add(action);
    }

    private void initDefaultActions() {
        MessageOperaUnit action = new MessageOperaUnit();
        action.setIconResId(R.drawable.tim_album);
        action.setTitleId(R.string.pic);
        action.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startSendPhoto();
            }
        });
        actions.add(action);

        action = new MessageOperaUnit();
        action.setIconResId(R.drawable.tim_camara);
        action.setTitleId(R.string.take_photo);
        action.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startTakeShot();
            }
        });
        actions.add(action);


//        action = new MessageOperaUnit();
//        action.setIconResId(R.drawable.tim_file);
//        action.setTitleId(R.string.file);
//        action.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startSendFile();
//            }
//        });
//        actions.add(action);

        action = new MessageOperaUnit();
        action.setIconResId(R.drawable.tim_accounts);
        action.setTitleId(R.string.carry_over);
        action.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                String message = TIMManager.getInstance().getLoginUser() + "转账";
                final MessageInfo transfer = MessageInfoUtil.buildTransferCustomMessage(MessageInfoUtil.TRANSFER_ACCOUNT_UN_RECEIVED, message);
                if (msgHandler != null)
                    msgHandler.sendMessage(transfer);
            }
        });

        actions.add(action);

        action = new MessageOperaUnit();
        action.setIconResId(R.drawable.tim_redpack);
        action.setTitleId(R.string.red_pack);
        action.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = TIMManager.getInstance().getLoginUser() + "红包";
                final MessageInfo redpacket = MessageInfoUtil.buildTransferCustomMessage(MessageInfoUtil.TRANSFER_RED_PACKET_UN_RECEIVED, message);
                if (msgHandler != null)
                    msgHandler.sendMessage(redpacket);
            }
        });
//            actions.add(action);
        action = new MessageOperaUnit();
        action.setIconResId(R.drawable.tim_personal_info);
        action.setTitleId(R.string.tim_psersonal_info);
        action.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPersonalInfo != null) {
                    mPersonalInfo.sendPersonalInfo();
                }
            }
        });
        actions.add(action);
    }

    private void startSendPhoto() {
        if (!EasyPermissions.hasPermissions(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            EasyPermissions.requestPermissions(activity, "需要您的内存卡访问权限，是否允许？", 1,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
            return;
        }

        Matisse.defaultFrom(activity, new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                if (data instanceof List) {
                    List<Uri> uris = (List<Uri>) data;
                    for (int i = 0; i < uris.size(); i++) {
                        MessageInfo info = MessageInfoUtil.buildImageMessage(uris.get(i), true, false);
                        if (msgHandler != null)
                            msgHandler.sendMessage(info);
                    }
                }
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {

            }
        });
    }

    private void startTakeShot() {
        if (!EasyPermissions.hasPermissions(activity, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            EasyPermissions.requestPermissions(activity, "需要您的内存卡和相机访问权限，是否允许？", 1,
                    Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
            return;
        }
        Intent captureIntent = new Intent(getContext(), CameraActivity.class);
        captureIntent.putExtra(UIKitConstants.CAMERA_TYPE, JCameraView.BUTTON_STATE_ONLY_CAPTURE);
        CameraActivity.mCallBack = new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                Uri contentUri = Uri.fromFile(new File(data.toString()));
                MessageInfo msg = MessageInfoUtil.buildImageMessage(contentUri, true, true);
                if (msgHandler != null)
                    msgHandler.sendMessage(msg);
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {

            }
        };
        getContext().startActivity(captureIntent);
    }

    private void startSendRecord() {
        if (!EasyPermissions.hasPermissions(activity, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)) {

            EasyPermissions.requestPermissions(activity, "需要您的内存卡、相机和录音访问权限，是否允许？", 1,
                    Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
            return;
        }
        Intent captureIntent = new Intent(getContext(), CameraActivity.class);
        captureIntent.putExtra(UIKitConstants.CAMERA_TYPE, JCameraView.BUTTON_STATE_ONLY_RECORDER);
        CameraActivity.mCallBack = new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                Intent videoData = (Intent) data;
                String imgPath = videoData.getStringExtra(UIKitConstants.CAMERA_IMAGE_PATH);
                String videoPath = videoData.getStringExtra(UIKitConstants.CAMERA_VIDEO_PATH);
                int imgWidth = videoData.getIntExtra(UIKitConstants.IMAGE_WIDTH, 0);
                int imgHeight = videoData.getIntExtra(UIKitConstants.IMAGE_HEIGHT, 0);
                long duration = videoData.getLongExtra(UIKitConstants.VIDEO_TIME, 0);
                MessageInfo msg = MessageInfoUtil.buildVideoMessage(imgPath, videoPath, imgWidth, imgHeight, duration);
                if (msgHandler != null)
                    msgHandler.sendMessage(msg);
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {

            }
        };
        getContext().startActivity(captureIntent);
    }

    private void startSendFile() {
        if (!EasyPermissions.hasPermissions(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {

            EasyPermissions.requestPermissions(activity, "需要您的内存卡读写权限，是否允许？", 1,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
            return;
        }
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        actionsFragment.setCallback(new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                MessageInfo info = MessageInfoUtil.buildFileMessage((Uri) data);
                if (msgHandler != null)
                    msgHandler.sendMessage(info);
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                UIUtils.toastLongMessage(errMsg);
            }
        });
        actionsFragment.startActivityForResult(intent, ChatActionsFragment.REQUEST_CODE_FILE);
    }

    public void setMoreOperaUnits(List<MessageOperaUnit> actions, boolean isAdd) {
        if (isAdd) {
            this.actions.addAll(actions);
        } else {
            this.actions = actions;
        }
        actionsFragment.setActions(actions);
    }


    private boolean checkStoragePermisson(Activity activity, String permisson) {
        boolean flag = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permission = ActivityCompat.checkSelfPermission(activity, permisson);
            if (PackageManager.PERMISSION_GRANTED != permission) {
                //2.没有权限
                showPermissionDialog();
                flag = false;
            }
        }
        return flag;
    }

    private void showPermissionDialog() {
        if (mPermissionDialog == null) {
            mPermissionDialog = new AlertDialog.Builder(activity)
                    .setMessage(activity.getString(R.string.manual_settings))
                    .setPositiveButton(activity.getString(R.string.settings), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cancelPermissionDialog();
                            Uri packageURI = Uri.parse("package:" + mPackName);
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                            activity.startActivity(intent);
                        }
                    })
                    .setNegativeButton(activity.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //关闭页面或者做其他操作
                            cancelPermissionDialog();
                        }
                    })
                    .create();
        }
        mPermissionDialog.show();
    }

    //关闭对话框
    private void cancelPermissionDialog() {
        mPermissionDialog.cancel();
    }

    public void setInputHandler(ChatInputHandler handler) {
        this.inputHandler = handler;
    }


    public void setMsgHandler(MessageHandler handler) {
        this.msgHandler = handler;
    }

    public void setPersonalInfo(PersonalInfo personalInfo) {
        mPersonalInfo = personalInfo;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.voice_input_switch) {
            if (currentState == STATE_SOFT_INPUT)
                currentState = STATE_VOICE_INPUT;
            else
                currentState = STATE_SOFT_INPUT;
            if (currentState == STATE_VOICE_INPUT) {
                switchBtn.setImageResource(R.drawable.action_textinput_selector);
                voiceBtn.setVisibility(VISIBLE);
//                msgEditor.setVisibility(GONE);
                clInput.setVisibility(GONE);
                faceBtn.setVisibility(View.GONE);
                hideSoftInput();
            } else {
                switchBtn.setImageResource(R.drawable.action_audio_selector);
                voiceBtn.setVisibility(GONE);
//                msgEditor.setVisibility(VISIBLE);
                clInput.setVisibility(VISIBLE);
                faceBtn.setVisibility(View.VISIBLE);
                showSoftInput();
            }

        } else if (view.getId() == R.id.face_btn) {
            if (currentState == STATE_FACE_INPUT) {
                currentState = STATE_NONE_INPUT;
                moreGroup.setVisibility(View.GONE);
            } else {
                showFaceViewGroup();
                currentState = STATE_FACE_INPUT;
            }
        } else if (view.getId() == R.id.more_btn) {
            if (currentState == STATE_ACTION_INPUT) {
                currentState = STATE_NONE_INPUT;
                moreGroup.setVisibility(View.GONE);
            } else {
                showActionsGroup();
                currentState = STATE_ACTION_INPUT;
            }
        } else if (view.getId() == R.id.send_iv) {
            if (sendAble) {
                if (msgHandler != null)
                    msgHandler.sendMessage(MessageInfoUtil.buildTextMessage(msgEditor.getText().toString()));
                msgEditor.setText("");
            }
        }
    }

    private void showSoftInput() {
        hideActionsGroup();
     /*   if (moreGroup.getVisibility() == VISIBLE) {
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        } else {
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }*/
        currentState = STATE_SOFT_INPUT;
        switchBtn.setImageResource(R.drawable.action_audio_selector);
        faceBtn.setImageResource(R.drawable.bottom_action_face_normal);
        msgEditor.requestFocus();
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(msgEditor, 0);
        if (inputHandler != null)
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    inputHandler.popupAreaShow();
                }
            }, 200);

    }


    public void hideSoftInput() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(msgEditor.getWindowToken(), 0);
        msgEditor.clearFocus();
        if (inputHandler != null)
            inputHandler.popupAreaHide();
        moreGroup.setVisibility(View.GONE);
        currentState = STATE_NONE_INPUT;
    }

    private void showFaceViewGroup() {
        if (fragmentManager == null)
            fragmentManager = activity.getSupportFragmentManager();
        if (faceFragment == null)
            faceFragment = new FaceFragment();
        hideSoftInput();
//        postDelayed(new Runnable() {
//            @Override
//            public void run() {
        moreGroup.setVisibility(View.VISIBLE);
        msgEditor.requestFocus();
        faceFragment.setListener(new FaceFragment.OnEmojiClickListener() {
            @Override
            public void onEmojiDelete() {
                int index = msgEditor.getSelectionStart();
                Editable editable = msgEditor.getText();
                boolean isFace = false;
                if (index <= 0)
                    return;
                if (editable.charAt(index - 1) == ']') {
                    for (int i = index - 2; i >= 0; i--) {
                        if (editable.charAt(i) == '[') {
                            String faceChar = editable.subSequence(i, index).toString();
                            if (FaceManager.isFaceChar(faceChar)) {
                                editable.delete(i, index);
                                isFace = true;
                            }
                            break;
                        }
                    }
                }
                if (!isFace) {
                    editable.delete(index - 1, index);
                }
            }

            @Override
            public void onEmojiClick(Emoji emoji) {
                int index = msgEditor.getSelectionStart();
                Editable editable = msgEditor.getText();
                editable.insert(index, emoji.getFilter());
                FaceManager.handlerEmojiText(msgEditor, editable.toString());
            }

            @Override
            public void onCustomFaceClick(int groupIndex, Emoji emoji) {
                msgHandler.sendMessage(MessageInfoUtil.buildCustomFaceMessage(groupIndex, emoji.getFilter()));
            }
        });
        fragmentManager.beginTransaction().replace(R.id.more_groups, faceFragment).commitAllowingStateLoss();
        if (inputHandler != null)
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    inputHandler.popupAreaShow();
                }
            }, 100);

        //      }
        //  }, 100);

    }

    private void showActionsGroup() {
        if (fragmentManager == null)
            fragmentManager = activity.getSupportFragmentManager();
        if (actionsFragment == null)
            actionsFragment = new ChatActionsFragment();

        actionsFragment.setActions(actions);
        hideSoftInput();
        moreGroup.setVisibility(View.VISIBLE);
        fragmentManager.beginTransaction().replace(R.id.more_groups, actionsFragment).commitAllowingStateLoss();
        if (inputHandler != null)
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    inputHandler.popupAreaShow();
                }
            }, 100);
    }

    private void hideActionsGroup() {
        moreGroup.setVisibility(View.GONE);
    }

    @Override
    public void recordComplete(long duration) {
        if (inputHandler != null) {
            if (audioCancel) {
                inputHandler.stopRecording();
                return;
            }
            if (duration < 500) {
                inputHandler.tooShortRecording();
                return;
            }
            inputHandler.stopRecording();
        }

        if (msgHandler != null) {
            SoundPlayUtils.play(1);
            msgHandler.sendMessage(MessageInfoUtil.buildAudioMessage(UIKitAudioArmMachine.getInstance().getRecordAudioPath(), (int) duration));
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() > TUIKit.getBaseConfigs().getMaxInputTextLength()) {
            msgEditor.setText(s.subSequence(1, s.length()));
            UIUtils.toastLongMessage(activity.getString(R.string.max_message_length));
            return;
        }


        if (!TextUtils.isEmpty(s.toString().trim())) {
            sendAble = true;
            sendIv.setVisibility(View.VISIBLE);
            moreBtn.setVisibility(View.GONE);
            if (msgEditor.getLineCount() != lastMsgLineCount) {
                lastMsgLineCount = msgEditor.getLineCount();
                if (inputHandler != null)
                    inputHandler.popupAreaShow();
            }
        } else {
            sendAble = false;
            sendIv.setVisibility(View.GONE);
            moreBtn.setVisibility(View.VISIBLE);

        }
    }


    public interface MessageHandler {
        void sendMessage(MessageInfo msg);
    }

    public interface PersonalInfo {
        void sendPersonalInfo();
    }

    public interface ChatInputHandler {

        void popupAreaShow();

        void popupAreaHide();

        void startRecording();

        void stopRecording();

        void tooShortRecording();

        void cancelRecording();
    }

}