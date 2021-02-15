### WebviewVideo
Enable Fullscreen mode in any videos with Landscape without reload Activity/url using savedInstanceState() or onRestoreInstanceState() in webview Android.
sample app for displaying Html5Video using Webview in Android.

I think the main problem is that you call web.loadUrl(webURL); also when savedInstanceState != null

QUESTION :- When I rotate my screen, the WebView reloads the whole page.I can't have this since some of my content contains dynamic/random material. Currently when rotated the screen reloads the original URL from the loadUrl() method.

#### Below Are The Solution

##### EDIT 1:-

Try:

if (savedInstanceState == null)

{

  webView.loadUrl(webURL);
  
}



##### EDIT 2: 
You also need the onSaveInstanceState and onRestoreInstanceState override.

@Override
protected void onSaveInstanceState(Bundle outState )

{

super.onSaveInstanceState(outState);

webView.saveState(outState);

}

@Override

protected void onRestoreInstanceState(Bundle savedInstanceState)

{

super.onRestoreInstanceState(savedInstanceState);

webView.restoreState(savedInstanceState);

}



#### Note: 
Please also add in your AndroidManifest.xml in your Activity 
##### android:configChanges="orientation|screenSize" 

Thanks
