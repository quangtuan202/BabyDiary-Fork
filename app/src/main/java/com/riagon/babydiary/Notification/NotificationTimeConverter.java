package com.riagon.babydiary.Notification;

public class NotificationTimeConverter {
    public static String convertToTime(Long time){
        String stringHours;
        String stringMinutes;
        String stringSeconds;

        long hours = time/3600;
        long minutes= (time/60)%60;
        long seconds= time%60;
       if(hours<10 ){
            stringHours="0"+hours;
        }
        else{
            stringHours= String.valueOf(hours);
        }

        if(minutes<10){
            stringMinutes="0"+minutes;
        }
        else{
            stringMinutes= String.valueOf(minutes);
        }
        if(seconds<10){
            stringSeconds="0"+seconds;
        }
        else{
            stringSeconds= String.valueOf(seconds);
        }

        return(stringHours+":"+stringMinutes+":"+stringSeconds);


    }

    public static String convertToTimeWithTwoFormat(Long time){
        String hms;
        if(time/3600>0) {
            hms = String.format("%02d:%02d:%02d", time / 3600,
                    time / 60 % 60,
                    time % 60);
        }
        else{
            hms = String.format("%02d:%02d",
                    time / 60 % 60,
                    time % 60);
        }

        return hms;
    }

}
