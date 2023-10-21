# Synchronized-Clock
An Android application with a time display switching between NTP-time and System-time

# Synchronized-Clock
An Android application with a time display switching from NTPtime to Systemtime
when the internet connection is lost.


![systemtime]("/systemtime.png")![ntptime]("/ntptime.png")
<p><br></p>



#### The InternetChecker class check the status of the connection and changes the connectedTo-boolean

```java
public class InternetChecker extends ConnectivityManager.NetworkCallback {
    @Override
    // Internet connection is available
    public void onAvailable(Network network) {
        super.onAvailable(network);
        // boolean set to false
        InternetBoolean.connectedTo = true;

    }

    @Override
    // Internet connection is lost
    public void onLost(Network network) {
        super.onLost(network);
      // boolean set to false
        InternetBoolean.connectedTo = false;
    }
```


#### Thread for updating the different times

```java
 Thread t = new Thread(){
                @Override
                public void run() {
                    try {
                            while(!isInterrupted()) {

                                Thread.sleep(1000);
                                runOnUiThread(() -> {
                                    if(InternetBoolean.connectedTo==false) {
                                        //If no internet is accessible this will run
                                        //Sets SystemTime in textview and "SystemTime" message in textView2
                                        long systemTimeNow = System.currentTimeMillis();
                                        SimpleDateFormat timeStringFormat = new SimpleDateFormat("kk:mm:ss");
                                        String timeString = timeStringFormat.format(systemTimeNow);
                                        textView.setText(timeString);
                                        textView2.setText("System Time:");
                                    }
                                    else{
                                    /*
                                    If internet is accessible this will run.
                                    Sets the time to NtpTime, as well as changes the internet message.
                                    The SNTPClient uses "time.google.com" as default host.
                                    */
                                   SNTPClient.getDate(TimeZone.getTimeZone("Asia/Colombo"), (rawDate, date, ex) -> {
                                        Calendar.getInstance().setTimeZone(TimeZone.getTimeZone("Asia/Colombo"));
                                        SimpleDateFormat timeFormat = new SimpleDateFormat("kk:mm:ss");
                                        String ntimeString = timeFormat.format(date);
                                        textView.setText(ntimeString);
                                        textView2.setText("NTP Time:");
                                    });}

                                });
                            }
                    } catch(InterruptedException e){
                        System.out.println("InterruptedException in Thread");
                }
            }

            };
```




#### Thanks to user aslamanver for the SNTP-client!
#### https://github.com/aslamanver/sntp-client-android