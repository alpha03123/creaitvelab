package com.example.examplemod.advanced_functions;

import static com.example.examplemod.advanced_functions.DynamicStringManager.toBackAndForce;

public class MotionStrings {



    public static void main (String[] args ) {
        System.out.println(toBackAndForce(Quadratic[0]));
    }
    //pt从0-1,f(t)也是0-1
    public static String[] Linear = new String[]{
            "pt"  // Linear: f(t) = t
    };

    public  static String[] Quadratic = new String[]{
            "pt^2",  // Ease In
            "1 - (1 - pt)^2",  // Ease Out //最终为1
            "if(pt,0,0.5)*2*pt^2 + if(pt,0.50001,1)*(1 - 2*(1 - pt)^2)"  // Ease In Out


    };

    public static  String[] Cubic = new String[]{
            "pt^3",  // Ease In
            "1 - (1 - pt)^3",  // Ease Out
            "if(pt,0,0.5)*4*pt^3 + if(pt,0.50001,1)*(1 - 4*(1 - pt)^3)"  // Ease In Out
    };

    public static  String[] Quartic = new String[]{
            "pt^4",  // Ease In
            "1 - (1 - pt)^4",  // Ease Out
            "if(pt,0,0.5)*8*pt^4 + if(pt,0.50001,1)*(1 - 8*(1 - pt)^4)"  // Ease In Out
    };

    public  static String[] Sine = new String[]{
            "1 - cos((pi * pt)/2)",  // Ease In
            "sin((pi * pt)/2)",  // Ease Out
            "0.5*(1 -cos(pi * pt))"  // Ease In Out
    };

    public  static String[] Exponential = new String[]{
            "if(pt,0,0)*0 + if(pt,0,1)*pow(2, 10*(pt - 1))",  // Ease In
            "if(pt,1,1)*1 + if(pt,0,1)*(1 - pow(2, -10*pt))",  // Ease Out
            "if(pt,0,0.5)*(0.5*pow(2, 10*(2*pt - 1))) + if(pt,0.50001,1)*(0.5*(2 -pow(2, -10*(2*pt - 1))))"  // Ease In Out
    };

    public static  String[] Circular = new String[]{
            "1 - sqrt(1 - pt^2)",  // Ease In
            "sqrt(1 - (1 - pt)^2)",  // Ease Out
            "if(pt,0,0.5)*(0.5*(1 - sqrt(1 - (2*pt)^2))) + if(pt,0.50001,1)*(0.5*(sqrt(1 - (2*pt - 2)^2) + 1))"  // Ease In Out
    };

    public static  String[] Elastic = new String[]{
            "pow(2, 10*(pt - 1)) * sin((2*pi/3)*(10*pt - 1))",  // Ease In
            "1 - pow(2, -10*pt) * cos((2*pi/3)*10*pt)",  // Ease Out
            ""  // Elastic Ease In Out 通常需要更复杂分段，此处留空
    };

    public  static String[] Back = new String[]{
            "pt^3 - 1.70158*pt^2*(1 - pt)",  // Ease In
            "1 - ((1 - pt)^3 - 1.70158*(1 - pt)^2*pt)",  // Ease Out
            ""  // Back Ease In Out 通常需要自定义分段
    };

    public  static String[] Bounce = new String[]{
            "",  // Bounce 通常只有 Ease Out
            "if(pt,0,4/11.0)*7.5625*pt^2 + if(pt,4/11.0,8/11.0)*(7.5625*(pt - 6/11.0)^2 + 0.75) + if(pt,8/11.0,1)*(7.5625*(pt - 9/11.0)^2 + 15/16.0)",  // Ease Out
            ""  // Bounce Ease In 通常需要反向计算
    };
}