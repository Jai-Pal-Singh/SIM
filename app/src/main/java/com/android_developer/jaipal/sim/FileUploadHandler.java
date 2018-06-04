package com.android_developer.jaipal.sim;

import android.content.Context;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FileUploadHandler {


    Context mContext;
    int serverResponseCode = 0;
    String uploadFilePath ="";
    String uploadFileName ="";
//    String urlToVisit="http://172.21.7.55:80/SnTInspection/UploadToServer.php";
//    String urlToVisit="http://172.21.5.81:8080/SnTServlet/sendSms.jsp";
//    String upLoadServerUri="http://192.168.43.22:80/SnTInspection/pdf_upload.php";
    String upLoadServerUri="https://jaipal2013.000webhostapp.com/pdf_upload.php";


    public FileUploadHandler(Context mContext, File file) {
        this.mContext = mContext;

        File f  = file;
//        String content_type  = getMimeType(f.getPath());
        String content_type  = "application/pdf";

        String file_path = f.getAbsolutePath();
        OkHttpClient client = new OkHttpClient();
        String mimeType= URLConnection.guessContentTypeFromName(f.getName());
        RequestBody file_body = RequestBody.create( MediaType.parse(mimeType),f);

        RequestBody request_body = new MultipartBody.Builder()
                .setType( MultipartBody.FORM)
                .addFormDataPart("type",content_type)
                .addFormDataPart("uploaded_file",file_path.substring(file_path.lastIndexOf("/")+1), file_body)
                .build();

        Request request = new Request.Builder()
                .url(upLoadServerUri)
                .post(request_body)
                .build();

        try {
            Response response = client.newCall(request).execute();

            if(!response.isSuccessful()){
                throw new IOException("Error : "+response);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String getMimeType(String path) {

        String extension = MimeTypeMap.getFileExtensionFromUrl(path);

        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }

}
