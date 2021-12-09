package com.pProject.ganada;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import gun0912.tedimagepicker.builder.TedImagePicker;
import gun0912.tedimagepicker.builder.listener.OnSelectedListener;

public class BottomSheetDialogFragment extends com.google.android.material.bottomsheet.BottomSheetDialogFragment {

    private String language, objectType;
    private TextView usingCameraOtherLanguageTv, usingGalleryOtherLanguageTv;
    private View usingCameraView, usingGalleryView;

    public BottomSheetDialogFragment(String objectType) {
        this.objectType = objectType;
    }

    //카메라 권한을 확인하는 launcher
    private ActivityResultLauncher<String> cameraPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    if (result) {   //승인됐으면
                        startCameraActivity();  //카메라 실행
                    } else {
                        Toast.makeText(requireContext(), "카메라 사용 권한을 허용해주세요.", Toast.LENGTH_SHORT).show();
                    }

                    dismiss();  //현재 다이얼로그 프래그먼트 닫기
                }
            }
    );

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_sheet_dialog, container, false);

        usingCameraOtherLanguageTv = (TextView) view.findViewById(R.id.camera_tv_other_language);
        usingGalleryOtherLanguageTv = (TextView) view.findViewById(R.id.gallery_tv_other_language);
        usingCameraView = (View) view.findViewById(R.id.camera_view);
        usingGalleryView = (View) view.findViewById(R.id.gallery_view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //카메라 촬영하기 view 클릭 리스너
        usingCameraView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA);
            }
        });
        //갤러리에서 가져오기 view 클릭 리스너
        usingGalleryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImage(); //갤러리로 이동하는 함수 호출
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        //sharedPreferences 에서 선택된 언어 가져오기
        language = getActivity().getSharedPreferences("Language", MODE_PRIVATE).getString("language", null);
        setLanguageUI(language);    //선택된 언어에 맞춰 TextView 의 텍스트를 설정하는 함수 호출
    }

    //선택된 언어에 맞춰 TextView 의 텍스트를 설정하는 함수
    private void setLanguageUI(String language) {
        switch (language) {
            case "english":
                usingCameraOtherLanguageTv.setText(R.string.using_camera_en);
                usingGalleryOtherLanguageTv.setText(R.string.using_gallery_en);
                break;
            case "china":
                usingCameraOtherLanguageTv.setText(R.string.using_camera_cn);
                usingGalleryOtherLanguageTv.setText(R.string.using_gallery_cn);
                break;
            case "vietnam":
                usingCameraOtherLanguageTv.setText(R.string.using_camera_vn);
                usingGalleryOtherLanguageTv.setText(R.string.using_gallery_vn);
                break;
            default:
                usingCameraOtherLanguageTv.setText(R.string.using_camera_jp);
                usingGalleryOtherLanguageTv.setText(R.string.using_gallery_jp);
                break;
        }
    }

    //CameraActivity 로 이동하는 함수
    private void startCameraActivity() {
        Intent intent = new Intent(requireActivity(), CameraActivity.class);
        intent.putExtra("objectType", this.objectType);

        startActivity(intent);
    }

    //갤러리로 이동하는 함수(TedImagePicker 라이브러리 활용)
    private void getImage() {
        TedImagePicker.with(requireContext()).start(new OnSelectedListener() {
            @Override
            public void onSelected(Uri uri) {
                if (objectType == "text") //텍스트 인식이면
                    extractText(uri);   //ML Kit 를 활용해 이미지 속에 있는 텍스트를 인식해 추출하는 함수 호출.
                else {  //사물 인식이면 바로 LearnWordActivity 로 이동
                    ((MainActivity) requireActivity()).startLearnWord(uri, "");
                    dismiss();
                }
            }
        });
    }

    //ML Kit 를 활용해 이미지 속에 있는 텍스트를 인식해 추출하는 함수
    public void extractText(Uri uri) {
        try {
            InputImage image = InputImage.fromFilePath(requireContext(), uri);

            TextRecognizer recognizer =
                    TextRecognition.getClient(new KoreanTextRecognizerOptions.Builder().build());

            recognizer.process(image)
                    .addOnSuccessListener(new OnSuccessListener<Text>() {
                        @Override
                        public void onSuccess(Text visionText) {
                            ((MainActivity) requireActivity()).startLearnWord(uri, visionText.getText());
                        }
                    })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    e.printStackTrace();
                                }
                            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}