# Synchronized-Clock
An Android application with a time display switching from NTPtime to Systemtime
when the internet connection is lost.


<img src="/systemtime.png" width="400" hight="1200"> <img src="/ntptime.png" width="400" hight="1200">
<p><br></p>


### Using the SNTPClient


```java
  SNTPClient.getDate(TimeZone.getTimeZone("America/New_York"), new SNTPClient.Listener() {
                                @Override
                                public void onTimeResponse(String rawDate, Date date, Exception ex) {
                                    /*
                                    If "date" is returned as null, internet is inaccessible,
                                    or the NTPserver can't be reached.
                                    Sets System time in textview and "SystemTime" message in textView2
                                    */
                                    if (date == null) {
                                        long systemTimeNow = System.currentTimeMillis();
                                        SimpleDateFormat systemTimeFormat = new SimpleDateFormat("kk:mm:ss");
                                        String timeString = systemTimeFormat.format(systemTimeNow);
                                        textView.setText(timeString);
                                        textView2.setText("System Time :");

                                    } else {
                                        /*
                                        If internet is accessible this will run.
                                        Sets the time to NtpTime, as well as changes the time-type message.
                                        */
                                        SimpleDateFormat NTPtimeFormat = new SimpleDateFormat("kk:mm:ss");
                                        String ntimeString = NTPtimeFormat.format(date);
                                        textView.setText(ntimeString);
                                        textView2.setText("NTP Time :");
                                    }
                                }
                            });
```
The SNTPClient is used in a thread and updated every second. It will connect to the chosen NTPserver (time.google.com as default) and return a date. If the request for a date fails and is returned as null, system time will be displayed instead. This will of course happen everytime internet is unavailable.

##### Thanks to user aslamanver for the SNTP-client!
##### https://github.com/aslamanver/sntp-client-android

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

This is used only for the check internet button.
Each time the button is pressed a networkcallback is sent to retrieve
the status.
