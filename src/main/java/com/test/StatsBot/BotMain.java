package com.test.StatsBot;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

public class BotMain {
    public static void main(String[] args)
    {
        BotCommandMod handler = new BotCommandMod();
        String token = "NTkwMTI2ODg5NDc0MDY0Mzk0.XRRxzg.LHsM1-spKOZ9lrakzST35NyRwEk";
        DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();
        System.out.println("봇을 시작하는중: " + api.getYourself().getDiscriminatedName());
        api.addMessageCreateListener(event -> {
            if(event.getMessageContent().startsWith("!"))
            {
                System.out.println("사용자 "+ event.getMessage().getAuthor().getDiscriminatedName() +"의 명령어 처리 시작: " + event.getMessageContent());
                if(handler.HandleCommand(event))
                {
                    System.out.println("사용자 "+ event.getMessage().getAuthor().getDiscriminatedName()  +"의 명령어 처리 완료: " + event.getMessageContent());
                }
                else {
                    System.out.println("사용자 "+ event.getMessage().getAuthor().getDiscriminatedName()  +"의 명령어 처리 실패: " + event.getMessageContent());
                }
            }
        });
    }
}
