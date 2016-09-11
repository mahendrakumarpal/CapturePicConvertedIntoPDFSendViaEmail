package com.example.captureimagesendthrough;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	private Button camera;
	private Button sendMail;
	private Image myImg;
	ArrayList<String> f = new ArrayList<String>();// list of file paths
	Document document;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initComponent();
		registerEvent();

	}

	private void registerEvent() {
		// TODO Auto-generated method stub
		camera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(cameraIntent, 0);
			}
		});
		sendMail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				File[] listFile;
				// Bitmap bMapName;
				File collectImages = new File(Environment.getExternalStorageDirectory(), "/collection.pdf");
				if (!collectImages.exists()) {
					try {
						collectImages.createNewFile();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				File fileImages = new File(Environment.getExternalStorageDirectory() + "/saved_images");

				if (fileImages.isDirectory()) {
					listFile = fileImages.listFiles();
					List<File> tempList = new ArrayList<File>(Arrays.asList(listFile));
					// System.out.println(">>>>>>>>>>>>>>tempList"+tempList.get(0));
					try {
						Image img = Image.getInstance(tempList.get(0).toString());
						document = new Document(img);
						PdfWriter.getInstance(document, new FileOutputStream(collectImages));
						document.open();
						for (int i = 0; i < tempList.size(); i++) {
							img = Image.getInstance(tempList.get(i).toString());
							document.setPageSize(img);
							document.newPage();
							img.setAbsolutePosition(0, 0);
							document.add(img);
						}
					} catch (Exception e) {
						// TODO: handle exception
					}

					document.close();
				}

				String[] mailto = { "" };
				Uri uri = Uri.fromFile(
						new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/collection.pdf"));
				Intent emailIntent = new Intent(Intent.ACTION_SEND);
				emailIntent.putExtra(Intent.EXTRA_EMAIL, mailto);
				emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Image PDF Report");
				emailIntent.setType("application/pdf");
				emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
				startActivity(Intent.createChooser(emailIntent, "Send email using:"));

				/*
				 * Document document = new Document(); File[] listFile; //Bitmap
				 * bMapName; File collectImages=new
				 * File(Environment.getExternalStorageDirectory(),
				 * "/collection.pdf"); if (!collectImages.exists()) { try {
				 * collectImages.createNewFile(); } catch(IOException e) { //
				 * TODO Auto-generated catch block e.printStackTrace(); } }
				 * 
				 * File fileImages = new
				 * File(Environment.getExternalStorageDirectory()+
				 * "/saved_images");
				 * 
				 * 
				 * if (fileImages.isDirectory()) { listFile =
				 * fileImages.listFiles(); List<File> tempList = new
				 * ArrayList<File>(Arrays.asList(listFile));
				 * //System.out.println(">>>>>>>>>>>>>>tempList"+tempList.
				 * get(0));
				 * 
				 * for (int i = 0; i < tempList.size(); i++) {
				 * 
				 * try { PdfWriter.getInstance(document,new
				 * FileOutputStream(collectImages)); document.open();
				 * System.out.println(">>>>>>>>>>>>>>tempList"+tempList.get(
				 * i)); FileInputStream in = new
				 * FileInputStream(tempList.get(i));
				 * System.out.println(in.toString()); BufferedInputStream buf =
				 * new BufferedInputStream(in); byte[] bMapArray= new
				 * byte[buf.available()]; buf.read(bMapArray); Bitmap bMapName =
				 * BitmapFactory.decodeByteArray(bMapArray, 0,
				 * bMapArray.length);
				 * 
				 * System.out.println(">>>>bitmap"+bMapName);
				 * ByteArrayOutputStream stream = new ByteArrayOutputStream();
				 * bMapName.compress(Bitmap.CompressFormat.JPEG, 100, stream);
				 * 
				 * myImg = Image.getInstance(stream.toByteArray()); //
				 * document.setPageSize(myImg); document.newPage();
				 * myImg.setAlignment(myImg.ALIGN_CENTER);
				 * 
				 * document.add(myImg);
				 * 
				 * } catch (FileNotFoundException e) { // TODO Auto-generated
				 * catch block e.printStackTrace(); } catch (DocumentException
				 * e) { // TODO Auto-generated catch block e.printStackTrace();
				 * } catch (IOException e) { // TODO Auto-generated catch block
				 * e.printStackTrace(); } catch(Exception e) {
				 * e.printStackTrace(); }
				 * //f.add(listFile[i].getAbsolutePath()); } document.close(); }
				 */

				/*
				 * Intent email = new Intent(Intent.ACTION_SEND);
				 * email.putExtra(Intent.EXTRA_EMAIL,
				 * "mahendra.r@motionpixel.co.in");
				 * email.putExtra(Intent.EXTRA_SUBJECT, "");
				 * email.putExtra(Intent.EXTRA_TEXT, ""); Uri uri =
				 * Uri.fromFile( new File(collectImages,"/collection.pdf"));
				 * email.putExtra(Intent.EXTRA_STREAM, uri);
				 * email.setType("application/pdf");
				 * email.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				 * startActivity(email);
				 */

			}
		});

	}

	private void initComponent() {
		// TODO Auto-generated method stub
		camera = (Button) findViewById(R.id.camera);
		sendMail = (Button) findViewById(R.id.sendMail);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			Bundle extras = data.getExtras();
			Bitmap mImageBitmap = (Bitmap) extras.get("data");
			// img.setImageBitmap(mImageBitmap);
			String root = Environment.getExternalStorageDirectory().toString();
			File myDir = new File(root + "/saved_images");
			myDir.mkdirs();
			Random generator = new Random();
			int n = 10000;
			n = generator.nextInt(n);
			String fname = "Image_" + n + ".jpg";
			File file = new File(myDir, fname);
			if (file.exists())
				file.delete();
			try {
				FileOutputStream out = new FileOutputStream(file);
				mImageBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
				out.flush();
				out.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
			/*
			 * String fpath =
			 * Environment.getExternalStorageDirectory().getAbsolutePath() +
			 * "/sample.pdf"; File file = new File(fpath);
			 * 
			 * if (!file.exists()) { try { file.createNewFile(); } catch
			 * (IOException e) { // TODO Auto-generated catch block
			 * e.printStackTrace(); } }
			 * 
			 */
		}
	}
}
