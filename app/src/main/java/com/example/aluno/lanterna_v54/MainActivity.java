package com.example.aluno.lanterna_v54;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.speech.RecognizerIntent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    //Atribuindo Atributos
    private Switch btn1;
    private Camera mcamera;
    private boolean FlashOn;
    private boolean hasFlash;
    private Camera.Parameters fparams;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Relacionando Buttons de xml e java
        btn1 = (Switch)findViewById(R.id.simpleSwitch1);

        //Verificação de Flash do dispositivo;
        hasFlash = getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        if (!hasFlash){
            AlertDialog alert = new AlertDialog.Builder(MainActivity.this).create();
            alert.setTitle("Error");
            alert.setMessage("Desculpa, mas seu dispositivo não possui Flash!");
            alert.setButton(Dialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            alert.show();
        }

        // Chamando Classes:
        getCamera();

        btn1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    turnOnFlash();
                }else{
                    turnOffFlash();
                }
            }
        });

    }

    private void getCamera() {
        if (mcamera == null) {
            try {
                mcamera = Camera.open();
                fparams = mcamera.getParameters();
            } catch (RuntimeException e){
                Log.e("Camera Erro. Falhou!", e.getMessage());
            }
        }
    }

    //Tornando Flash-On
    private void turnOnFlash(){
        if(!FlashOn){
            if (mcamera == null || fparams == null){
                return;
            }
            fparams = mcamera.getParameters();
            fparams.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            mcamera.setParameters(fparams);
            mcamera.startPreview();
            FlashOn = true;
        }
    }

    //Tornando Flash-Off
    private void turnOffFlash(){
        if(FlashOn){
            if (mcamera == null || fparams == null){
                return;
            }

            fparams = mcamera.getParameters();
            fparams.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            mcamera.setParameters(fparams);
            mcamera.stopPreview();
            FlashOn = false;

        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    @Override
    protected void onPause(){
        super.onPause();

        //Quando Pausado tornar Flash-Off
        turnOffFlash();
    }

    @Override
    protected void onRestart(){
        super.onRestart();
    }

    @Override
    protected void onResume(){
        super.onResume();
        //Em Resumo tornando Flash-ON
        if(hasFlash)
            turnOnFlash();
    }

    @Override
    protected void onStart(){
        super.onStart();

        getCamera();
    }

    @Override
    protected void onStop(){
        super.onStop();

        if (mcamera != null){
            mcamera.release();
            mcamera = null;
        }
    }

    // (FUTURO) Fase 2: Ativando Classes de Reconhecimento de Voz!

    // (FUTURO) Fase 3: Ativando Classes de Movimento!

}
