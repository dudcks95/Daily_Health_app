package com.example.dailyhealth.db;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.room.TypeConverter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Converters {
    //   이미지를 DB에 저장할 수 있는 형태로 변환 (Bitmap -> ByteArray)
    @TypeConverter
    public byte[] fromBitmap(Bitmap bmp){
        // Byte Array 동적으로 받아서 출력한다.
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // 압축할 Bitmap(이미지), 압축할 퀄리티(얼마나 압축할것인가), 압축된 Byte Array받을 outputStream
        bmp.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

        // 사용 완료된 메모리 해제
        try {
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        bmp.recycle();

        return outputStream.toByteArray();
    }

    // 화면에 표시할 수 있는 형태로 전환 / DB에 저장된 ByteArray -> Bitmap
    @TypeConverter
    public Bitmap toBitmap(byte[] bytes){
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }


}
