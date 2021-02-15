# WebviewVideo
Enable Fullscreen mode in any videos with Landscape without reload Activity/url using savedInstanceState() or onRestoreInstanceState() in webview Android.
sample app for displaying Html5Video using Webview in Android.

I think the main problem is that you call web.loadUrl(webURL); also when savedInstanceState != null

##### EDIT 1:-

Try:

if (savedInstanceState == null)

{

  web.loadUrl(webURL);
  
}



##### EDIT 2: 
You also need the onSaveInstanceState and onRestoreInstanceState override.

@Override
protected void onSaveInstanceState(Bundle outState )

{

super.onSaveInstanceState(outState);

web.saveState(outState);

}

@Override

protected void onRestoreInstanceState(Bundle savedInstanceState)

{

super.onRestoreInstanceState(savedInstanceState);

web.restoreState(savedInstanceState);

}



#### Note: 
Please also add in your AndroidManifest.xml in your Activity 
##### android:configChanges="orientation|screenSize" 

Thanks
