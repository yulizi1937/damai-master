# OKHttp  上传下载进度展示库

**up**

```
//OkHttpClient请求Client
private static final OkHttpClient client = new OkHttpClient();

File file = new File("/sdcard/1.doc");
//此文件必须在手机上存在，实际情况下请自行修改，这个目录下的文件只是在我手机中存在。


//这个是非ui线程回调，不可直接操作UI
final ProgressListener progressListener = new ProgressListener() {
	@Override
	public void onProgress(long bytesWrite, long contentLength, boolean done) {
		Log.e("TAG", "bytesWrite:" + bytesWrite);
		Log.e("TAG", "contentLength" + contentLength);
		Log.e("TAG", (100 * bytesWrite) / contentLength + " % done ");
		Log.e("TAG", "done:" + done);
		Log.e("TAG", "================================");
	}
};


//这个是ui线程回调，可直接操作UI
final UIProgressListener uiProgressRequestListener = new UIProgressListener() {
	@Override
	public void onUIProgress(long bytesWrite, long contentLength, boolean done) {
		Log.e("TAG", "bytesWrite:" + bytesWrite);
		Log.e("TAG", "contentLength" + contentLength);
		Log.e("TAG", (100 * bytesWrite) / contentLength + " % done ");
		Log.e("TAG", "done:" + done);
		Log.e("TAG", "================================");
		//ui层回调
		uploadProgress.setProgress((int) ((100 * bytesWrite) / contentLength));
		//Toast.makeText(getApplicationContext(), bytesWrite + " " + contentLength + " " + done, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onUIStart(long bytesWrite, long contentLength, boolean done) {
		super.onUIStart(bytesWrite, contentLength, done);
		Toast.makeText(getApplicationContext(),"start",Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onUIFinish(long bytesWrite, long contentLength, boolean done) {
		super.onUIFinish(bytesWrite, contentLength, done);
		Toast.makeText(getApplicationContext(),"end",Toast.LENGTH_SHORT).show();
	}
};

//构造上传请求，类似web表单
RequestBody requestBody = new MultipartBuilder().type(MultipartBuilder.FORM)
		.addFormDataPart("hello", "android")
		.addFormDataPart("photo", file.getName(), RequestBody.create(null, file))
		.addPart(Headers.of("Content-Disposition", "form-data; name=\"another\";filename=\"another.dex\""), RequestBody.create(MediaType.parse("application/octet-stream"), file))
		.build();

//进行包装，使其支持进度回调
final Request request = new Request.Builder().url("http://your.url").post(ProgressHelper.addProgressRequestListener(requestBody, uiProgressRequestListener)).build();
//开始请求
client.newCall(request).enqueue(new Callback() {
	@Override
	public void onFailure(Request request, IOException e) {
		Log.e("TAG", "error ", e);
	}

	@Override
	public void onResponse(Response response) throws IOException {
		Log.e("TAG", response.body().string());
	}
});
```

**download**

```
//OkHttpClient请求Client
private static final OkHttpClient client = new OkHttpClient();

//这个是非ui线程回调，不可直接操作UI
final ProgressListener progressResponseListener = new ProgressListener() {
	@Override
	public void onProgress(long bytesRead, long contentLength, boolean done) {
		Log.e("TAG", "bytesRead:" + bytesRead);
		Log.e("TAG", "contentLength:" + contentLength);
		Log.e("TAG", "done:" + done);
		if (contentLength != -1) {
			//长度未知的情况下回返回-1
			Log.e("TAG", (100 * bytesRead) / contentLength + "% done");
		}
		Log.e("TAG", "================================");
	}
};


//这个是ui线程回调，可直接操作UI
final UIProgressListener uiProgressResponseListener = new UIProgressListener() {
	@Override
	public void onUIProgress(long bytesRead, long contentLength, boolean done) {
		Log.e("TAG", "bytesRead:" + bytesRead);
		Log.e("TAG", "contentLength:" + contentLength);
		Log.e("TAG", "done:" + done);
		if (contentLength != -1) {
			//长度未知的情况下回返回-1
			Log.e("TAG", (100 * bytesRead) / contentLength + "% done");
		}
		Log.e("TAG", "================================");
		//ui层回调
		downloadProgeress.setProgress((int) ((100 * bytesRead) / contentLength));
		//Toast.makeText(getApplicationContext(), bytesRead + " " + contentLength + " " + done, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onUIStart(long bytesRead, long contentLength, boolean done) {
		super.onUIStart(bytesRead, contentLength, done);
		Toast.makeText(getApplicationContext(),"start",Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onUIFinish(long bytesRead, long contentLength, boolean done) {
		super.onUIFinish(bytesRead, contentLength, done);
		Toast.makeText(getApplicationContext(),"end",Toast.LENGTH_SHORT).show();
	}
};

//构造请求
final Request request1 = new Request.Builder()
		.url("http://121.41.119.107:81/test/1.doc")
		.build();

//包装Response使其支持进度回调
ProgressHelper.addProgressResponseListener(client, uiProgressResponseListener).newCall(request1).enqueue(new Callback() {
	@Override
	public void onFailure(Request request, IOException e) {
		Log.e("TAG", "error ", e);
	}

	@Override
	public void onResponse(Response response) throws IOException {
		Log.e("TAG", response.body().string());
	}
});
```
