package com.exemple.sychronizedclock;


import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;

import java.net.InetAddress;
import java.util.Date;

public class TimeRetriver {
    public Date getNTPTime() {

        // Här skapas ett nytt objekt av typen NTPUDPClient som används för att hämta tiden
        // från en NTP-server (Network Time Protocol) över UDP.
        NTPUDPClient timeClient = new NTPUDPClient();

        // Denna rad ställer in en timeout på 2000 millisekunder (2 sekunder) för NTP-klienten.
        // Om en förfrågan till NTP-servern inte besvaras inom denna tid kommer det att uppstå
        // ett timeout-fel.
        timeClient.setDefaultTimeout(2000);

        TimeInfo timeInfo;
        try {
            InetAddress inetAddress = InetAddress.getByName("time.google.com");
            timeInfo = timeClient.getTime(inetAddress);

            // Här hämtas tidsstämpeln för när meddelandet sändes från NTP-servern och konverterar
            // den till ett long-värde som representerar antalet millisekunder sedan 1 januari 1970.
            // Detta kallas en Unix timestamp
            long NTPTime = timeInfo.getMessage().getTransmitTimeStamp().getTime();
            Date date = new Date(NTPTime);

            // Skriver ut i terminalen att tiden kommer via Nätverk
            System.out.println("getTime() returning NTPServer time: " + date);

            // Returnernar ett datumobjekt
            return date;
        } catch (Exception e) {

            // Skriver ut i terminalen att tiden kommer via System
            System.out.println("getTime() returning System time: " + new Date());

            // Returnernar ett datumobjekt
            return new Date();
        }
    }
}