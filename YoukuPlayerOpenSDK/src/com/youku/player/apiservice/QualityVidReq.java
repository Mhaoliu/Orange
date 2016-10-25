package com.youku.player.apiservice;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;

public class QualityVidReq {
	private String url = "https://api.youku.com/quality/video/by/keyword.json?client_id=792b1d08a5348d0d&lengthtype=3&published=month&count=10";
	private URL urll;
	private HttpURLConnection httpURLConnection;
	private boolean isrunning = false;
	private GetUrlTask task;
	private static class SingletonHolder {
		private static final QualityVidReq INSTANCE = new QualityVidReq();
	}

	private QualityVidReq() {
		try {
			urll = new URL(url);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private GetQualityListener getQualityListener;

	public interface GetQualityListener {
		public void onResult(String vid);
	}

	public void getQualityVid(GetQualityListener mGetQualityListener) {
		getQualityListener = mGetQualityListener;
		if (!isrunning) {
			task = new GetUrlTask();
			task.execute();
		}
	}

	private class GetUrlTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			isrunning = true;
			String vid = "";
			InputStream is = null;
			try {
				httpURLConnection = (HttpURLConnection) urll.openConnection();
				httpURLConnection.setRequestMethod("GET");
				httpURLConnection.connect();
				is = httpURLConnection.getInputStream();
				StringBuffer out = new StringBuffer();
				byte[] b = new byte[4096];
				for (int n; (n = is.read(b)) != -1;) {
					out.append(new String(b, 0, n));
				}
				JSONObject json = new JSONObject(out.toString());
				JSONArray jary = json.optJSONArray("videos");
				int inx = (int) (Math.floor(Math.random() * jary.length()));
				JSONObject video = jary.optJSONObject(inx);
				vid = video.getString("id");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {

				try {
					if(is != null){
						is.close();
					}
					is = null;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					httpURLConnection.disconnect();
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
			isrunning = false;
			return vid;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (getQualityListener != null) {
				getQualityListener.onResult(result);
			}
		}
	}

	public static final QualityVidReq getInstance() {
		return SingletonHolder.INSTANCE;
	}
}
