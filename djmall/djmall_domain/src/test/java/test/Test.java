package test;

import java.util.Arrays;
import java.util.List;

public class Test {
/*
    public static void main(String[] args) {
        int [] arr = {12,44,76,2,46,8,845,22,45};
        for (int i = 0; i <arr.length-1 ; i++) {
            for (int j = 0; j <arr.length-i-1 ; j++) {
                //从小到大排序
                if(arr[j] > arr[j+1]){
                    int temp = arr[j];
                    arr[j]  = arr[j+1];
                    arr[j+1] = temp;
                }
            }
        }


        for (int i = 0; i <arr.length ; i++) {
            System.out.print(arr[i]+"--");
        }

    }*/

    public static int doFactorial(int n){
               if(n<0){
                        return -1;//传入的数据不合法
                     }
                if( n>=0 && n<=1){//递归结束的条件
                        return 1;
                }
        return n*doFactorial(n-1);
        }



   /* public static int doFactorial(int n){
               int result = 1;
              if(n<0){
                         return -1;//返回-1，说明传入数据不合法
                  }
               if(n==0){
                       return 1;
                   }
                for(int i =1;i<=n;i++){
                      result*=i;
                     }
                return result;

      }*/



    /*public static void main(String[] args) {

        System.out.println(doFactorial(7));
    }*/







}
