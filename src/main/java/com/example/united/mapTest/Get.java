package com.example.united.mapTest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ibm.icu.impl.number.parse.InfinityMatcher;
import io.socket.client.IO;
import io.socket.client.Socket;
import net.minecraft.world.level.Level;

import java.net.URISyntaxException;
import java.util.ArrayList;

import static com.example.united.mapTest.Const.local_ip;

public class Get {
    public static ArrayList<ArrayList<Integer>> l1s=new ArrayList<>();
    public static ArrayList<Integer> l1=new ArrayList<>();
    public static ArrayList<Integer> l2=new ArrayList<>();
    public static void listenClickEvent(Socket socket, Level level) {
        socket.on("coordinate_broadcast", args -> {
            Gson gson = new Gson();
            JsonObject data = gson.fromJson(args[0].toString(), JsonObject.class);
            int x = data.get("x").getAsInt();
            int y = data.get("y").getAsInt();
            int z = data.get("z").getAsInt();
            l1.add(x);
            l1.add(y);
            l1.add(z);
            ArrayList<Integer> l1n=new ArrayList<>();
            l1n.add(x);
            l1n.add(y);
            l1n.add(z);
            l1s.add(l1n);
            System.out.println(l1s);
        });

        socket.on("rightClick_coordinate_broadcast", args -> {
            Gson gson = new Gson();
            JsonObject data = gson.fromJson(args[0].toString(), JsonObject.class);
            int x = data.get("x").getAsInt();
            int y = data.get("y").getAsInt();
            int z = data.get("z").getAsInt();
            l2.add(x);
            l2.add(y);
            l2.add(z);
            System.out.println(2);
        });

        socket.on("equipmentLaunched",args->{

            Gson gson = new Gson();
            JsonObject data = gson.fromJson(args[0].toString(), JsonObject.class);
            int tX1=data.get("tX1").getAsInt();
            int tX2= Integer.MAX_VALUE;
            if(!data.get("tX2").isJsonNull()){
                tX2=data.get("tX2").getAsInt();
            }
            int tZ1=data.get("tZ1").getAsInt();
            int tZ2= Integer.MAX_VALUE;
            if(!data.get("tZ2").isJsonNull()){

                tZ2=data.get("tZ2").getAsInt();
            }
            String type= data.getAsJsonObject("equipment").get("type").getAsString();

            handleStrategies.handleAllStrategies(level,type,tX1,tZ1,tX2,tZ2);

        });
    }

    public static void listenDisConnectEvent(Socket socket) {
        socket.on(Socket.EVENT_DISCONNECT, args3 -> {
            System.out.println("Disconnected from server");
        });
    }

}
