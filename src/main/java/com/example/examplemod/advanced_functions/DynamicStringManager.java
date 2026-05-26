package com.example.examplemod.advanced_functions;

public class DynamicStringManager {
    public static String alphaEase=DynamicStringManager.toEaseDynamicExp("w","threshold(255 * (1 - (pt)^3))");

    public static String toEaseDynamicExp(String target,double times,String easeString,double offset,double scale){
        String easeBackType="("+easeString+")*"+times;
        String result=target+"<-"+easeBackType+";";
        return result
                .replace("pt", "((if(pt,-1000,-0.0001)*0+if(pt,0,1)*pt+if(pt,1.0001,1000)*1)+"+offset+")")
                .replace("pt","("+scale+"*pt)");
    }
    public static String toEaseDynamicExp(String target,double times,String easeString,double offset){
        String easeBackType="("+easeString+")*"+times;
        String result=target+"<-"+easeBackType+";";
        return result.replace("pt",
                "((if(pt,-1000,-0.0001)*0+if(pt,0,1)*pt+if(pt,1.0001,1000)*1)+"+offset+")");
    }
    public static String toEaseDynamicExp(String target,String string){
        return target+"<-"+string+";";
    }
    //f'(t)=f(1-t)
    public static String reverse(String easeString){
        return easeString.replace("pt","(1-pt)");
    }
    // if(pt,0,0.5)*(str).replace(t,(2t))+if(pt,0.5,1)*(str.replace(t,(2-2t)))
    // f'1t(t) = f(2t) 0<pt<0.5
    // f'2t(t)=f(2-2t) 0.5<pt<1
    public static String toBackAndForce(String easeString){
        return
                "if(pt,0,0.5)*"+"("+easeString.replace("pt","(2pt)")+")"+"+"
                        +"if(pt,0.50001,1)*"+"("+easeString.replace("pt","(2-2pt)")+")";
    }

}
